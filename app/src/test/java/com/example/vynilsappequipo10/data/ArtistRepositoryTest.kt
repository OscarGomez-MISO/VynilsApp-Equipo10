package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.BandService
import com.example.vynilsappequipo10.data.remote.MusicianService
import com.example.vynilsappequipo10.domain.ArtistType
import com.example.vynilsappequipo10.domain.Band
import com.example.vynilsappequipo10.domain.Musician
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ArtistRepositoryTest {

    private lateinit var repository: ArtistRepository
    private val musicianService: MusicianService = mockk()
    private val bandService: BandService = mockk()

    @Before
    fun setup() {
        repository = ArtistRepository(musicianService, bandService)
    }

    @Test
    fun `getArtists combines musicians and bands`() = runBlocking {
        // Given
        val musician = Musician(1, "Musician", "img", "desc", "1990", emptyList())
        val band = Band(2, "Band", "img", "desc", "2000", emptyList())
        coEvery { musicianService.getMusicians() } returns listOf(musician)
        coEvery { bandService.getBands() } returns listOf(band)

        // When
        val result = repository.getArtists()

        // Then
        assertEquals(2, result.size)
        assertEquals(ArtistType.MUSICIAN, result[0].type)
        assertEquals(ArtistType.BAND, result[1].type)
    }

    @Test
    fun `getArtistById returns musician when type is MUSICIAN`() = runBlocking {
        // Given
        val musician = Musician(1, "Musician", "img", "desc", "1990", emptyList())
        coEvery { musicianService.getMusicianById(1) } returns musician

        // When
        val result = repository.getArtistById(1, ArtistType.MUSICIAN)

        // Then
        assertEquals("Musician", result.name)
        assertEquals(ArtistType.MUSICIAN, result.type)
    }
}
