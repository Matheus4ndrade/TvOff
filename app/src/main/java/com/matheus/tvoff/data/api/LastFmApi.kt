package com.matheus.tvoff.data.api

import com.matheus.tvoff.data.modelo.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URLEncoder

class LastFmApi(private val apiKey: String) {

    companion object {
        private const val BASE_URL = "https://ws.audioscrobbler.com/2.0/"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend fun buscarAlbuns(query: String, limite: Int = 30): List<Album> = withContext(Dispatchers.IO) {
        try {
            val queryEncoded = URLEncoder.encode(query, "UTF-8")
            val url = "$BASE_URL?method=album.search&album=$queryEncoded&api_key=$apiKey&format=json&limit=$limite"

            val response = java.net.URL(url).readText()
            val resultado = json.decodeFromString<LastFmSearchResponse>(response)

            resultado.results.albummatches.album.map { it.paraAlbum() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun buscarAlbumDetalhes(artista: String, album: String): Album? = withContext(Dispatchers.IO) {
        try {
            val artistaEncoded = URLEncoder.encode(artista, "UTF-8")
            val albumEncoded = URLEncoder.encode(album, "UTF-8")
            val url = "$BASE_URL?method=album.getinfo&artist=$artistaEncoded&album=$albumEncoded&api_key=$apiKey&format=json"

            val response = java.net.URL(url).readText()
            val resultado = json.decodeFromString<LastFmAlbumInfoResponse>(response)

            resultado.album.paraAlbum()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun buscarTopAlbunsArtista(artista: String, limite: Int = 10): List<Album> = withContext(Dispatchers.IO) {
        try {
            val artistaEncoded = URLEncoder.encode(artista, "UTF-8")
            val url = "$BASE_URL?method=artist.gettopalbums&artist=$artistaEncoded&api_key=$apiKey&format=json&limit=$limite"

            val response = java.net.URL(url).readText()
            val resultado = json.decodeFromString<LastFmTopAlbumsResponse>(response)

            resultado.topalbums.album.map { it.paraAlbum() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

@Serializable
data class LastFmSearchResponse(
    val results: LastFmResults
)

@Serializable
data class LastFmResults(
    val albummatches: LastFmAlbumMatches
)

@Serializable
data class LastFmAlbumMatches(
    val album: List<LastFmAlbumSearch>
)


@Serializable
data class LastFmAlbumSearch(
    val name: String,
    val artist: String,
    val url: String? = null,
    val image: List<LastFmImage> = emptyList(),
    val mbid: String? = null
) {
    fun paraAlbum(): Album {
        val capaUrl = image
            .lastOrNull { it.size == "extralarge" || it.size == "large" || it.size == "medium" }
            ?._text
            ?: image.lastOrNull()?._text
            ?: ""

        val idUnico = if (mbid.isNullOrBlank()) {
            "${artist.hashCode()}_${name.hashCode()}_${System.currentTimeMillis()}"
        } else {
            mbid
        }

        return Album(
            id = idUnico,
            titulo = name,
            artista = artist,
            capaUrl = capaUrl,
            anoLancamento = null
        )
    }
}


@Serializable
data class LastFmTopAlbumsResponse(
    val topalbums: LastFmTopAlbums
)

@Serializable
data class LastFmTopAlbums(
    val album: List<LastFmAlbumTop>
)

@Serializable
data class LastFmAlbumTop(
    val name: String,
    val artist: LastFmArtist,
    val url: String? = null,
    val image: List<LastFmImage> = emptyList(),
    val mbid: String? = null
) {
    fun paraAlbum(): Album {
        val capaUrl = image
            .lastOrNull { it.size == "extralarge" || it.size == "large" || it.size == "medium" }
            ?._text
            ?: image.lastOrNull()?._text
            ?: ""

        val idUnico = if (mbid.isNullOrBlank()) {
            "${artist.name.hashCode()}_${name.hashCode()}_${System.currentTimeMillis()}"
        } else {
            mbid
        }

        return Album(
            id = idUnico,
            titulo = name,
            artista = artist.name,
            capaUrl = capaUrl,
            anoLancamento = null
        )
    }
}

@Serializable
data class LastFmArtist(
    val name: String,
    val mbid: String? = null,
    val url: String? = null
)

@Serializable
data class LastFmAlbumInfoResponse(
    val album: LastFmAlbumInfo
)

@Serializable
data class LastFmAlbumInfo(
    val name: String,
    val artist: String,
    val url: String? = null,
    val image: List<LastFmImage> = emptyList(),
    val mbid: String? = null,
    val wiki: LastFmWiki? = null
) {
    fun paraAlbum(): Album {
        val capaUrl = image
            .lastOrNull { it.size == "extralarge" || it.size == "large" }
            ?._text
            ?: ""

        val idUnico = if (mbid.isNullOrBlank()) {
            "${artist.hashCode()}_${name.hashCode()}_${System.currentTimeMillis()}"
        } else {
            mbid
        }

        return Album(
            id = idUnico,
            titulo = name,
            artista = artist,
            capaUrl = capaUrl,
            anoLancamento = null
        )
    }
}

@Serializable
data class LastFmImage(
    @SerialName("#text") val _text: String,
    val size: String
)

@Serializable
data class LastFmWiki(
    val published: String? = null,
    val summary: String? = null,
    val content: String? = null
)