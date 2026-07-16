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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alisonsfadev.endemias.features.visitas.domain.Quarteirao
import com.alisonsfadev.endemias.ui.theme.EndemiasTheme
import com.alisonsfadev.endemias.ui.theme.endemiaColors
import com.alisonsfadev.endemias.ui.theme.spacing

@Composable
fun VisitasScreen(
    onQuarteiraoClick: (Long) -> Unit,
    viewModel: VisitasViewModel = viewModel()
) {
    val quarteiroes by viewModel.quarteiroes.collectAsStateWithLifecycle()

    Scaffold() {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(MaterialTheme.spacing.xl)
                .padding(paddingValues)
        ) {
            Text(
                text = "Quarteirões da sua área",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
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
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(MaterialTheme.spacing.xl)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Quarteirão ${quarteirao.numero}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (progresso == 1f) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Concluído",
                        tint = MaterialTheme.endemiaColors.visitado
                    )
                }
            }

            Text(
                text = quarteirao.logradouroPrincipal,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(MaterialTheme.spacing.md))

            LinearProgressIndicator(
                progress = { progresso },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.endemiaColors.visitado,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Text(
                text = "${quarteirao.imoveisVisitados}/${quarteirao.totalImoveis} imóveis",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun VisitasScreenPreview() {
    EndemiasTheme(darkTheme = true) {
        VisitasScreen({})
    }
}