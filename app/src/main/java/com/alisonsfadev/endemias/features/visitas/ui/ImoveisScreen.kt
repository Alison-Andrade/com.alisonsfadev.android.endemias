package com.alisonsfadev.endemias.features.visitas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alisonsfadev.endemias.features.visitas.domain.Imovel
import com.alisonsfadev.endemias.ui.theme.endemiaColors
import com.alisonsfadev.endemias.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImoveisScreen(
    quarteiraoId: Long,
    onNavigateBack: () -> Unit,
    onImovelClick: (Long) -> Unit,
) {
    val viewModel: ImoveisViewModel = viewModel(
        factory = viewModelFactory {
            initializer { ImoveisViewModel(quarteiraoId) }
        }
    )
    val imoveis by viewModel.imoveis.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Imóveis do Quarteirão #$quarteiraoId") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(MaterialTheme.spacing.xl)
                .padding(paddingValues)
        ) {
            Text(
                text = "Selecione um imóvel para vistoriar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(MaterialTheme.spacing.xl))

            LazyColumn {
                items(imoveis, key = { it.id }) { imovel ->
                    ImovelCard(
                        imovel = imovel,
                        onClick = { onImovelClick(imovel.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ImovelCard(
    imovel: Imovel,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.lg)
        ) {
            Text(
                text = "Nº ${imovel.numero}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.width(MaterialTheme.spacing.md))
            Text(
                text = imovel.tipo,
                style = MaterialTheme.typography.bodySmall,
                color = if (imovel.visitado)
                    MaterialTheme.endemiaColors.visitado
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
