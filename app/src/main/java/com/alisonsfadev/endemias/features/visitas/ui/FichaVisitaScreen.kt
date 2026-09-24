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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alisonsfadev.endemias.features.visitas.domain.model.StatusVisita
import com.alisonsfadev.endemias.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaVisitaScreen(
    imovelId: Long,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
) {
    val viewModel: FichaVisitaViewModel = viewModel(
        factory = viewModelFactory {
            initializer { FichaVisitaViewModel(imovelId) }
        }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                text = "Situação da Visita",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusVisita.entries.forEach { status ->
                    FilterChip(
                        selected = uiState.status == status,
                        onClick = { viewModel.updateStatus(status) },
                        label = { Text(status.label) },
                        leadingIcon = if (uiState.status == status) {
                            { Icon(Icons.Default.Check, contentDescription = null) }
                        } else null
                    )
                }
            }

            if (uiState.exigeTratamento) {
                Spacer(Modifier.height(MaterialTheme.spacing.sm))
                Text(
                    text = "Dados do Tratamento / Focos",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                OutlinedTextField(
                    value = uiState.qntdEliminados,
                    onValueChange = { viewModel.updateQntdEliminados(it) },
                    label = { Text("Depósitos Eliminados") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.qntdTratados,
                    onValueChange = { viewModel.updateQntdTratados(it) },
                    label = { Text("Depósitos Tratados (Larvicida)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.qntdLarvicida,
                    onValueChange = { viewModel.updateQntdLarvicida(it) },
                    label = { Text("Quantidade de Larvicida (g)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Imóvel com Foco (Focal)?")
                    Switch(
                        checked = uiState.focal,
                        onCheckedChange = { viewModel.updateFocal(it) }
                    )
                }
            }

            OutlinedTextField(
                value = uiState.observacao,
                onValueChange = { viewModel.updateObservacao(it) },
                label = { Text("Observações") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(Modifier.height(MaterialTheme.spacing.xl))

            Button(
                onClick = { viewModel.salvarVisita(onSaveSuccess) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.salvando) "Salvando..." else "Salvar Ficha de Visita")
            }
        }
    }
}
