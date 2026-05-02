package com.example.vynilsappequipo10.domain

data class Collector(
    val id: Int? = null,
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
