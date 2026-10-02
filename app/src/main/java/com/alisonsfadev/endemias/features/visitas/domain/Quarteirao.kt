package com.alisonsfadev.endemias.features.visitas.domain

data class Quarteirao(
    val id: Long,
    val numero: Int,
    val logradouroPrincipal: String,
    val totalImoveis: Int,
    val imoveisVisitados: Int,
    val residencias: Int = 0,
    val comercios: Int = 0,
    val terrenosBaldios: Int = 0,
    val outros: Int = 0,
)
