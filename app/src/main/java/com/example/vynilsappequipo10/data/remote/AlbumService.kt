package com.example.vynilsappequipo10.data.remote

import com.example.vynilsappequipo10.domain.Album
import retrofit2.http.GET

interface AlbumService {
    @GET("albums")
    suspend fun getAlbums(): List<Album>

    @GET("albums/{id}")
    suspend fun getAlbumById(@retrofit2.http.Path("id") id: Int): Album
}
