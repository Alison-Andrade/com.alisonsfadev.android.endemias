package com.alisonsfadev.endemias.features.home.ui

data class HomeUiState(
    val nomeAgente: String = "",
    val zona: String = "",
    val visitasRealizadas: Int = 0,
    val visitasPrevistas: Int = 0,
    val focosEncontrados: Int = 0,
    val imoveisTratados: Int = 0,
    val amostrasColetadas: Int = 0,
    val isLoading: Boolean = false,
    val erro: String? = null,
)