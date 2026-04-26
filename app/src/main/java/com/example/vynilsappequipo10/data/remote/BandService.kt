package com.example.vynilsappequipo10.data.remote

import com.example.vynilsappequipo10.domain.Band
import retrofit2.http.GET
import retrofit2.http.Path

interface BandService {
    @GET("bands")
    suspend fun getBands(): List<Band>

    @GET("bands/{id}")
    suspend fun getBandById(@Path("id") id: Int): Band
}
