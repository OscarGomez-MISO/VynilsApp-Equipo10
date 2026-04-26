package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.MusicianService
import com.example.vynilsappequipo10.data.remote.RetrofitClient
import com.example.vynilsappequipo10.domain.Musician

class MusicianRepository(
    private val service: MusicianService = RetrofitClient.musicianService
) {
    suspend fun getMusicians(): List<Musician> = service.getMusicians()

    suspend fun getMusicianById(id: Int): Musician = service.getMusicianById(id)
}
