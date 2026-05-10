package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.CollectorService
import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.domain.Collector
import com.example.vynilsappequipo10.domain.CollectorAlbumWithAlbum
import com.example.vynilsappequipo10.domain.CollectorDetail
import com.example.vynilsappequipo10.domain.CollectorRequest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CollectorRepositoryTest {

    private lateinit var repository: CollectorRepository
    private val service: CollectorService = mockk()

    @Before
    fun setup() {
        repository = CollectorRepository(service)
    }

    @Test
    fun `getCollectors returns list`() = runBlocking {
        val expected = listOf(Collector(1, "Name", "123", "a@a.com"))
        coEvery { service.getCollectors() } returns expected
        val result = repository.getCollectors()
        assertEquals(expected, result)
    }

    @Test
    fun `getCollectorById returns detail`() = runBlocking {
        val expected = CollectorDetail(1, "Name", "123", "a@a.com")
        coEvery { service.getCollectorById(1) } returns expected
        val result = repository.getCollectorById(1)
        assertEquals(expected, result)
    }

    @Test
    fun `createCollector returns new collector`() = runBlocking {
        val request = CollectorRequest("New", "555", "new@a.com")
        val expected = Collector(10, "New", "555", "new@a.com")
        coEvery { service.createCollector(request) } returns expected
        val result = repository.createCollector(request)
        assertEquals(expected, result)
    }

    @Test
    fun `getCollectorAlbums returns list of albums with details`() = runBlocking {
        // Given
        val album = Album(1, "Test Album", "cover", "2024", "Desc", "Rock", "Sony")
        val expected = listOf(
            CollectorAlbumWithAlbum(1, 25.0, "Active", album),
            CollectorAlbumWithAlbum(2, 30.0, "Inactive", album)
        )
        coEvery { service.getCollectorAlbums(1) } returns expected

        // When
        val result = repository.getCollectorAlbums(1)

        // Then
        assertEquals(2, result.size)
        assertEquals(25.0, result[0].price, 0.01)
        assertEquals("Active", result[0].status)
    }

    @Test
    fun `getCollectorAlbums returns empty list when no albums`() = runBlocking {
        // Given
        coEvery { service.getCollectorAlbums(1) } returns emptyList()

        // When
        val result = repository.getCollectorAlbums(1)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getCollectors returns empty list when no collectors`() = runBlocking {
        // Given
        coEvery { service.getCollectors() } returns emptyList()

        // When
        val result = repository.getCollectors()

        // Then
        assertTrue(result.isEmpty())
    }
}
