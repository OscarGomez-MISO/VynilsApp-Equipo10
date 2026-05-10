package com.example.vynilsappequipo10.ui.artists

import com.example.vynilsappequipo10.data.ArtistRepository
import com.example.vynilsappequipo10.domain.Artist
import com.example.vynilsappequipo10.domain.ArtistType
import io.mockk.coEvery
import io.mockk.coVerify
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

    @Test
    fun `onSearchQueryChange with empty query shows all artists`() {
        // Arrange
        val artists = listOf(
            Artist(1, "Rubén Blades", "img1", "desc1", "date1", emptyList(), ArtistType.MUSICIAN),
            Artist(2, "Queen", "img2", "desc2", "date2", emptyList(), ArtistType.BAND)
        )
        coEvery { repository.getArtists() } returns artists
        viewModel = ArtistsViewModel(repository)
        viewModel.onSearchQueryChange("rubén") // Apply filter first

        // Act
        viewModel.onSearchQueryChange("")

        // Assert
        assertEquals(2, viewModel.uiState.value.artists.size)
        assertEquals("", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `loadArtists with exception without message uses default error`() {
        // Arrange
        coEvery { repository.getArtists() } throws Exception()

        // Act
        viewModel = ArtistsViewModel(repository)

        // Assert
        assertEquals("Error desconocido", viewModel.uiState.value.error)
    }

    @Test
    fun `loadArtists returns empty list`() {
        // Arrange
        coEvery { repository.getArtists() } returns emptyList()

        // Act
        viewModel = ArtistsViewModel(repository)

        // Assert
        assertTrue(viewModel.uiState.value.artists.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `onSearchQueryChange with no matching results returns empty list`() {
        // Arrange
        val artists = listOf(
            Artist(1, "Rubén Blades", "img1", "desc1", "date1", emptyList(), ArtistType.MUSICIAN),
            Artist(2, "Queen", "img2", "desc2", "date2", emptyList(), ArtistType.BAND)
        )
        coEvery { repository.getArtists() } returns artists
        viewModel = ArtistsViewModel(repository)

        // Act
        viewModel.onSearchQueryChange("nonexistent")

        // Assert
        assertTrue(viewModel.uiState.value.artists.isEmpty())
        assertEquals("nonexistent", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `loadArtists can be called manually to refresh`() {
        // Arrange
        val artists = listOf(
            Artist(1, "Artist 1", "img1", "desc1", "date1", emptyList(), ArtistType.MUSICIAN)
        )
        coEvery { repository.getArtists() } returns artists
        viewModel = ArtistsViewModel(repository)

        // Act
        viewModel.loadArtists()

        // Assert
        coVerify(exactly = 2) { repository.getArtists() } // Once in init, once in manual call
    }
}
