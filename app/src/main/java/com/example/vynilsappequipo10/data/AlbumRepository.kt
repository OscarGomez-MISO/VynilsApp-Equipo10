package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.data.remote.AlbumService
import com.example.vynilsappequipo10.data.remote.RetrofitClient

open class AlbumRepository(
    private val service: AlbumService = RetrofitClient.albumService
) {
    open suspend fun getAlbums(): List<Album> = service.getAlbums()

    open suspend fun getAlbumById(id: Int): Album = service.getAlbumById(id)

    suspend fun addComment(albumId: Int, comment: com.example.vynilsappequipo10.domain.CommentRequest) = 
        service.addComment(albumId, comment)
}
