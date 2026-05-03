package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.CollectorService
import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.domain.Collector
import com.example.vynilsappequipo10.domain.CollectorAlbumWithAlbum
import com.example.vynilsappequipo10.domain.CollectorDetail
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CollectorRepositoryTest {

    private val service: CollectorService = mockk()
    private val repository = CollectorRepository(service)

    private val fakeAlbum = Album(
        id = 1, name = "Buscando América", cover = "", releaseDate = "1984-08-01",
        description = "", genre = "Salsa", recordLabel = "Elektra"
    )

    @Test
    fun `getCollectors returns list from service`() = runBlocking {
        val collectors = listOf(
            Collector(1, "Marcus Thorne", "1234567890", "marcus@email.com"),
            Collector(2, "Elena Rodriguez", "0987654321", "elena@email.com")
        )
        coEvery { service.getCollectors() } returns collectors

        val result = repository.getCollectors()

        assertEquals(2, result.size)
        assertEquals("Marcus Thorne", result[0].name)
        assertEquals("Elena Rodriguez", result[1].name)
    }

    @Test
    fun `getCollectorById returns collector detail from service`() = runBlocking {
        val detail = CollectorDetail(
            id = 1, name = "Manolo Bellon",
            telephone = "3502457896", email = "manollo@caracol.com.co"
        )
        coEvery { service.getCollectorById(1) } returns detail

        val result = repository.getCollectorById(1)

        assertEquals(1, result.id)
        assertEquals("Manolo Bellon", result.name)
        assertEquals("manollo@caracol.com.co", result.email)
    }

    @Test
    fun `getCollectorAlbums returns albums list from service`() = runBlocking {
        val albums = listOf(
            CollectorAlbumWithAlbum(id = 1, price = 35.0, status = "Active", album = fakeAlbum)
        )
        coEvery { service.getCollectorAlbums(1) } returns albums

        val result = repository.getCollectorAlbums(1)

        assertEquals(1, result.size)
        assertEquals("Buscando América", result[0].album.name)
        assertEquals("Active", result[0].status)
    }

    @Test
    fun `getCollectorAlbums returns empty list when collector has no albums`() = runBlocking {
        coEvery { service.getCollectorAlbums(99) } returns emptyList()

        val result = repository.getCollectorAlbums(99)

        assertEquals(0, result.size)
    }
}
