package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.CollectorService
import com.example.vynilsappequipo10.domain.Collector
import com.example.vynilsappequipo10.domain.CollectorDetail
import com.example.vynilsappequipo10.domain.CollectorRequest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
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
}
