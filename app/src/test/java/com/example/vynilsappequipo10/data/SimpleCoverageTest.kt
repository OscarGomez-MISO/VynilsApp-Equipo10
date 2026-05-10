package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.domain.AlbumRequest
import com.example.vynilsappequipo10.domain.Artist
import com.example.vynilsappequipo10.domain.ArtistType
import com.example.vynilsappequipo10.domain.Band
import com.example.vynilsappequipo10.domain.Collector
import com.example.vynilsappequipo10.domain.CollectorAlbum
import com.example.vynilsappequipo10.domain.CollectorAlbumWithAlbum
import com.example.vynilsappequipo10.domain.CollectorDetail
import com.example.vynilsappequipo10.domain.CollectorIdRequest
import com.example.vynilsappequipo10.domain.CollectorRequest
import com.example.vynilsappequipo10.domain.Comment
import com.example.vynilsappequipo10.domain.CommentRequest
import com.example.vynilsappequipo10.domain.FavoritePerformer
import com.example.vynilsappequipo10.domain.Musician
import com.example.vynilsappequipo10.domain.Performer
import com.example.vynilsappequipo10.domain.Track
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Tests para verificar cobertura de domain models y data classes.
 */
class SimpleCoverageTest {

    @Test
    fun `verificar Album domain coverage`() {
        val album = Album(
            id = 1,
            name = "Test Album",
            cover = "url",
            releaseDate = "2024",
            description = "Desc",
            genre = "Rock",
            recordLabel = "Sony"
        )
        assertEquals("Test Album", album.name)
        assertEquals(1, album.id)
        assertEquals("url", album.cover)
        assertEquals("2024", album.releaseDate)
        assertEquals("Desc", album.description)
        assertEquals("Rock", album.genre)
        assertEquals("Sony", album.recordLabel)
    }

    @Test
    fun `verificar Album with tracks performers and comments`() {
        val track = Track(1, "Track 1", "3:30")
        val performer = Performer(1, "Artist", "img", "desc", "1990")
        val comment = Comment(1, "Great!", 5)
        
        val album = Album(
            id = 1,
            name = "Full Album",
            cover = "url",
            releaseDate = "2024",
            description = "Desc",
            genre = "Rock",
            recordLabel = "Sony",
            tracks = listOf(track),
            performers = listOf(performer),
            comments = listOf(comment)
        )
        
        assertEquals(1, album.tracks.size)
        assertEquals("Track 1", album.tracks[0].name)
        assertEquals("3:30", album.tracks[0].duration)
        assertEquals(1, album.performers.size)
        assertEquals("Artist", album.performers[0].name)
        assertEquals(1, album.comments.size)
        assertEquals(5, album.comments[0].rating)
    }

    @Test
    fun `verificar Track domain coverage`() {
        val track = Track(1, "Song Name", "4:15")
        assertEquals(1, track.id)
        assertEquals("Song Name", track.name)
        assertEquals("4:15", track.duration)
    }

    @Test
    fun `verificar Performer domain coverage`() {
        val performer = Performer(1, "Performer Name", "img.jpg", "Bio", "1985-01-01")
        assertEquals(1, performer.id)
        assertEquals("Performer Name", performer.name)
        assertEquals("img.jpg", performer.image)
        assertEquals("Bio", performer.description)
        assertEquals("1985-01-01", performer.birthDate)
        
        val performerNoBirthDate = Performer(2, "Another", "img", "desc")
        assertNull(performerNoBirthDate.birthDate)
    }

    @Test
    fun `verificar Comment domain coverage`() {
        val comment = Comment(1, "Amazing album!", 5)
        assertEquals(1, comment.id)
        assertEquals("Amazing album!", comment.description)
        assertEquals(5, comment.rating)
    }

    @Test
    fun `verificar AlbumRequest domain coverage`() {
        val request = AlbumRequest("Name", "cover", "2024", "desc", "Rock", "EMI")
        assertEquals("Name", request.name)
        assertEquals("cover", request.cover)
        assertEquals("2024", request.releaseDate)
        assertEquals("desc", request.description)
        assertEquals("Rock", request.genre)
        assertEquals("EMI", request.recordLabel)
    }

    @Test
    fun `verificar Artist domain coverage`() {
        val artist = Artist(1, "Artist Name", "img", "desc", "1990", emptyList(), ArtistType.MUSICIAN)
        assertEquals(1, artist.id)
        assertEquals("Artist Name", artist.name)
        assertEquals("img", artist.image)
        assertEquals("desc", artist.description)
        assertEquals("1990", artist.date)
        assertEquals(ArtistType.MUSICIAN, artist.type)
        
        val band = Artist(2, "Band Name", "img", "desc", "2000", emptyList(), ArtistType.BAND)
        assertEquals(ArtistType.BAND, band.type)
    }

    @Test
    fun `verificar ArtistType enum coverage`() {
        assertEquals(ArtistType.MUSICIAN, ArtistType.valueOf("MUSICIAN"))
        assertEquals(ArtistType.BAND, ArtistType.valueOf("BAND"))
        assertEquals(2, ArtistType.values().size)
    }

