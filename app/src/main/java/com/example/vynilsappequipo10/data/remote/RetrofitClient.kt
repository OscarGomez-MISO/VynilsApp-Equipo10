package com.example.vynilsappequipo10.data.remote

import com.example.vynilsappequipo10.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val albumService: AlbumService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlbumService::class.java)
    }
}
