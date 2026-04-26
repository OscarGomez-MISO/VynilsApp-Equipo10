package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.BandService
import com.example.vynilsappequipo10.data.remote.MusicianService
import com.example.vynilsappequipo10.data.remote.RetrofitClient
import com.example.vynilsappequipo10.domain.Artist
import com.example.vynilsappequipo10.domain.ArtistType

open class ArtistRepository(
    private val musicianService: MusicianService = RetrofitClient.musicianService,
    private val bandService: BandService = RetrofitClient.bandService
) {
    open suspend fun getArtists(): List<Artist> {
        val musicians = musicianService.getMusicians().map { m ->
            Artist(
                id = m.id,
                name = m.name,
                image = m.image,
                description = m.description,
                date = m.birthDate,
                albums = m.albums,
                type = ArtistType.MUSICIAN
            )
        }
        val bands = bandService.getBands().map { b ->
            Artist(
                id = b.id,
                name = b.name,
                image = b.image,
                description = b.description,
                date = b.creationDate,
                albums = b.albums,
                type = ArtistType.BAND
            )
        }
        return musicians + bands
    }

    open suspend fun getArtistById(id: Int, type: ArtistType): Artist {
        return if (type == ArtistType.MUSICIAN) {
            val m = musicianService.getMusicianById(id)
            Artist(id = m.id, name = m.name, image = m.image, description = m.description,
                date = m.birthDate, albums = m.albums, type = ArtistType.MUSICIAN)
        } else {
            val b = bandService.getBandById(id)
            Artist(id = b.id, name = b.name, image = b.image, description = b.description,
                date = b.creationDate, albums = b.albums, type = ArtistType.BAND)
        }
    }
}
