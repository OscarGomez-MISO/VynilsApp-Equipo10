package com.example.vynilsappequipo10.data

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
    fun `getArtists returns combined list of musicians and bands`() = runBlocking {
        // Arrange
        val musicians = listOf(
            Musician(1, "Musician 1", "img1", "desc1", "1990-01-01")
        )
        val bands = listOf(
            Band(2, "Band 1", "img2", "desc2", "2000-01-01")
        )
        coEvery { musicianService.getMusicians() } returns musicians
        coEvery { bandService.getBands() } returns bands

        // Act
        val artists = repository.getArtists()

        // Assert
        assertEquals(2, artists.size)
        assertEquals("Musician 1", artists[0].name)
        assertEquals(ArtistType.MUSICIAN, artists[0].type)
        assertEquals("Band 1", artists[1].name)
        assertEquals(ArtistType.BAND, artists[1].type)
    }

    @Test
    fun `getArtistById for musician returns artist`() = runBlocking {
        // Arrange
        val musician = Musician(1, "Musician 1", "img1", "desc1", "1990-01-01")
        coEvery { musicianService.getMusicianById(1) } returns musician

        // Act
        val artist = repository.getArtistById(1, ArtistType.MUSICIAN)

        // Assert
        assertEquals("Musician 1", artist.name)
        assertEquals(ArtistType.MUSICIAN, artist.type)
    }

    @Test
    fun `getArtistById for band returns artist`() = runBlocking {
        // Arrange
        val band = Band(1, "Band 1", "img1", "desc1", "2000-01-01")
        coEvery { bandService.getBandById(1) } returns band

        // Act
        val artist = repository.getArtistById(1, ArtistType.BAND)

        // Assert
        assertEquals("Band 1", artist.name)
        assertEquals(ArtistType.BAND, artist.type)
    }
}
