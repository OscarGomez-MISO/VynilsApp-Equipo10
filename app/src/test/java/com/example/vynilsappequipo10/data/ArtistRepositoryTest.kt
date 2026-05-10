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
import org.junit.Assert.assertTrue
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

    @Test
    fun `getArtistById returns band when type is BAND`() = runBlocking {
        // Given
        val band = Band(2, "Queen", "img", "Rock band", "1970", emptyList())
        coEvery { bandService.getBandById(2) } returns band

        // When
        val result = repository.getArtistById(2, ArtistType.BAND)

        // Then
        assertEquals("Queen", result.name)
        assertEquals(ArtistType.BAND, result.type)
        assertEquals("1970", result.date)
    }

    @Test
    fun `getArtists returns empty list when no artists exist`() = runBlocking {
        // Given
        coEvery { musicianService.getMusicians() } returns emptyList()
        coEvery { bandService.getBands() } returns emptyList()

        // When
        val result = repository.getArtists()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getArtists returns only musicians when no bands exist`() = runBlocking {
        // Given
        val musician = Musician(1, "Solo Artist", "img", "desc", "1985", emptyList())
        coEvery { musicianService.getMusicians() } returns listOf(musician)
        coEvery { bandService.getBands() } returns emptyList()

        // When
        val result = repository.getArtists()

        // Then
        assertEquals(1, result.size)
        assertEquals(ArtistType.MUSICIAN, result[0].type)
    }

    @Test
    fun `getArtists returns only bands when no musicians exist`() = runBlocking {
        // Given
        val band = Band(1, "The Beatles", "img", "desc", "1960", emptyList())
        coEvery { musicianService.getMusicians() } returns emptyList()
        coEvery { bandService.getBands() } returns listOf(band)

        // When
        val result = repository.getArtists()

        // Then
        assertEquals(1, result.size)
        assertEquals(ArtistType.BAND, result[0].type)
    }
}
