package com.example.vynilsappequipo10.domain

import com.google.gson.annotations.SerializedName

data class Band(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    @SerializedName("creationDate") val creationDate: String? = null,
    val albums: List<Album> = emptyList()
)
