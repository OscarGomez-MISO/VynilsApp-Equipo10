package com.example.vynilsappequipo10.data.remote

import com.example.vynilsappequipo10.domain.Collector
import com.example.vynilsappequipo10.domain.CollectorAlbumWithAlbum
import com.example.vynilsappequipo10.domain.CollectorDetail
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CollectorService {
    @GET("collectors")
    suspend fun getCollectors(): List<Collector>

    @GET("collectors/{id}")
    suspend fun getCollectorById(@Path("id") id: Int): CollectorDetail

    @GET("collectors/{id}/albums")
    suspend fun getCollectorAlbums(@Path("id") id: Int): List<CollectorAlbumWithAlbum>

    @POST("collectors")
    suspend fun createCollector(@Body collector: Collector): Collector
}
