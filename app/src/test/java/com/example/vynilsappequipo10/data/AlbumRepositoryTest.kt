package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.AlbumService
import com.example.vynilsappequipo10.domain.Album
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumRepositoryTest {

    private val service: AlbumService = mockk()
    private val repository = AlbumRepository(service)

    @Test
    fun `getAlbums returns list from service`() = runBlocking {
        // Arrange
        val expectedAlbums = listOf(
            Album(1, "Album 1", "cover1", "2023-01-01", "Desc 1", "Genre 1", "Label 1"),
            Album(2, "Album 2", "cover2", "2023-01-02", "Desc 2", "Genre 2", "Label 2")
        )
        coEvery { service.getAlbums() } returns expectedAlbums

        // Act
        val actualAlbums = repository.getAlbums()

        // Assert
        assertEquals(expectedAlbums, actualAlbums)
    }

    @Test
    fun `getAlbumById returns album from service`() = runBlocking {
        // Arrange
        val expectedAlbum = Album(1, "Album 1", "cover1", "2023-01-01", "Desc 1", "Genre 1", "Label 1")
        coEvery { service.getAlbumById(1) } returns expectedAlbum

        // Act
        val actualAlbum = repository.getAlbumById(1)

        // Assert
        assertEquals(expectedAlbum, actualAlbum)
    }
}
