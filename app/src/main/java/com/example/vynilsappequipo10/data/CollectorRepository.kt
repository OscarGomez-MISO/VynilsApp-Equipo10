package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.CollectorService
import com.example.vynilsappequipo10.data.remote.RetrofitClient
import com.example.vynilsappequipo10.domain.Collector

open class CollectorRepository(
    private val service: CollectorService = RetrofitClient.collectorService
) {
    open suspend fun getCollectors(): List<Collector> = service.getCollectors()
    suspend fun createCollector(collector: Collector): Collector = service.createCollector(collector)
}
