package com.example.vynilsappequipo10.domain

import com.google.gson.annotations.SerializedName

data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    @SerializedName("releaseDate") val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String
)
