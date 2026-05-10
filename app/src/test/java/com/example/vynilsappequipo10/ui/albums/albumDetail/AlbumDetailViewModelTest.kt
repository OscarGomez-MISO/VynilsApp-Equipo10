package com.example.vynilsappequipo10.ui.albums.albumDetail

import com.example.vynilsappequipo10.data.AlbumRepository
import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.domain.Comment
import com.example.vynilsappequipo10.domain.Performer
import com.example.vynilsappequipo10.domain.Track
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumDetailViewModelTest {

    private lateinit var viewModel: AlbumDetailViewModel
    private val repository: AlbumRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AlbumDetailViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAlbum success updates uiState with album`() = runTest {
        // Given
        val albumId = 1
        val expectedAlbum = Album(
            id = albumId,
            name = "Test Album",
            cover = "cover.jpg",
            releaseDate = "2021-01-01",
            description = "Description",
            genre = "Rock",
            recordLabel = "Sony",
            tracks = emptyList(),
            performers = emptyList(),
            comments = emptyList()
        )
        coEvery { repository.getAlbumById(albumId) } returns expectedAlbum

        // When
        viewModel.loadAlbum(albumId)

        // Then
        assertEquals(expectedAlbum, viewModel.uiState.value.album)
        assertEquals(false, viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `loadAlbum error updates uiState with error message`() = runTest {
        // Given
        val albumId = 1
        val errorMessage = "Network Error"
        coEvery { repository.getAlbumById(albumId) } throws Exception(errorMessage)

        // When
        viewModel.loadAlbum(albumId)

        // Then
        assertEquals(null, viewModel.uiState.value.album)
        assertEquals(false, viewModel.uiState.value.isLoading)
        assertEquals(errorMessage, viewModel.uiState.value.error)
    }

    @Test
    fun `loadAlbum with full album data including tracks performers and comments`() = runTest {
        // Given
        val albumId = 1
        val tracks = listOf(Track(1, "Track 1", "3:30"), Track(2, "Track 2", "4:15"))
        val performers = listOf(Performer(1, "Artist", "img", "desc", "1990"))
        val comments = listOf(Comment(1, "Great!", 5), Comment(2, "Nice", 4))
        
        val expectedAlbum = Album(
            id = albumId,
            name = "Full Album",
            cover = "cover.jpg",
            releaseDate = "2021-01-01",
            description = "Description",
            genre = "Rock",
            recordLabel = "Sony",
            tracks = tracks,
            performers = performers,
            comments = comments
        )
        coEvery { repository.getAlbumById(albumId) } returns expectedAlbum

        // When
        viewModel.loadAlbum(albumId)

        // Then
        assertEquals(2, viewModel.uiState.value.album?.tracks?.size)
        assertEquals(1, viewModel.uiState.value.album?.performers?.size)
        assertEquals(2, viewModel.uiState.value.album?.comments?.size)
    }

    @Test
    fun `loadAlbum with exception without message uses default error`() = runTest {
        // Given
        val albumId = 1
        coEvery { repository.getAlbumById(albumId) } throws Exception()

        // When
        viewModel.loadAlbum(albumId)

        // Then
        assertEquals("Error desconocido", viewModel.uiState.value.error)
    }

    @Test
    fun `initial state has null album and not loading`() {
        // Assert
        assertNull(viewModel.uiState.value.album)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }
}
