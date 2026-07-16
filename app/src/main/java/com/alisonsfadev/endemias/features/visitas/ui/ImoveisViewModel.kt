package com.alisonsfadev.endemias.features.visitas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alisonsfadev.endemias.features.visitas.data.MockVisitasDataSource
import com.alisonsfadev.endemias.features.visitas.domain.Imovel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImoveisViewModel(private val quarteiraoId: Long) : ViewModel() {

    private val _imoveis = MutableStateFlow<List<Imovel>>(emptyList())
    val imoveis: StateFlow<List<Imovel>> = _imoveis.asStateFlow()

    init {
        viewModelScope.launch {
            _imoveis.value = MockVisitasDataSource.getImoveisDoQuarteirao((quarteiraoId))
        }
    }

}