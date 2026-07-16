package com.alisonsfadev.endemias.features.visitas.domain

data class Quarteirao(
    val id: Long,
    val numero: Int,
    val logradouroPrincipal: String,
    val totalImoveis: Int,
    val imoveisVisitados: Int
)