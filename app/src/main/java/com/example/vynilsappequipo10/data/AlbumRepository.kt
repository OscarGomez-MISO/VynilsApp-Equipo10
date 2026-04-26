package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.data.remote.AlbumService
import com.example.vynilsappequipo10.data.remote.RetrofitClient

class AlbumRepository(
    private val service: AlbumService = RetrofitClient.albumService
) {
    suspend fun getAlbums(): List<Album> = service.getAlbums()
}
