package com.example.vynilsappequipo10.data.remote

import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.domain.Comment
import com.example.vynilsappequipo10.domain.CommentRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AlbumService {
    @GET("albums")
    suspend fun getAlbums(): List<Album>

    @GET("albums/{id}")
    suspend fun getAlbumById(@Path("id") id: Int): Album

    @POST("albums/{id}/comments")
    suspend fun addComment(
        @Path("id") albumId: Int,
        @Body comment: CommentRequest
    ): Comment
}
