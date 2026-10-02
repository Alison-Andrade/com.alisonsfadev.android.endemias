package com.alisonsfadev.endemias.features.visitas.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alisonsfadev.endemias.core.navigation.EndemiasScreens
import com.alisonsfadev.endemias.features.visitas.data.MockVisitasDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ImoveisViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataSource: MockVisitasDataSource,
) : ViewModel() {
    private val quarteiraoId: Long = checkNotNull(savedStateHandle[EndemiasScreens.ARG_QUARTEIRAO_ID])
    val imoveis = dataSource.observarImoveis(quarteiraoId).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        dataSource.getImoveisDoQuarteirao(quarteiraoId),
    )
}
