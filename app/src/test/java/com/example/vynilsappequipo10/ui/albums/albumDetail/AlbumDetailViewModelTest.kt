package com.example.vynilsappequipo10.ui.albums.albumDetail

import com.example.vynilsappequipo10.data.AlbumRepository
import com.example.vynilsappequipo10.domain.Album
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
    fun `loadAlbum success updates uiState`() {
        // Arrange
        val albumId = 1
        val album = Album(albumId, "Album 1", "cover1", "2023-01-01", "Desc 1", "Genre 1", "Label 1")
        coEvery { repository.getAlbumById(albumId) } returns album

        // Act
        viewModel.loadAlbum(albumId)

        // Assert
        assertEquals(album, viewModel.uiState.value.album)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `loadAlbum error updates uiState`() {
        // Arrange
        val albumId = 1
        val errorMessage = "Not Found"
        coEvery { repository.getAlbumById(albumId) } throws Exception(errorMessage)

        // Act
        viewModel.loadAlbum(albumId)

        // Assert
        assertEquals(null, viewModel.uiState.value.album)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(errorMessage, viewModel.uiState.value.error)
    }
}
