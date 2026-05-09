package com.example.vynilsappequipo10.data.remote

import com.example.vynilsappequipo10.BuildConfig
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val NETWORK_TIMEOUT = 30L

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(3, 5, TimeUnit.MINUTES))
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val albumService: AlbumService by lazy {
        retrofit.create(AlbumService::class.java)
    }

    val collectorService: CollectorService by lazy {
        retrofit.create(CollectorService::class.java)
    }

    val musicianService: MusicianService by lazy {
        retrofit.create(MusicianService::class.java)
    }

    val bandService: BandService by lazy {
        retrofit.create(BandService::class.java)
    }
}
