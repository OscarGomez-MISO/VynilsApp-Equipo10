package com.example.vynilsappequipo10.ui.albums.albumDetail

import com.example.vynilsappequipo10.data.AlbumRepository
import com.example.vynilsappequipo10.domain.Album
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
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
}
