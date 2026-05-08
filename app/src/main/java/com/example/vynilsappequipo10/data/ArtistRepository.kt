package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.data.remote.BandService
import com.example.vynilsappequipo10.data.remote.MusicianService
import com.example.vynilsappequipo10.data.remote.RetrofitClient
import com.example.vynilsappequipo10.domain.Artist
import com.example.vynilsappequipo10.domain.ArtistType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

open class ArtistRepository(
    private val musicianService: MusicianService = RetrofitClient.musicianService,
    private val bandService: BandService = RetrofitClient.bandService
) {
    open suspend fun getArtists(): List<Artist> = withContext(Dispatchers.IO) {
        val musiciansDeferred = async { musicianService.getMusicians() }
        val bandsDeferred = async { bandService.getBands() }

        val musicians = musiciansDeferred.await().map { m ->
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
        val bands = bandsDeferred.await().map { b ->
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
        musicians + bands
    }

    open suspend fun getArtistById(id: Int, type: ArtistType): Artist = withContext(Dispatchers.IO) {
        if (type == ArtistType.MUSICIAN) {
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
