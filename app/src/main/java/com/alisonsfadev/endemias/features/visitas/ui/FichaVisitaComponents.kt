package com.alisonsfadev.endemias.features.visitas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alisonsfadev.endemias.features.visitas.domain.model.TipoLarvicida
import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaContext
import com.alisonsfadev.endemias.ui.theme.spacing

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ContextoCard(contexto: VisitaContext) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        FlowRow(
            modifier = Modifier.padding(MaterialTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
        ) {
            listOf(
                "Quarteirão" to contexto.numeroQuarteirao,
                "Lado" to contexto.lado,
                "Rua" to contexto.logradouro,
                "Imóvel" to "${contexto.numeroImovel} · ${contexto.tipoImovel}",
                "Ciclo/ano" to contexto.cicloAno,
                "Atividade" to contexto.atividade,
            ).forEach { (label, valor) ->
                Column {
                    Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(valor, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
internal fun SecaoTitulo(texto: String) {
    Text(texto, style = MaterialTheme.typography.titleMedium)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T> DropdownCampo(
    label: String,
    valor: String,
    opcoes: List<T>,
    rotulo: (T) -> String,
    onSelecionar: (T) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    erro: String? = null,
) {
    var expandido by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expandido && enabled,
        onExpandedChange = { if (enabled) expandido = it },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = valor,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            singleLine = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido && enabled) },
            isError = erro != null,
            supportingText = erro?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled),
        )
        ExposedDropdownMenu(expanded = expandido && enabled, onDismissRequest = { expandido = false }) {
            opcoes.forEach { opcao ->
                DropdownMenuItem(
                    text = { Text(rotulo(opcao)) },
                    onClick = { onSelecionar(opcao); expandido = false },
                )
            }
        }
    }
}

/** O mesmo valor controla a digitação e os botões, inclusive durante uma edição vazia. */
@Composable
internal fun QuantidadeDepositosCampo(
    rotulo: String,
    valor: String,
    onChange: (String) -> Unit,
    enabled: Boolean,
    tag: String,
    modifier: Modifier = Modifier,
    erro: String? = null,
    rotuloAcima: Boolean = false,
) {
    val quantidade = valor.toIntOrNull()
    Column(modifier, verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)) {
        if (rotuloAcima) {
            Text(rotulo, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(
                    horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.xs,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (!rotuloAcima) {
                    Text(rotulo, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                }
                IconButton(
                    onClick = { onChange((requireNotNull(quantidade) - 1).toString()) },
                    enabled = enabled && quantidade != null && quantidade > 0,
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Diminuir ${rotulo.lowercase()}")
                }
                OutlinedTextField(
                    value = valor,
                    onValueChange = onChange,
                    enabled = enabled,
                    singleLine = true,
                    isError = erro != null,
                    textStyle = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = (if (rotuloAcima) Modifier.weight(1f) else Modifier.width(80.dp))
                        .testTag(tag).semantics { contentDescription = rotulo },
                )
                IconButton(
                    onClick = { onChange(((quantidade ?: 0) + 1).toString()) },
                    enabled = enabled && (valor.isEmpty() || (quantidade != null && quantidade < Int.MAX_VALUE)),
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Aumentar ${rotulo.lowercase()}")
                }
            }
        }
        erro?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
internal fun TratamentoBloco(state: FichaVisitaUiState, onAction: (FichaVisitaAction) -> Unit) {
    QuantidadeDepositosCampo(
        rotulo = "Depósitos eliminados",
        valor = state.depositosEliminados,
        onChange = { onAction(FichaVisitaAction.DepositosEliminadosChanged(it)) },
        enabled = state.podeEditar,
        tag = "depositos_eliminados",
        erro = state.erros[CampoFichaVisita.DEPOSITOS_ELIMINADOS],
        modifier = Modifier.fillMaxWidth(),
    )
    Text(
        text = "Imóvel tratado: ${if (state.imovelTratado) "Sim" else "Não"}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.testTag("imovel_tratado"),
    )
    SecaoTitulo("Focal · larvicida (opcional)")
    DropdownCampo(
        label = "Tipo (L1)", valor = state.tipoLarvicida?.sigla ?: "Sem larvicida",
        opcoes = listOf<TipoLarvicida?>(null) + TipoLarvicida.entries,
        rotulo = { it?.sigla ?: "Sem larvicida" },
        onSelecionar = { onAction(FichaVisitaAction.LarvicidaTipoChanged(it)) },
        enabled = state.podeEditar, erro = state.erros[CampoFichaVisita.LARVICIDA_TIPO],
        modifier = Modifier.fillMaxWidth(),
    )
    Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs),
        ) {
            Text("Qtde (carga)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedTextField(
                value = state.qntdLarvicida,
                onValueChange = { onAction(FichaVisitaAction.LarvicidaCargaChanged(it)) },
                singleLine = true, enabled = state.podeEditar,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = CampoFichaVisita.LARVICIDA_CARGA in state.erros,
                supportingText = state.erros[CampoFichaVisita.LARVICIDA_CARGA]?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth().padding(vertical = MaterialTheme.spacing.xs)
                    .testTag("carga_larvicida").semantics { contentDescription = "Qtde (carga)" },
            )
        }
        QuantidadeDepositosCampo(
            rotulo = "Depósitos tratados",
            valor = state.depositosTratados,
            onChange = { onAction(FichaVisitaAction.LarvicidaDepositosChanged(it)) },
            enabled = state.podeEditar,
            tag = "recipientes_tratados",
            erro = state.erros[CampoFichaVisita.LARVICIDA_DEPOSITOS],
            modifier = Modifier.weight(1.25f),
            rotuloAcima = true,
        )
    }
}
