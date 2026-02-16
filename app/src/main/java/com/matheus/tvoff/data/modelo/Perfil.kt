package com.matheus.tvoff.data.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Perfil(
    val nickname: String,
    val nome: String,
    val bio: String = "",
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("albens_favoritos")
    val albensFavoritos: List<Album> = emptyList(),
    @SerialName("albens_ouvidos")
    val albensOuvidos: List<AvaliacaoAlbum> = emptyList(),
    @SerialName("data_criacao")
    val dataCriacao: Long = System.currentTimeMillis(),
    @SerialName("senha_hash")
    val senhaHash: String = ""
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