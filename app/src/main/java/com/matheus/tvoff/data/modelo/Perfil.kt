package com.matheus.tvoff.data.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Perfil(
    val nickname: String,
    val nome: String,
    val bio: String = "",
    val avatarUrl: String? = null,
    val albensFavoritos: List<Album> = emptyList(),
    val albensOuvidos: List<AvaliacaoAlbum> = emptyList(),
    val dataCriacao: Long = System.currentTimeMillis()
) {

    fun obterIniciais(): String {
        val partes = nome.trim().split(" ")
        return if (partes.size >= 2) {
            "${partes[0].first()}${partes[1].first()}".uppercase()
        } else {
            nome.take(2).uppercase()
        }
    }

    fun favoritosCompletos(): Boolean = albensFavoritos.size >= 5
}
