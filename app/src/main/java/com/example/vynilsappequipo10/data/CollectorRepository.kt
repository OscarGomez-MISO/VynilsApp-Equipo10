package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.CollectorService
import com.example.vynilsappequipo10.data.remote.RetrofitClient
import com.example.vynilsappequipo10.domain.Collector
import com.example.vynilsappequipo10.domain.CollectorAlbumWithAlbum
import com.example.vynilsappequipo10.domain.CollectorDetail
import com.example.vynilsappequipo10.domain.CollectorRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class CollectorRepository(
    private val service: CollectorService = RetrofitClient.collectorService
) {
    open suspend fun getCollectors(): List<Collector> = withContext(Dispatchers.IO) {
        service.getCollectors()
    }

    open suspend fun getCollectorById(id: Int): CollectorDetail = withContext(Dispatchers.IO) {
        service.getCollectorById(id)
    }

    open suspend fun getCollectorAlbums(id: Int): List<CollectorAlbumWithAlbum> = withContext(Dispatchers.IO) {
        service.getCollectorAlbums(id)
    }

    open suspend fun createCollector(collector: CollectorRequest): Collector = withContext(Dispatchers.IO) {
        service.createCollector(collector)
    }
}