    @Test
    fun `verificar Musician domain coverage`() {
        val musician = Musician(1, "Rubén Blades", "img", "desc", "1948-07-16", emptyList())
        assertEquals(1, musician.id)
        assertEquals("Rubén Blades", musician.name)
        assertEquals("img", musician.image)
        assertEquals("desc", musician.description)
        assertEquals("1948-07-16", musician.birthDate)
    }

    @Test
    fun `verificar Band domain coverage`() {
        val band = Band(1, "Queen", "img", "desc", "1970-01-01", emptyList())
        assertEquals(1, band.id)
        assertEquals("Queen", band.name)
        assertEquals("img", band.image)
        assertEquals("desc", band.description)
        assertEquals("1970-01-01", band.creationDate)
    }

    @Test
    fun `verificar Collector domain coverage`() {
        val collector = Collector(1, "Collector Name", "123456789", "email@test.com")
        assertEquals(1, collector.id)
        assertEquals("Collector Name", collector.name)
        assertEquals("123456789", collector.telephone)
        assertEquals("email@test.com", collector.email)
        
        val collectorNoId = Collector(name = "No ID", telephone = "111", email = "a@b.com")
        assertNull(collectorNoId.id)
    }

    @Test
    fun `verificar CollectorAlbum domain coverage`() {
        val collectorAlbum = CollectorAlbum(1, 25.99, "Active")
        assertEquals(1, collectorAlbum.id)
        assertEquals(25.99, collectorAlbum.price, 0.01)
        assertEquals("Active", collectorAlbum.status)
    }

    @Test
    fun `verificar CollectorAlbumWithAlbum domain coverage`() {
        val album = Album(1, "Album", "cover", "2024", "desc", "Rock", "Sony")
        val collectorAlbumWithAlbum = CollectorAlbumWithAlbum(1, 30.0, "Inactive", album)
        assertEquals(1, collectorAlbumWithAlbum.id)
        assertEquals(30.0, collectorAlbumWithAlbum.price, 0.01)
        assertEquals("Inactive", collectorAlbumWithAlbum.status)
        assertEquals("Album", collectorAlbumWithAlbum.album.name)
    }

    @Test
    fun `verificar CollectorDetail domain coverage`() {
        val comment = Comment(1, "Nice", 4)
        val performer = FavoritePerformer(1, "Artist", "img", "desc")
        val collectorAlbum = CollectorAlbum(1, 20.0, "Active")
        
        val detail = CollectorDetail(
            id = 1,
            name = "Collector",
            telephone = "123",
            email = "a@a.com",
            comments = listOf(comment),
            favoritePerformers = listOf(performer),
            collectorAlbums = listOf(collectorAlbum)
        )
        
        assertEquals(1, detail.id)
        assertEquals("Collector", detail.name)
        assertEquals(1, detail.comments.size)
        assertEquals(1, detail.favoritePerformers.size)
        assertEquals(1, detail.collectorAlbums.size)
    }

    @Test
    fun `verificar FavoritePerformer domain coverage`() {
        val performer = FavoritePerformer(1, "Favorite Artist", "img.jpg", "Biography")
        assertEquals(1, performer.id)
        assertEquals("Favorite Artist", performer.name)
        assertEquals("img.jpg", performer.image)
        assertEquals("Biography", performer.description)
    }

    @Test
    fun `verificar CollectorRequest domain coverage`() {
        val request = CollectorRequest("Name", "123456", "email@test.com")
        assertEquals("Name", request.name)
        assertEquals("123456", request.telephone)
        assertEquals("email@test.com", request.email)
    }

    @Test
    fun `verificar CommentRequest domain coverage`() {
        val collectorId = CollectorIdRequest(100)
        val request = CommentRequest("Great album!", 5, collectorId)
        assertEquals("Great album!", request.description)
        assertEquals(5, request.rating)
        assertEquals(100, request.collector.id)
    }

    @Test
    fun `verificar CollectorIdRequest domain coverage`() {
        val request = CollectorIdRequest(42)
        assertEquals(42, request.id)
    }

    @Test
    fun `verificar Album copy function`() {
        val original = Album(1, "Original", "cover", "2024", "desc", "Rock", "Sony")
        val copy = original.copy(name = "Modified")
        
        assertEquals("Modified", copy.name)
        assertEquals(original.id, copy.id)
        assertNotEquals(original.name, copy.name)
    }

    @Test
    fun `verificar Artist with null date`() {
        val artist = Artist(1, "Artist", "img", "desc", null, emptyList(), ArtistType.MUSICIAN)
        assertNull(artist.date)
    }

    @Test
    fun `verificar data class equals and hashCode`() {
        val album1 = Album(1, "Album", "cover", "2024", "desc", "Rock", "Sony")
        val album2 = Album(1, "Album", "cover", "2024", "desc", "Rock", "Sony")
        val album3 = Album(2, "Different", "cover", "2024", "desc", "Rock", "Sony")
        
        assertEquals(album1, album2)
        assertEquals(album1.hashCode(), album2.hashCode())
        assertNotEquals(album1, album3)
    }
}
