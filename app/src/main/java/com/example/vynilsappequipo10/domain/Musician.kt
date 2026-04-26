package com.example.vynilsappequipo10.domain

import com.google.gson.annotations.SerializedName

data class Musician(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    @SerializedName("birthDate") val birthDate: String? = null,
    val albums: List<Album> = emptyList()
)
