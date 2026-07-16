package com.alisonsfadev.endemias.features.visitas.ui
//
//import androidx.lifecycle.ViewModel
//import com.alisonsfadev.endemias.features.visitas.data.StatusImovel
//import com.alisonsfadev.endemias.features.visitas.data.getMockRgData
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//
//class RgViewModel : ViewModel() {
//
//    private val _uiState = MutableStateFlow(RgUiState())
//    val uiState: StateFlow<RgUiState> = _uiState.asStateFlow()
//
//    init {
//        carregarRg()
//    }
//
//    private fun carregarRg() {
//        val rg = getMockRgData()
//        val todosImoveis = rg.lados.flatMap { it.imoveis }
//
//        _uiState.update { it ->
//            it.copy(
//                rg = rg,
//                totalVisitados = todosImoveis.count { it.status == StatusImovel.VISITADO },
//                totalFocos     = todosImoveis.count { it.status == StatusImovel.FOCO },
//                totalPendentes = todosImoveis.count { it.status == StatusImovel.PENDENTE },
//                )
//        }
//    }
//
//}