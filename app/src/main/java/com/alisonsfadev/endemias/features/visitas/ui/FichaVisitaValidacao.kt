package com.alisonsfadev.endemias.features.visitas.ui

private val REGEX_HORA = Regex("([01][0-9]|2[0-3]):[0-5][0-9]")

fun validarFichaVisita(state: FichaVisitaUiState): Map<CampoFichaVisita, String> = buildMap {
    if (state.status == null) put(CampoFichaVisita.SITUACAO, "Selecione a situação do imóvel")
    if (state.horaVisivel && !REGEX_HORA.matches(state.hora)) {
        put(CampoFichaVisita.HORA, "Use o formato HH:mm")
    }
    if (state.trabalhado && !state.visitaIniciada) {
        put(CampoFichaVisita.HORA, "Inicie a visita selecionando Trabalhado")
    }
    if (state.trabalhado && state.depositosEliminados.isNotBlank()) {
        val eliminados = state.depositosEliminados.toIntOrNull()
        if (eliminados == null || eliminados < 0) {
            put(CampoFichaVisita.DEPOSITOS_ELIMINADOS, "Informe uma quantidade entre 0 e ${Int.MAX_VALUE}")
        }
    }
    if (state.trabalhado && state.larvicidaInformado) {
        if (state.tipoLarvicida == null) put(CampoFichaVisita.LARVICIDA_TIPO, "Selecione o larvicida")
        val carga = state.qntdLarvicida.replace(',', '.').toDoubleOrNull()
        if (carga == null || !carga.isFinite() || carga <= 0.0) {
            put(CampoFichaVisita.LARVICIDA_CARGA, "Informe uma carga maior que zero")
        }
        val depositos = state.depositosTratados.toIntOrNull()
        if (depositos == null || depositos <= 0) {
            put(CampoFichaVisita.LARVICIDA_DEPOSITOS, "Informe os depósitos tratados")
        }
    }
}
