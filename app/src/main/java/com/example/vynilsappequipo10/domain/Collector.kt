package com.example.vynilsappequipo10.domain

data class CollectorAlbum(
    val id: Int,
    val price: Double,
    val status: String
)

data class Collector(
    val id: Int? = null,
    val name: String,
    val telephone: String,
    val email: String,
    val collectorAlbums: List<CollectorAlbum> = emptyList()
)

data class CollectorRequest(
    val name: String,
    val telephone: String,
    val email: String
)

data class CommentRequest(
    val description: String,
    val rating: Int,
    val collector: CollectorIdRequest
)

data class CollectorIdRequest(
    val id: Int
)
