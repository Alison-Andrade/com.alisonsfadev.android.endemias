package com.alisonsfadev.endemias.features.visitas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alisonsfadev.endemias.features.visitas.domain.model.StatusVisita
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FichaVisitaViewModel(private val imovelId: Long) : ViewModel() {

    private val _uiState = MutableStateFlow(FichaVisitaUiState(imovelId = imovelId, enderecoImovel = "Imóvel #$imovelId"))
    val uiState: StateFlow<FichaVisitaUiState> = _uiState.asStateFlow()

    fun updateStatus(status: StatusVisita) {
        _uiState.update { it.copy(status = status) }
    }

    fun updateObservacao(obs: String) {
        _uiState.update { it.copy(observacao = obs) }
    }

    fun updateQntdEliminados(qntd: String) {
        _uiState.update { it.copy(qntdEliminados = qntd) }
    }

    fun updateQntdTratados(qntd: String) {
        _uiState.update { it.copy(qntdTratados = qntd) }
    }

    fun updateQntdLarvicida(qntd: String) {
        _uiState.update { it.copy(qntdLarvicida = qntd) }
    }

    fun updateTipoLarvicida(tipo: String) {
        _uiState.update { it.copy(tipoLarvicida = tipo) }
    }

    fun updateFocal(focal: Boolean) {
        _uiState.update { it.copy(focal = focal) }
    }

    fun salvarVisita(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(salvando = true) }
            delay(400)
            _uiState.update { it.copy(salvando = false, sucesso = true) }
            onSuccess()
        }
    }
}
