package com.alisonsfadev.endemias.features.visitas.domain

data class Imovel(
    val id: Long,
    val numero: String,
    val logradouro: String,
    val tipo: String,
    val visitado: Boolean
)
