package com.example.vynilsappequipo10

import com.example.vynilsappequipo10.data.ArtistRepository
import com.example.vynilsappequipo10.data.remote.BandService
import com.example.vynilsappequipo10.data.remote.MusicianService
import com.example.vynilsappequipo10.domain.Band
import com.example.vynilsappequipo10.domain.Musician
import com.example.vynilsappequipo10.domain.ArtistType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ArtistRepositoryTest {

    private val musicianService: MusicianService = mockk()
    private val bandService: BandService = mockk()
    private val repository = ArtistRepository(musicianService, bandService)

    @Test
    fun getArtists_returnsCombinedList() = runBlocking {
        val musicians = listOf(Musician(1, "M1", "img1", "desc1", "1990-01-01"))
        val bands = listOf(Band(2, "B1", "img2", "desc2", "2000-01-01"))
        coEvery { musicianService.getMusicians() } returns musicians
        coEvery { bandService.getBands() } returns bands

        val artists = repository.getArtists()
        assertEquals(2, artists.size)
        assertEquals("M1", artists[0].name)
        assertEquals("B1", artists[1].name)
    }
}
