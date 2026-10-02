package com.alisonsfadev.endemias.features.visitas.domain.model

import java.time.Instant
import java.time.LocalTime

enum class TipoVisita(val sigla: String, val label: String) {
    NORMAL("N", "N (normal)"),
    RECUPERADO("R", "R (recuperado)"),
}

enum class StatusVisita(val label: String) {
    TRABALHADO("Trabalhado"),
    FECHADO("Fechado"),
    RECUSADO("Recusado"),
}

enum class TipoLarvicida(val sigla: String) {
    B("B")
}

data class VisitaContext(
    val numeroQuarteirao: String,
    val lado: String,
    val logradouro: String,
    val numeroImovel: String,
    val tipoImovel: String,
    val cicloAno: String,
    val atividade: String = "T"
)

data class VisitaTratamento(
    val clientUuid: String,
    val imovelId: Long,
    val tipoVisita: TipoVisita,
    val situacao: StatusVisita,
    val hora: LocalTime?,
    val registradoEm: Instant,
    val depositosEliminados: Int,
    val imovelTratado: Boolean,
    val tipoLarvicida: TipoLarvicida?,
    val qntdLarvicida: Double?,
    val depositosTratados: Int?,
)
