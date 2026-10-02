package com.alisonsfadev.endemias.features.visitas.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle

@Composable
fun FichaVisitaRoute(
    onNavigateBack: () -> Unit,
    onSaveSuccess: (Long?) -> Unit,
    viewModel: FichaVisitaViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onSaveSuccessAtual by rememberUpdatedState(onSaveSuccess)
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.eventos.collect { evento ->
                when (evento) {
                    is FichaVisitaEvento.Salvo -> onSaveSuccessAtual(evento.proximoImovelId)
                }
            }
        }
    }

    FichaVisitaScreen(uiState = uiState, onAction = viewModel::onAction, onNavigateBack = onNavigateBack)
}
