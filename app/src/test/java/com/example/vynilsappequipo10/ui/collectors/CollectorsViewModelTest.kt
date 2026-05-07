package com.example.vynilsappequipo10.ui.collectors

import com.example.vynilsappequipo10.data.CollectorRepository
import com.example.vynilsappequipo10.domain.Collector
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CollectorsViewModelTest {

    private lateinit var viewModel: CollectorsViewModel
    private val repository: CollectorRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCollectors success updates uiState`() {
        val collectors = listOf(
            Collector(1, "Marcus Thorne", "1234567890", "marcus@email.com"),
            Collector(2, "Elena Rodriguez", "0987654321", "elena@email.com")
        )
        coEvery { repository.getCollectors() } returns collectors

        viewModel = CollectorsViewModel(repository)

        assertEquals(collectors, viewModel.uiState.value.collectors)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `loadCollectors error updates uiState`() {
        val errorMessage = "Error loading collectors"
        coEvery { repository.getCollectors() } throws Exception(errorMessage)

        viewModel = CollectorsViewModel(repository)

        assertTrue(viewModel.uiState.value.collectors.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(errorMessage, viewModel.uiState.value.error)
    }

    @Test
    fun `onSearchQueryChange filters collectors by name`() {
        val collectors = listOf(
            Collector(1, "Marcus Thorne", "1234567890", "marcus@email.com"),
            Collector(2, "Elena Rodriguez", "0987654321", "elena@email.com")
        )
        coEvery { repository.getCollectors() } returns collectors
        viewModel = CollectorsViewModel(repository)

        viewModel.onSearchQueryChange("marcus")

        assertEquals(1, viewModel.uiState.value.collectors.size)
        assertEquals("Marcus Thorne", viewModel.uiState.value.collectors[0].name)
    }

    @Test
    fun `loadCollectors empty list uiState has empty collectors`() {
        coEvery { repository.getCollectors() } returns emptyList()

        viewModel = CollectorsViewModel(repository)

        assertTrue(viewModel.uiState.value.collectors.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.error)
    }
}
