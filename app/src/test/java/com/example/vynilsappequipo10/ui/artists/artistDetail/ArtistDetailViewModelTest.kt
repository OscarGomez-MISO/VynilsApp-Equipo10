package com.example.vynilsappequipo10.ui.artists.artistDetail

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
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistDetailViewModelTest {

    private lateinit var viewModel: ArtistDetailViewModel
    private val repository: ArtistRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ArtistDetailViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadArtist success updates uiState`() {
        // Arrange
        val artistId = 1
        val artistType = ArtistType.MUSICIAN
        val artist = Artist(artistId, "Artist 1", "img1", "desc1", "date1", emptyList(), artistType)
        coEvery { repository.getArtistById(artistId, artistType) } returns artist

        // Act
        viewModel.loadArtist(artistId, artistType)

        // Assert
        assertEquals(artist, viewModel.uiState.value.artist)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `loadArtist error updates uiState`() {
        // Arrange
        val artistId = 1
        val artistType = ArtistType.MUSICIAN
        val errorMessage = "Not Found"
        coEvery { repository.getArtistById(artistId, artistType) } throws Exception(errorMessage)

        // Act
        viewModel.loadArtist(artistId, artistType)

        // Assert
        assertEquals(null, viewModel.uiState.value.artist)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(errorMessage, viewModel.uiState.value.error)
    }

    @Test
    fun `loadArtist with BAND type success`() {
        // Arrange
        val artistId = 2
        val artistType = ArtistType.BAND
        val band = Artist(artistId, "Queen", "img", "Rock band", "1970", emptyList(), artistType)
        coEvery { repository.getArtistById(artistId, artistType) } returns band

        // Act
        viewModel.loadArtist(artistId, artistType)

        // Assert
        assertEquals(band, viewModel.uiState.value.artist)
        assertEquals(ArtistType.BAND, viewModel.uiState.value.artist?.type)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `loadArtist with exception without message uses default error`() {
        // Arrange
        val artistId = 1
        val artistType = ArtistType.MUSICIAN
        coEvery { repository.getArtistById(artistId, artistType) } throws Exception()

        // Act
        viewModel.loadArtist(artistId, artistType)

        // Assert
        assertEquals("Error desconocido", viewModel.uiState.value.error)
    }

    @Test
    fun `initial state has null artist and not loading`() {
        // Assert
        assertNull(viewModel.uiState.value.artist)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }
}
