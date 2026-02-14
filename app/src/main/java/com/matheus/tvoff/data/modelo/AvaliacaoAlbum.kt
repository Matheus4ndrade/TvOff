package com.matheus.tvoff.data.modelo

import kotlinx.serialization.Serializable

@Serializable
data class AvaliacaoAlbum(
    val album: Album,
    val avaliacao: TipoAvaliacao,
    val dataAdicao: Long = System.currentTimeMillis()
)
