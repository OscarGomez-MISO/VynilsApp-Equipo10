package com.example.vynilsappequipo10.ui.artists

import com.example.vynilsappequipo10.data.ArtistRepository
import com.example.vynilsappequipo10.domain.Artist
import com.example.vynilsappequipo10.domain.ArtistType
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
class ArtistsViewModelTest {

    private lateinit var viewModel: ArtistsViewModel
    private val repository: ArtistRepository = mockk()
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
    fun `loadArtists success updates uiState`() {
        // Arrange
        val artists = listOf(
            Artist(1, "Artist 1", "img1", "desc1", "date1", emptyList(), ArtistType.MUSICIAN),
            Artist(2, "Artist 2", "img2", "desc2", "date2", emptyList(), ArtistType.BAND)
        )
        coEvery { repository.getArtists() } returns artists

        // Act
        viewModel = ArtistsViewModel(repository)

        // Assert
        assertEquals(artists, viewModel.uiState.value.artists)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `loadArtists error updates uiState`() {
        // Arrange
        val errorMessage = "Error loading artists"
        coEvery { repository.getArtists() } throws Exception(errorMessage)

        // Act
        viewModel = ArtistsViewModel(repository)

        // Assert
        assertTrue(viewModel.uiState.value.artists.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(errorMessage, viewModel.uiState.value.error)
    }

    @Test
    fun `onSearchQueryChange filters artists`() {
        // Arrange
        val artists = listOf(
            Artist(1, "Rubén Blades", "img1", "desc1", "date1", emptyList(), ArtistType.MUSICIAN),
            Artist(2, "Queen", "img2", "desc2", "date2", emptyList(), ArtistType.BAND)
        )
        coEvery { repository.getArtists() } returns artists
        viewModel = ArtistsViewModel(repository)

        // Act
        viewModel.onSearchQueryChange("rubén")

        // Assert
        assertEquals(1, viewModel.uiState.value.artists.size)
        assertEquals("Rubén Blades", viewModel.uiState.value.artists[0].name)
    }
}
