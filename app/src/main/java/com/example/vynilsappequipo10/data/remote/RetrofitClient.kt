package com.example.vynilsappequipo10.data.remote

import com.example.vynilsappequipo10.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val albumService: AlbumService by lazy {
        retrofit.create(AlbumService::class.java)
    }

    val musicianService: MusicianService by lazy {
        retrofit.create(MusicianService::class.java)
    }

    val bandService: BandService by lazy {
        retrofit.create(BandService::class.java)
    }
}
