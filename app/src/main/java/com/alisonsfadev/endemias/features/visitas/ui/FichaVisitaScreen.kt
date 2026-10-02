package com.alisonsfadev.endemias.features.visitas.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.alisonsfadev.endemias.features.visitas.domain.model.StatusVisita
import com.alisonsfadev.endemias.features.visitas.domain.model.TipoVisita
import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaContext
import com.alisonsfadev.endemias.ui.theme.EndemiasTheme
import com.alisonsfadev.endemias.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FichaVisitaScreen(
    uiState: FichaVisitaUiState,
    onAction: (FichaVisitaAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    BackHandler(enabled = uiState.salvando || uiState.salvo) {}
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ficha de visita") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, enabled = !uiState.salvando && !uiState.salvo) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
                .verticalScroll(rememberScrollState()).padding(MaterialTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.lg),
        ) {
            if (uiState.carregando) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            } else if (uiState.contexto != null) {
                ContextoCard(uiState.contexto)
            } else {
                OutlinedButton(onClick = { onAction(FichaVisitaAction.RecarregarContexto) }) {
                    Text("Tentar carregar novamente")
                }
            }

            SecaoTitulo("Visita")
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)) {
                if (uiState.horaVisivel) {
                    OutlinedTextField(
                        value = uiState.hora,
                        onValueChange = { onAction(FichaVisitaAction.HoraChanged(it)) },
                        label = { Text("Hora") },
                        isError = CampoFichaVisita.HORA in uiState.erros,
                        supportingText = uiState.erros[CampoFichaVisita.HORA]?.let { { Text(it) } },
                        singleLine = true,
                        enabled = uiState.podeEditar,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                        modifier = Modifier.weight(1f).testTag("hora_visita"),
                    )
                }
                DropdownCampo(
                    label = "Tipo", valor = uiState.tipoVisita.label,
                    opcoes = TipoVisita.entries, rotulo = { it.label },
                    onSelecionar = { onAction(FichaVisitaAction.TipoVisitaChanged(it)) },
                    enabled = uiState.podeEditar,
                    modifier = Modifier.weight(1f),
                )
            }

            SecaoTitulo("Situação")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)) {
                StatusVisita.entries.forEach { status ->
                    FilterChip(
                        selected = uiState.status == status,
                        onClick = { onAction(FichaVisitaAction.SituacaoChanged(status)) },
                        label = { Text(status.label) }, enabled = uiState.podeEditar,
                        modifier = Modifier.testTag("situacao_${status.name}"),
                    )
                }
            }
            val erroSituacao = uiState.erros[CampoFichaVisita.SITUACAO]
            if (erroSituacao != null) {
                Text(erroSituacao, color = MaterialTheme.colorScheme.error)
            } else if (uiState.status == null) {
                Text("Selecione a situação do imóvel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            if (uiState.trabalhado) {
                SecaoTitulo("Tratamento")
                TratamentoBloco(uiState, onAction)
            } else if (uiState.status != null) {
                Text(
                    "Sem tratamento nesta visita. O imóvel fica pendente para recuperação.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            uiState.erro?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }
            if (uiState.salvando) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(MaterialTheme.spacing.sm))
            Button(
                onClick = { onAction(FichaVisitaAction.Salvar(irParaProximo = false)) },
                enabled = uiState.podeEditar,
                modifier = Modifier.fillMaxWidth().testTag("salvar_visita"),
                shape = MaterialTheme.shapes.small,
            ) {
                Text(if (uiState.salvando) "Salvando..." else "Salvar visita")
            }
            OutlinedButton(
                onClick = { onAction(FichaVisitaAction.Salvar(irParaProximo = true)) },
                enabled = uiState.podeEditar,
                modifier = Modifier.fillMaxWidth().testTag("salvar_proximo"),
            ) { Text("Salvar e ir para o próximo imóvel") }
        }
    }
}

private fun contextoPreview() = VisitaContext("12", "—", "Rua das Flores", "101", "RESIDENCIA", "—")

@Preview(name = "Inicial claro", showBackground = true)
@Preview(name = "Inicial escuro", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FichaVisitaScreenPreview() {
    EndemiasTheme {
        FichaVisitaScreen(
            uiState = FichaVisitaUiState(imovelId = 1, contexto = contextoPreview(), carregando = false),
            onAction = {}, onNavigateBack = {},
        )
    }
}

@Preview(name = "Visita iniciada", showBackground = true, heightDp = 1000)
@Composable
private fun FichaVisitaIniciadaPreview() {
    EndemiasTheme {
        FichaVisitaScreen(
            uiState = FichaVisitaUiState(
                imovelId = 1, contexto = contextoPreview(), carregando = false,
                status = StatusVisita.TRABALHADO, visitaIniciada = true, hora = "08:42",
            ),
            onAction = {}, onNavigateBack = {},
        )
    }
}
