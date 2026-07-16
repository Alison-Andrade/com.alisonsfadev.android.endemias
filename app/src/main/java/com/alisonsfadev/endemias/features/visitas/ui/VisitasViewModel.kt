package com.alisonsfadev.endemias.features.visitas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alisonsfadev.endemias.features.visitas.data.MockVisitasDataSource
import com.alisonsfadev.endemias.features.visitas.domain.Quarteirao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VisitasViewModel : ViewModel() {

    private val _quarteiroes = MutableStateFlow<List<Quarteirao>>(emptyList())
    val quarteiroes: StateFlow<List<Quarteirao>> = _quarteiroes.asStateFlow()

    init {
        carregarQuarteiroes()
    }

    private fun carregarQuarteiroes() {
        viewModelScope.launch {
            _quarteiroes.value = MockVisitasDataSource.getQuarteiroes()
        }
    }

}