package com.nativeplayer.ui.viewmodel

import com.nativeplayer.data.model.CategoryVisibilitySettings
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
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class CategoryVisibilityViewModelTest {

    @Mock
    private lateinit var preferencesManager: PreferencesManager

    @Mock
    private lateinit var repository: com.nativeplayer.data.repository.IptvRepository

    private lateinit var viewModel: CategoryVisibilityViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        val defaultSettings = CategoryVisibilitySettings()
        whenever(preferencesManager.getCategoryVisibility()).thenReturn(defaultSettings)
        viewModel = CategoryVisibilityViewModel(preferencesManager, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have empty hidden categories`() = runTest {
        // Given
        whenever(preferencesManager.getCategoryVisibility()).thenReturn(CategoryVisibilitySettings())

        // When
        val visibility = viewModel.categoryVisibility.first()

        // Then
        assertTrue(visibility.hiddenCategoriesLiveTV.isEmpty())
        assertTrue(visibility.hiddenCategoriesMovies.isEmpty())
        assertTrue(visibility.hiddenCategoriesSeries.isEmpty())
    }

    @Test
    fun `toggleCategoryVisibility should hide category when not hidden`() = runTest {
        // Given
        val categoryName = "Sports"
        assertFalse(viewModel.isCategoryHidden("live", categoryName))

        // When
        viewModel.toggleCategoryVisibility("live", categoryName)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.isCategoryHidden("live", categoryName))
    }

    @Test
    fun `toggleCategoryVisibility should show category when hidden`() = runTest {
        // Given
        val categoryName = "Sports"
        viewModel.toggleCategoryVisibility("live", categoryName)
        advanceUntilIdle()
        assertTrue(viewModel.isCategoryHidden("live", categoryName))

        // When
        viewModel.toggleCategoryVisibility("live", categoryName)
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.isCategoryHidden("live", categoryName))
    }

    @Test
    fun `toggleCategoryVisibility should handle movies type correctly`() = runTest {
        // Given
        val categoryName = "Action"

        // When
        viewModel.toggleCategoryVisibility("movies", categoryName)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.isCategoryHidden("movies", categoryName))
        assertFalse(viewModel.isCategoryHidden("live", categoryName))
        assertFalse(viewModel.isCategoryHidden("series", categoryName))
    }

    @Test
    fun `toggleCategoryVisibility should handle series type correctly`() = runTest {
        // Given
        val categoryName = "Drama"

        // When
        viewModel.toggleCategoryVisibility("series", categoryName)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.isCategoryHidden("series", categoryName))
        assertFalse(viewModel.isCategoryHidden("live", categoryName))
        assertFalse(viewModel.isCategoryHidden("movies", categoryName))
    }

    @Test
    fun `resetVisibility should clear all hidden categories`() = runTest {
        // Given
        viewModel.toggleCategoryVisibility("live", "Sports")
        viewModel.toggleCategoryVisibility("movies", "Action")
        viewModel.toggleCategoryVisibility("series", "Drama")
        advanceUntilIdle()

        // When
        viewModel.resetVisibility()
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.isCategoryHidden("live", "Sports"))
        assertFalse(viewModel.isCategoryHidden("movies", "Action"))
        assertFalse(viewModel.isCategoryHidden("series", "Drama"))
    }

    @Test
    fun `categoryVisibility StateFlow should emit updated values`() = runTest {
        // Given
        val categoryName = "Horror"

        // When
        viewModel.toggleCategoryVisibility("live", categoryName)
        advanceUntilIdle()

        // Then
        val visibility = viewModel.categoryVisibility.first()
        assertTrue(visibility.hiddenCategoriesLiveTV.contains(categoryName))
    }
}
