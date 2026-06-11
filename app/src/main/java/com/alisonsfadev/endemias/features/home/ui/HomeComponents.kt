package com.alisonsfadev.endemias.features.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alisonsfadev.endemias.ui.theme.EndemiasTheme

@Composable
fun ResumoCard(
    label: String,
    valor: Int,
    subtitulo: String,
    cor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = cor,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = subtitulo,
                style = MaterialTheme.typography.labelSmall,
                color = cor
            )
        }

    }
}

@Preview(showBackground = false)
@Composable
fun ResumoCardPreview() {
    EndemiasTheme(
        darkTheme = true
    ) {
        ResumoCard(
            label = "Visitas realizadas",
            valor = 12,
            subtitulo = "de ${25} previstas",
            cor = MaterialTheme.colorScheme.primary,
            )
    }
}