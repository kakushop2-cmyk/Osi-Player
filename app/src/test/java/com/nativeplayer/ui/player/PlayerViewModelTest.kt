package com.nativeplayer.ui.player

import com.nativeplayer.data.model.Episode
import com.nativeplayer.data.model.PlayerSettings
import com.nativeplayer.data.model.Season
import com.nativeplayer.data.model.Series
import com.nativeplayer.data.repository.IptvRepository
import com.nativeplayer.data.repository.PreferencesManager
import com.nativeplayer.data.repository.WatchHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argWhere
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class PlayerViewModelTest {

    @Mock
    private lateinit var watchHistoryRepository: WatchHistoryRepository

    @Mock
    private lateinit var preferencesManager: PreferencesManager

    @Mock
    private lateinit var iptvRepository: IptvRepository

    private lateinit var viewModel: PlayerViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(preferencesManager.getPlayerSettings()).thenReturn(PlayerSettings())
        viewModel = PlayerViewModel(watchHistoryRepository, preferencesManager, iptvRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── loadNextEpisode ──────────────────────────────────────────────

    @Test
    fun `loadNextEpisode returns next episode in same season`() = runTest {
        val seasons = listOf(
            Season(1, listOf(
                Episode("e1", "Episode 1", 1, "url1", "30min"),
                Episode("e2", "Episode 2", 2, "url2", "30min"),
                Episode("e3", "Episode 3", 3, "url3", "30min")
            ))
        )
        val series = Series("s1", "Test Serie", null, "2024", 5.0f, "Plot", "Category", seasons)
        whenever(iptvRepository.loadSeriesInfo("s1")).thenReturn(series)

        val result = viewModel.loadNextEpisode("s1", 1, 2)

        assertNotNull(result)
        assertEquals("url3", result?.url)
        assertEquals(1, result?.seasonNumber)
        assertEquals(3, result?.episodeNumber)
    }

    @Test
    fun `loadNextEpisode returns first episode of next season`() = runTest {
        val seasons = listOf(
            Season(1, listOf(
                Episode("e1", "Episode 1", 1, "url1", "30min")
            )),
            Season(2, listOf(
                Episode("e2", "Episode 2", 1, "url2", "30min")
            ))
        )
        val series = Series("s1", "Test Serie", null, "2024", 5.0f, "Plot", "Category", seasons)
        whenever(iptvRepository.loadSeriesInfo("s1")).thenReturn(series)

        val result = viewModel.loadNextEpisode("s1", 1, 1)

        assertNotNull(result)
        assertEquals("url2", result?.url)
        assertEquals(2, result?.seasonNumber)
        assertEquals(1, result?.episodeNumber)
    }

    @Test
    fun `loadNextEpisode returns null for last episode of last season`() = runTest {
        val seasons = listOf(
            Season(1, listOf(
                Episode("e1", "Last Episode", 10, "url1", "30min")
            ))
        )
        val series = Series("s1", "Test Serie", null, "2024", 5.0f, "Plot", "Category", seasons)
        whenever(iptvRepository.loadSeriesInfo("s1")).thenReturn(series)

        val result = viewModel.loadNextEpisode("s1", 1, 10)

        assertNull(result)
    }

    @Test
    fun `loadNextEpisode returns null when series not found`() = runTest {
        whenever(iptvRepository.loadSeriesInfo("unknown")).thenReturn(null)

        val result = viewModel.loadNextEpisode("unknown", 1, 1)

        assertNull(result)
    }

    @Test
    fun `loadNextEpisode returns null when season not found`() = runTest {
        val seasons = listOf(
            Season(1, listOf(
                Episode("e1", "Episode 1", 1, "url1", "30min")
            ))
        )
        val series = Series("s1", "Test Serie", null, "2024", 5.0f, "Plot", "Category", seasons)
        whenever(iptvRepository.loadSeriesInfo("s1")).thenReturn(series)

        val result = viewModel.loadNextEpisode("s1", 99, 1)

        assertNull(result)
    }

    @Test
    fun `loadNextEpisode returns null when episode not found`() = runTest {
        val seasons = listOf(
            Season(1, listOf(
                Episode("e1", "Episode 1", 1, "url1", "30min")
            ))
        )
        val series = Series("s1", "Test Serie", null, "2024", 5.0f, "Plot", "Category", seasons)
        whenever(iptvRepository.loadSeriesInfo("s1")).thenReturn(series)

        val result = viewModel.loadNextEpisode("s1", 1, 99)

        assertNull(result)
    }

    @Test
    fun `loadNextEpisode returns null when repository throws exception`() = runTest {
        whenever(iptvRepository.loadSeriesInfo("s1")).thenThrow(RuntimeException("Network error"))

        val result = viewModel.loadNextEpisode("s1", 1, 1)

        assertNull(result)
    }

    // ── savePosition mit Season/Episode ──────────────────────────────

    @Test
    fun `savePosition saves with season and episode numbers`() = runTest {
        viewModel.savePosition(
            url = "http://test.stream/url",
            position = 5000L,
            duration = 120000L,
            type = ContentType.SERIES,
            contentId = "series123",
            title = "Test Episode",
            imageUrl = "",
            seasonNumber = 2,
            episodeNumber = 5
        )

        testDispatcher.scheduler.advanceUntilIdle()

        verify(watchHistoryRepository).savePosition(argWhere { entity ->
            entity.itemId == "series123" &&
                entity.seasonNumber == 2 &&
                entity.episodeNumber == 5 &&
                entity.type == "series" &&
                entity.position == 5000L &&
                entity.duration == 120000L &&
                entity.title == "Test Episode" &&
                entity.streamUrl == "http://test.stream/url"
        })
    }

    @Test
    fun `savePosition does not save for LIVE content`() = runTest {
        viewModel.savePosition(
            url = "http://test.stream/live",
            position = 5000L,
            duration = 120000L,
            type = ContentType.LIVE
        )

        testDispatcher.scheduler.advanceUntilIdle()

        verify(watchHistoryRepository, org.mockito.kotlin.never()).savePosition(any())
    }

    @Test
    fun `getPlayerSettings returns settings from preferences`() {
        val expected = PlayerSettings(autoPlayNext = true)
        whenever(preferencesManager.getPlayerSettings()).thenReturn(expected)

        val result = viewModel.getPlayerSettings()

        assertEquals(expected, result)
    }
}
