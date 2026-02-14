package com.matheus.tvoff.data.modelo

import kotlinx.serialization.Serializable

@Serializable
enum class TipoAvaliacao {
    AMEI,
    GOSTEI,
    NHEM
}

fun TipoAvaliacao.paraEmoji(): String = when (this) {
    TipoAvaliacao.AMEI -> "♥"
    TipoAvaliacao.GOSTEI -> "👍"
    TipoAvaliacao.NHEM -> "😐"
}

fun TipoAvaliacao.paraCor(): Long = when (this) {
    TipoAvaliacao.AMEI -> 0xFFFF4444
    TipoAvaliacao.GOSTEI -> 0xFF44DC44
    TipoAvaliacao.NHEM -> 0xFFDCB400
}
