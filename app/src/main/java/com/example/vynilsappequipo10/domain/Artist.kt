package com.example.vynilsappequipo10.domain

enum class ArtistType { MUSICIAN, BAND }

data class Artist(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val date: String? = null,
    val albums: List<Album> = emptyList(),
    val type: ArtistType = ArtistType.MUSICIAN
)
