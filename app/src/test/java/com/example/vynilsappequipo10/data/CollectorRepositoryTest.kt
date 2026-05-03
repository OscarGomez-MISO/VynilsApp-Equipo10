package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.CollectorService
import com.example.vynilsappequipo10.domain.Collector
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CollectorRepositoryTest {

    private val service: CollectorService = mockk()
    private val repository = CollectorRepository(service)

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
}
