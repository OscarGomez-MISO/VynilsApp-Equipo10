package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.CollectorService
import com.example.vynilsappequipo10.data.remote.RetrofitClient
import com.example.vynilsappequipo10.domain.Collector
import com.example.vynilsappequipo10.domain.CollectorAlbumWithAlbum
import com.example.vynilsappequipo10.domain.CollectorDetail

open class CollectorRepository(
    private val service: CollectorService = RetrofitClient.collectorService
) {
    open suspend fun getCollectors(): List<Collector> = service.getCollectors()
    open suspend fun getCollectorById(id: Int): CollectorDetail = service.getCollectorById(id)
    open suspend fun getCollectorAlbums(id: Int): List<CollectorAlbumWithAlbum> = service.getCollectorAlbums(id)
    suspend fun createCollector(collector: Collector): Collector = service.createCollector(collector)
}
