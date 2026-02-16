package com.matheus.tvoff.data.repositorio

import com.matheus.tvoff.BuildConfig
import com.matheus.tvoff.data.api.LastFmApi
import com.matheus.tvoff.data.mock.AlbunsMock
import com.matheus.tvoff.data.modelo.Album

class AlbumRepositorio {
    
    private val lastFmApi: LastFmApi? = try {
        if (BuildConfig.LASTFM_API_KEY.isNotBlank()) {
            LastFmApi(BuildConfig.LASTFM_API_KEY)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
    
    private val cache = mutableMapOf<String, List<Album>>()

    suspend fun buscar(query: String): List<Album> {
        if (query.isBlank()) {
            return obterAlbunsPopulares()
        }
        
        val cacheKey = query.lowercase().trim()
        cache[cacheKey]?.let { return it }
        
        if (lastFmApi != null) {
            try {
                val resultados = lastFmApi.buscarAlbuns(query, limite = 50)
                if (resultados.isNotEmpty()) {
                    cache[cacheKey] = resultados
                    return resultados
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        val resultadosMock = AlbunsMock.buscar(query)
        cache[cacheKey] = resultadosMock
        return resultadosMock
    }

    suspend fun obterAlbunsPopulares(): List<Album> {
        if (lastFmApi != null) {
            try {
                val artistasPopulares = listOf(
                    "Radiohead",
                    "Pink Floyd",
                    "The Beatles",
                    "Kendrick Lamar",
                    "Daft Punk",
                    "Miles Davis"
                )
                
                val resultados = mutableListOf<Album>()

                for (artista in artistasPopulares) {
                    val topAlbuns = lastFmApi.buscarTopAlbunsArtista(artista, limite = 3)
                    resultados.addAll(topAlbuns)
                }
                
                if (resultados.isNotEmpty()) {
                    return resultados.shuffled().take(30)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        return AlbunsMock.albuns.shuffled()
    }

    suspend fun buscarDetalhes(artista: String, titulo: String): Album? {
        if (lastFmApi != null) {
            try {
                return lastFmApi.buscarAlbumDetalhes(artista, titulo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        return AlbunsMock.albuns.find {
            it.artista.equals(artista, ignoreCase = true) && 
            it.titulo.equals(titulo, ignoreCase = true) 
        }
    }

    fun limparCache() {
        cache.clear()
    }
}
