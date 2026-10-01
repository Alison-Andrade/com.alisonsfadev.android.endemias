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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alisonsfadev.endemias.features.visitas.domain.model.StatusVisita
import com.alisonsfadev.endemias.ui.theme.EndemiasTheme
import com.alisonsfadev.endemias.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaVisitaScreen(
    imovelId: Long,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    uiState: FichaVisitaUiState
) {
    val viewModel: FichaVisitaViewModel = viewModel(
        factory = viewModelFactory {
            initializer { FichaVisitaViewModel(imovelId) }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ficha de Visita - Imóvel #${uiState.imovelId}") },
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
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.xl)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.lg)
        ) {
            Text(
                text = "Visita",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)
            ) {
                OutlinedTextField(
                    value = "8:15",
                    onValueChange = { },
                    label = { Text("Hora") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = "N (normal)",
                    onValueChange = { },
                    label = { Text("Tipo") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusVisita.entries.forEach { status ->
                    FilterChip(
                        selected = uiState.status == status,
                        onClick = { viewModel.updateStatus(status) },
                        label = { Text(status.label) }
                    )
                }
            }

            if (uiState.trabalhado) {
                Spacer(Modifier.height(MaterialTheme.spacing.sm))

            }

            Spacer(Modifier.height(MaterialTheme.spacing.xl))

            Button(
                onClick = { viewModel.salvarVisita(onSaveSuccess) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
            ) {
                Text(if (uiState.salvando) "Salvando..." else "Salvar Ficha de Visita")
            }
        }
    }
}

@Preview
@Composable
fun FichaVisitaScreenPreview() {
    EndemiasTheme(
        darkTheme = true
    ) {
        FichaVisitaScreen(
            imovelId = 1,
            onNavigateBack = {},
            onSaveSuccess = {},
            uiState = FichaVisitaUiState(
                imovelId = 1,
                enderecoImovel = "Rua das Flores, 123",
                status = StatusVisita.TRABALHADO,
            )
        )
    }
}
