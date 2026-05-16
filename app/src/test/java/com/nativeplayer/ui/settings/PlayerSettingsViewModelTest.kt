package com.nativeplayer.ui.settings

import com.nativeplayer.data.model.PlayerSettings
import com.nativeplayer.data.repository.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class PlayerSettingsViewModelTest {

    @Mock
    private lateinit var preferencesManager: PreferencesManager

    private lateinit var viewModel: PlayerSettingsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        val defaultSettings = PlayerSettings()
        whenever(preferencesManager.getPlayerSettings()).thenReturn(defaultSettings)
        viewModel = PlayerSettingsViewModel(preferencesManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `save() should emit true when preferences save succeeds`() = runTest {
        // Given
        val settings = PlayerSettings(
            defaultPlaybackSpeed = 1.5f,
            preferredAudioLanguage = "eng"
        )
        whenever(preferencesManager.savePlayerSettings(any())).thenReturn(true)

        viewModel.update(settings)

        // When
        viewModel.save()
        advanceUntilIdle()

        // Then
        val result = viewModel.saveResult.first()
        assertTrue(result, "save() should emit true when commit succeeds")
    }

    @Test
    fun `save() should emit false when preferences save fails`() = runTest {
        // Given
        val settings = PlayerSettings(defaultPlaybackSpeed = 1.5f)
        whenever(preferencesManager.savePlayerSettings(any())).thenReturn(false)

        viewModel.update(settings)

        // When
        viewModel.save()
        advanceUntilIdle()

        // Then
        val result = viewModel.saveResult.first()
        assertFalse(result, "save() should emit false when commit fails")
    }

    @Test
    fun `save() should emit true on successful save`() = runTest {
        // Given
        val settings = PlayerSettings(defaultPlaybackSpeed = 1.5f)
        whenever(preferencesManager.savePlayerSettings(any())).thenReturn(true)

        viewModel.update(settings)

        // When
        viewModel.save()
        advanceUntilIdle()

        // Then
        val result = viewModel.saveResult.first()
        assertTrue(result, "save() should emit true after successful save")
    }

    @Test
    fun `save() should validate and coerce settings values`() = runTest {
        // Given
        val invalidSettings = PlayerSettings(
            defaultPlaybackSpeed = 3.0f,  // Should be coerced to max 2.0f
            seekForwardMs = 500_000L,     // Should be coerced to max 300_000L
            bufferSizeSeconds = 200       // Should be coerced to max 120
        )

        whenever(preferencesManager.savePlayerSettings(any())).thenReturn(true)

        viewModel.update(invalidSettings)

        // When
        viewModel.save()
        advanceUntilIdle()

        // Then
        val result = viewModel.saveResult.first()
        assertTrue(result)
        assertEquals(2.0f, viewModel.settings.value.defaultPlaybackSpeed)
        assertEquals(300_000L, viewModel.settings.value.seekForwardMs)
        assertEquals(120, viewModel.settings.value.bufferSizeSeconds)
    }

    @Test
    fun `update() should reflect new settings immediately`() {
        // Given
        val settings1 = PlayerSettings(defaultPlaybackSpeed = 1.5f)
        val settings2 = PlayerSettings(defaultPlaybackSpeed = 1.2f)

        viewModel.update(settings1)
        assertEquals(1.5f, viewModel.settings.value.defaultPlaybackSpeed)

        // When
        viewModel.update(settings2)

        // Then
        assertEquals(1.2f, viewModel.settings.value.defaultPlaybackSpeed, "settings should update immediately on update()")
    }
}

