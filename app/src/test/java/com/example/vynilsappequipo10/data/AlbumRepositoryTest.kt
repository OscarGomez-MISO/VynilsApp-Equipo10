package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.AlbumService
import com.example.vynilsappequipo10.domain.Album
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AlbumRepositoryTest {

    private lateinit var repository: AlbumRepository
    private val service: AlbumService = mockk()

    @Before
    fun setup() {
        repository = AlbumRepository(service)
    }

    @Test
    fun `getAlbums returns list of albums`() = runBlocking {
        // Given
        val expectedAlbums = listOf(
            Album(1, "Album 1", "cover1", "2021", "Desc", "Rock", "EMI"),
            Album(2, "Album 2", "cover2", "2022", "Desc", "Pop", "Sony")
        )
        coEvery { service.getAlbums() } returns expectedAlbums

        // When
        val result = repository.getAlbums()

        // Then
        assertEquals(expectedAlbums, result)
    }

    @Test
    fun `getAlbumById returns specific album`() = runBlocking {
        // Given
        val albumId = 1
        val expectedAlbum = Album(albumId, "Album 1", "cover1", "2021", "Desc", "Rock", "EMI")
        coEvery { service.getAlbumById(albumId) } returns expectedAlbum

        // When
        val result = repository.getAlbumById(albumId)

        // Then
        assertEquals(expectedAlbum, result)
    }
}
