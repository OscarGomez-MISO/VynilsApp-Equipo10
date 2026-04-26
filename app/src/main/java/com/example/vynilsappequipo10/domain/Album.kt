package com.example.vynilsappequipo10.domain

import com.google.gson.annotations.SerializedName

data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    @SerializedName("releaseDate") val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
    val tracks: List<Track> = emptyList(),
    val performers: List<Performer> = emptyList(),
    val comments: List<Comment> = emptyList()
)

data class Track(
    val id: Int,
    val name: String,
    val duration: String
)

data class Performer(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String? = null
)

data class Comment(
    val id: Int,
    val description: String,
    val rating: Int
)
