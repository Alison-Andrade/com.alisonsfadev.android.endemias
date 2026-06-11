package com.alisonsfadev.endemias.features.home.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        carregarDados()
    }

    private fun carregarDados() {
        _uiState.update {
            it.copy(
                nomeAgente = "Alison",
                zona = "Zona 28 - Gafuringa",
                visitasRealizadas = 12,
                visitasPrevistas = 28,
                focosEncontrados = 3,
                imoveisTratados = 8,
                amostrasColetadas = 5
            )
        }
    }

}