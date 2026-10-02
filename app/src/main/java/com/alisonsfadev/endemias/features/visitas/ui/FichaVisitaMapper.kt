package com.alisonsfadev.endemias.features.visitas.ui

import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaTratamento
import java.time.Clock
import java.time.Instant
import java.time.LocalTime

fun FichaVisitaUiState.toDomain(clock: Clock): VisitaTratamento {
    require(validarFichaVisita(this).isEmpty()) { "Ficha de visita inválida" }
    val comLarvicida = tratamentoComLarvicida
    return VisitaTratamento(
        clientUuid = clientUuid,
        imovelId = imovelId,
        tipoVisita = tipoVisita,
        situacao = requireNotNull(status),
        hora = if (horaVisivel) LocalTime.parse(hora) else null,
        registradoEm = Instant.now(clock),
        depositosEliminados = if (trabalhado) depositosEliminados.toIntOrNull() ?: 0 else 0,
        imovelTratado = imovelTratado,
        tipoLarvicida = if (comLarvicida) tipoLarvicida else null,
        qntdLarvicida = if (comLarvicida) qntdLarvicida.replace(',', '.').toDouble() else null,
        depositosTratados = if (comLarvicida) depositosTratados.toInt() else null,
    )
}
