package com.example.vynilsappequipo10.data.remote

import com.example.vynilsappequipo10.domain.Collector
import com.example.vynilsappequipo10.domain.CollectorRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CollectorService {
    @GET("collectors")
    suspend fun getCollectors(): List<Collector>

    @POST("collectors")
    suspend fun createCollector(@Body collector: CollectorRequest): Collector
}
