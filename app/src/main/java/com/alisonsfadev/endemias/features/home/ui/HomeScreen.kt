package com.alisonsfadev.endemias.features.home.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alisonsfadev.endemias.ui.theme.EndemiasTheme
import com.alisonsfadev.endemias.ui.theme.endemiaColors

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Resumo do dia",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row (
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ResumoCard(
                    label = "Visitas realizadas",
                    valor = uiState.visitasRealizadas,
                    subtitulo = "de ${uiState.visitasPrevistas} previstas",
                    cor = MaterialTheme.endemiaColors.visitado,
                    modifier = Modifier.weight(1f)
                )
                ResumoCard(
                    label = "Focos encontrados",
                    valor = uiState.focosEncontrados,
                    subtitulo = "Aedes",
                    cor = MaterialTheme.endemiaColors.focoCritico,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ResumoCard(
                    label = "Imóveis tratados",
                    valor = uiState.imoveisTratados,
                    subtitulo = "larvicida aplicado",
                    cor = MaterialTheme.endemiaColors.ausente,
                    modifier = Modifier.weight(1f)
                )
                ResumoCard(
                    label = "Amostras coletadas",
                    valor = uiState.amostrasColetadas,
                    subtitulo = "aguardando lab",
                    cor = MaterialTheme.endemiaColors.info,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    EndemiasTheme(
        darkTheme = true
    ) {
        HomeScreen()
    }
}