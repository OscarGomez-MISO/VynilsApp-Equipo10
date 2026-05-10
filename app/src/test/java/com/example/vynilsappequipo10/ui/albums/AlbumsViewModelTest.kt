package com.example.vynilsappequipo10.ui.albums

import com.example.vynilsappequipo10.data.AlbumRepository
import com.example.vynilsappequipo10.domain.Album
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
class AlbumsViewModelTest {

    private lateinit var viewModel: AlbumsViewModel
    private val repository: AlbumRepository = mockk()
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
    fun `loadAlbums success updates uiState`() {
        // Arrange
        val albums = listOf(
            Album(1, "Album 1", "cover1", "2023-01-01", "Desc 1", "Genre 1", "Label 1"),
            Album(2, "Album 2", "cover2", "2023-01-02", "Desc 2", "Genre 2", "Label 2")
        )
        coEvery { repository.getAlbums() } returns albums

        // Act
        viewModel = AlbumsViewModel(repository)

        // Assert
        assertEquals(albums, viewModel.uiState.value.albums)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `loadAlbums error updates uiState`() {
        // Arrange
        val errorMessage = "Network Error"
        coEvery { repository.getAlbums() } throws Exception(errorMessage)

        // Act
        viewModel = AlbumsViewModel(repository)

        // Assert
        assertTrue(viewModel.uiState.value.albums.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(errorMessage, viewModel.uiState.value.error)
    }

    @Test
    fun `onSearchQueryChange filters albums`() {
        // Arrange
        val albums = listOf(
            Album(1, "Salsa Party", "cover1", "2023-01-01", "Desc 1", "Salsa", "Label 1"),
            Album(2, "Rock Classics", "cover2", "2023-01-02", "Desc 2", "Rock", "Label 2")
        )
        coEvery { repository.getAlbums() } returns albums
        viewModel = AlbumsViewModel(repository)

        // Act
        viewModel.onSearchQueryChange("salsa")

        // Assert
        assertEquals(1, viewModel.uiState.value.albums.size)
        assertEquals("Salsa Party", viewModel.uiState.value.albums[0].name)
        assertEquals("salsa", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `onSearchQueryChange with empty query shows all albums`() {
        // Arrange
        val albums = listOf(
            Album(1, "Salsa Party", "cover1", "2023-01-01", "Desc 1", "Salsa", "Label 1"),
            Album(2, "Rock Classics", "cover2", "2023-01-02", "Desc 2", "Rock", "Label 2")
        )
        coEvery { repository.getAlbums() } returns albums
        viewModel = AlbumsViewModel(repository)
        viewModel.onSearchQueryChange("salsa") // Apply filter first

        // Act
        viewModel.onSearchQueryChange("")

        // Assert
        assertEquals(2, viewModel.uiState.value.albums.size)
        assertEquals("", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `onSearchQueryChange filters by genre`() {
        // Arrange
        val albums = listOf(
            Album(1, "Album One", "cover1", "2023-01-01", "Desc 1", "Salsa", "Label 1"),
            Album(2, "Album Two", "cover2", "2023-01-02", "Desc 2", "Rock", "Label 2"),
            Album(3, "Album Three", "cover3", "2023-01-03", "Desc 3", "Salsa", "Label 3")
        )
        coEvery { repository.getAlbums() } returns albums
        viewModel = AlbumsViewModel(repository)

        // Act
        viewModel.onSearchQueryChange("Rock")

        // Assert
        assertEquals(1, viewModel.uiState.value.albums.size)
        assertEquals("Rock", viewModel.uiState.value.albums[0].genre)
    }

    @Test
    fun `refreshAfterCreate reloads albums`() {
        // Arrange
        val albums = listOf(
            Album(1, "Album 1", "cover1", "2023-01-01", "Desc 1", "Genre 1", "Label 1")
        )
        coEvery { repository.getAlbums() } returns albums
        viewModel = AlbumsViewModel(repository)

        // Act
        viewModel.refreshAfterCreate()

        // Assert
        coVerify(exactly = 2) { repository.getAlbums() } // Once in init, once in refresh
    }

    @Test
    fun `loadAlbums with exception without message uses default error`() {
        // Arrange
        coEvery { repository.getAlbums() } throws Exception()

        // Act
        viewModel = AlbumsViewModel(repository)

        // Assert
        assertEquals("Error desconocido", viewModel.uiState.value.error)
    }

    @Test
    fun `onSearchQueryChange preserves filter after reload`() {
        // Arrange
        val albums = listOf(
            Album(1, "Salsa Party", "cover1", "2023-01-01", "Desc 1", "Salsa", "Label 1"),
            Album(2, "Rock Classics", "cover2", "2023-01-02", "Desc 2", "Rock", "Label 2")
        )
        coEvery { repository.getAlbums() } returns albums
        viewModel = AlbumsViewModel(repository)
        viewModel.onSearchQueryChange("salsa")

        // Act - reload with same filter active
        viewModel.loadAlbums()

        // Assert - filter is preserved
        assertEquals("salsa", viewModel.uiState.value.searchQuery)
        assertEquals(1, viewModel.uiState.value.albums.size)
    }
}
