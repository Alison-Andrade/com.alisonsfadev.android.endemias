package com.alisonsfadev.endemias.features.visitas.ui

import com.alisonsfadev.endemias.features.visitas.domain.model.StatusVisita
import com.alisonsfadev.endemias.features.visitas.domain.model.TipoLarvicida
import com.alisonsfadev.endemias.features.visitas.domain.model.TipoVisita
import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaContext
import java.util.UUID

enum class CampoFichaVisita { SITUACAO, HORA, DEPOSITOS_ELIMINADOS, LARVICIDA_TIPO, LARVICIDA_CARGA, LARVICIDA_DEPOSITOS }

data class FichaVisitaUiState(
    val imovelId: Long = 0L,
    val clientUuid: String = UUID.randomUUID().toString(),
    val contexto: VisitaContext? = null,
    val proximoImovelId: Long? = null,
    val carregando: Boolean = true,
    val status: StatusVisita? = null,
    val visitaIniciada: Boolean = false,
    val hora: String = "",
    val tipoVisita: TipoVisita = TipoVisita.NORMAL,
    val depositosEliminados: String = "0",
    val tipoLarvicida: TipoLarvicida? = null,
    val qntdLarvicida: String = "",
    val depositosTratados: String = "0",
    val erros: Map<CampoFichaVisita, String> = emptyMap(),
    val erro: String? = null,
    val salvando: Boolean = false,
    val salvo: Boolean = false,
) {
    val trabalhado: Boolean get() = status == StatusVisita.TRABALHADO
    val tratamentoComLarvicida: Boolean
        get() = trabalhado && (depositosTratados.toIntOrNull() ?: 0) > 0
    val imovelTratado: Boolean
        get() = trabalhado && ((depositosEliminados.toIntOrNull() ?: 0) > 0 || tratamentoComLarvicida)
    val larvicidaInformado: Boolean
        get() = tipoLarvicida != null ||
            (qntdLarvicida.isNotBlank() && qntdLarvicida.replace(',', '.').toDoubleOrNull() != 0.0) ||
            (depositosTratados.isNotBlank() && depositosTratados.toIntOrNull() != 0)
    val horaVisivel: Boolean get() = visitaIniciada && status != null && status != StatusVisita.FECHADO
    val podeEditar: Boolean get() = !carregando && contexto != null && !salvando && !salvo
}

sealed interface FichaVisitaAction {
    data class SituacaoChanged(val valor: StatusVisita) : FichaVisitaAction
    data class HoraChanged(val valor: String) : FichaVisitaAction
    data class TipoVisitaChanged(val valor: TipoVisita) : FichaVisitaAction
    data class DepositosEliminadosChanged(val valor: String) : FichaVisitaAction
    data class LarvicidaTipoChanged(val valor: TipoLarvicida?) : FichaVisitaAction
    data class LarvicidaCargaChanged(val valor: String) : FichaVisitaAction
    data class LarvicidaDepositosChanged(val valor: String) : FichaVisitaAction
    data class Salvar(val irParaProximo: Boolean) : FichaVisitaAction
    data object RecarregarContexto : FichaVisitaAction
}

sealed interface FichaVisitaEvento {
    data class Salvo(val proximoImovelId: Long?) : FichaVisitaEvento
}
