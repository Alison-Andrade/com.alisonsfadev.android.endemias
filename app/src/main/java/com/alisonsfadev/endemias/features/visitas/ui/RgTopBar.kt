package com.alisonsfadev.endemias.features.visitas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alisonsfadev.endemias.ui.theme.EndemiasTheme

@Composable
fun RgTopBar(
    localidade: String,
    tipo: String,
    cidade: String,
    uf: String,
    quarteirao: Int,
    totalVisitados: Int,
    totalPendentes: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inversePrimary)
            .statusBarsPadding()
            .padding(horizontal = 14.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable(onClick = onNavigateBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp),
                )
            }
            Column {
                Text(
                    text = "Quarteirão $quarteirao",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Column {
                        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){
                            RgMetaChip(
                                icon = Icons.Outlined.LocationOn,
                                label = "$cidade · $uf"
                            )
                            RgMetaChip(
                                icon = Icons.Outlined.Numbers,
                                label = "2405801"
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){
                            RgMetaChip(
                                icon = Icons.Outlined.LocationOn,
                                label = "$localidade / $tipo"
                            )
                            RgMetaChip(
                                icon = Icons.Outlined.GridView,
                                label = "00144"
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun RgMetaChip(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = Color.White.copy(alpha = 0.15f),
                shape = MaterialTheme.shapes.extraLarge
            )
            .padding(horizontal = 9.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF9FE1CB),
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun RgTotalBadge(
    valor: Int,
    sufixo: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = containerColor,
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 8.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$valor $sufixo",
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
fun RgTopBarPreview() {
    EndemiasTheme(
        darkTheme = true
    ) {
        RgTopBar(
            localidade = "Gafuringa",
            tipo = "BR",
            cidade = "João Câmara",
            uf = "RN",
            quarteirao = 128,
            totalVisitados = 15,
            totalPendentes = 10,
            onNavigateBack = {}
        )
    }
}