package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.domain.AlbumRequest
import com.example.vynilsappequipo10.data.remote.AlbumService
import com.example.vynilsappequipo10.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class AlbumRepository(
    private val service: AlbumService = RetrofitClient.albumService
) {
    open suspend fun getAlbums(): List<Album> = withContext(Dispatchers.IO) {
        service.getAlbums()
    }

    open suspend fun getAlbumById(id: Int): Album = withContext(Dispatchers.IO) {
        service.getAlbumById(id)
    }

    suspend fun createAlbum(album: AlbumRequest): Album = withContext(Dispatchers.IO) {
        service.createAlbum(album)
    }

    suspend fun addComment(albumId: Int, comment: com.example.vynilsappequipo10.domain.CommentRequest) = 
        withContext(Dispatchers.IO) {
            service.addComment(albumId, comment)
        }
}
