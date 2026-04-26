package com.example.vynilsappequipo10.data.remote

import com.example.vynilsappequipo10.domain.Musician
import retrofit2.http.GET
import retrofit2.http.Path

interface MusicianService {
    @GET("musicians")
    suspend fun getMusicians(): List<Musician>

    @GET("musicians/{id}")
    suspend fun getMusicianById(@Path("id") id: Int): Musician
}
