package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.AlbumService
import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.domain.AlbumRequest
import com.example.vynilsappequipo10.domain.Comment
import com.example.vynilsappequipo10.domain.CommentRequest
import com.example.vynilsappequipo10.domain.CollectorIdRequest
import io.mockk.coEvery
import io.mockk.coVerify
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

    @Test
    fun `createAlbum returns created album`() = runBlocking {
        // Given
        val request = AlbumRequest("New Album", "cover", "2024", "Desc", "Rock", "Sony")
        val expectedAlbum = Album(10, "New Album", "cover", "2024", "Desc", "Rock", "Sony")
        coEvery { service.createAlbum(request) } returns expectedAlbum

        // When
        val result = repository.createAlbum(request)

        // Then
        assertEquals(expectedAlbum, result)
        assertEquals("New Album", result.name)
    }

    @Test
    fun `addComment calls service with correct parameters`() = runBlocking {
        // Given
        val albumId = 1
        val comment = CommentRequest("Great album!", 5, CollectorIdRequest(100))
        val expectedComment = Comment(1, "Great album!", 5)
        coEvery { service.addComment(albumId, comment) } returns expectedComment

        // When
        repository.addComment(albumId, comment)

        // Then
        coVerify { service.addComment(albumId, comment) }
    }

    @Test
    fun `getAlbums returns empty list when no albums`() = runBlocking {
        // Given
        coEvery { service.getAlbums() } returns emptyList()

        // When
        val result = repository.getAlbums()

        // Then
        assertEquals(emptyList<Album>(), result)
    }
}
