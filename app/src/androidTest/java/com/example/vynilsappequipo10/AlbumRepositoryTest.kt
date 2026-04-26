package com.example.vynilsappequipo10

import com.example.vynilsappequipo10.data.AlbumRepository
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
    fun getAlbums_returnsListFromService() = runBlocking {
        val expectedAlbums = listOf(
            Album(1, "Album 1", "cover1", "2023-01-01", "Desc 1", "Genre 1", "Label 1")
        )
        coEvery { service.getAlbums() } returns expectedAlbums
        val actualAlbums = repository.getAlbums()
        assertEquals(expectedAlbums, actualAlbums)
    }
}
