package com.alisonsfadev.endemias.features.visitas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.outlined.Terrain
import androidx.compose.material.icons.outlined.Warehouse
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.alisonsfadev.endemias.features.visitas.domain.Quarteirao
import com.alisonsfadev.endemias.ui.theme.EndemiasTheme
import com.alisonsfadev.endemias.ui.theme.endemiaColors
import com.alisonsfadev.endemias.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuarteiroesScreen(
    onQuarteiraoClick: (Long) -> Unit,
    viewModel: QuarteiroesViewModel = hiltViewModel()
) {
    val quarteiroes by viewModel.quarteiroes.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quarteirões Atribuídos") }
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
                text = "Selecione o quarteirão para gerenciar os imóveis",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(MaterialTheme.spacing.xl))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)
            ) {
                items(quarteiroes, key = { it.id }) { quarteirao ->
                    QuarteiraoCard(
                        quarteirao = quarteirao,
                        onClick = { onQuarteiraoClick(quarteirao.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuarteiraoCard(
    quarteirao: Quarteirao,
    onClick: () -> Unit,
) {
    val progresso = if (quarteirao.totalImoveis > 0) {
        quarteirao.imoveisVisitados / quarteirao.totalImoveis.toFloat()
    } else 0f

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(Modifier.padding(MaterialTheme.spacing.xl)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${quarteirao.numero}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
//                if (progresso == 1f) {
//                    Icon(
//                        imageVector = Icons.Filled.CheckCircle,
//                        contentDescription = "Concluído",
//                        tint = MaterialTheme.endemiaColors.visitado
//                    )
//                }
            }

            Spacer(Modifier.height(4.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.5f)
            )

            Spacer(Modifier.height(MaterialTheme.spacing.md))

            Text(
                text = "${quarteirao.imoveisVisitados} de ${quarteirao.totalImoveis} imóveis visitados",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { progresso },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.endemiaColors.visitado,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                gapSize = 0.dp,
                drawStopIndicator = {}
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.md),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = "Residencias",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "R ${quarteirao.residencias}",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Store,
                        contentDescription = "Comércios",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "C ${quarteirao.comercios}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Terrain,
                        contentDescription = "Terrenos Baldios",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "TB ${quarteirao.terrenosBaldios}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Warehouse,
                        contentDescription = "Outros",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "O ${quarteirao.outros}",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun QuarteiroesScreenPreview() {
    EndemiasTheme(darkTheme = true) {
        QuarteiroesScreen({})
    }
}
