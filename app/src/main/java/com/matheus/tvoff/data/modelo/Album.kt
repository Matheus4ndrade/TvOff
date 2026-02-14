package com.matheus.tvoff.data.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val id: String = "",
    val titulo: String,
    val artista: String,
    val capaUrl: String,
    val anoLancamento: Int? = null
)
