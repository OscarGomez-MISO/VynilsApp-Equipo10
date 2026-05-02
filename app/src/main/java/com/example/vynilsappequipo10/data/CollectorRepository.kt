package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.CollectorService
import com.example.vynilsappequipo10.data.remote.RetrofitClient
import com.example.vynilsappequipo10.domain.Collector

class CollectorRepository(
    private val service: CollectorService = RetrofitClient.collectorService
) {
    suspend fun getCollectors(): List<Collector> = service.getCollectors()
    suspend fun createCollector(collector: Collector): Collector = service.createCollector(collector)
}
