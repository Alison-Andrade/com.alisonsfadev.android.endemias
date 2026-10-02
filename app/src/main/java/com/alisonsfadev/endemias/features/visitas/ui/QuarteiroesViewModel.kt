package com.alisonsfadev.endemias.features.visitas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alisonsfadev.endemias.features.visitas.data.MockVisitasDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class QuarteiroesViewModel @Inject constructor(dataSource: MockVisitasDataSource) : ViewModel() {
    val quarteiroes = dataSource.observarQuarteiroes().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        dataSource.getQuarteiroes(),
    )
}
