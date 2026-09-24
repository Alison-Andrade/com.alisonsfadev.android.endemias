package com.alisonsfadev.endemias.features.visitas.ui

import com.alisonsfadev.endemias.features.visitas.domain.model.StatusVisita

data class FichaVisitaUiState(
    val imovelId: Long = 0L,
    val enderecoImovel: String = "",

    val status: StatusVisita? = null,
    val observacao: String = "",

    val qntdEliminados: String = "",
    val qntdTratados: String = "",
    val qntdLarvicida: String = "",
    val tipoLarvicida: String = "",
    val focal: Boolean = false,

    val salvando: Boolean = false,
    val erro: String? = null,
    val sucesso: Boolean = false
) {
    val exigeTratamento: Boolean
        get() = status == StatusVisita.TRABALHADO || status == StatusVisita.RECUPERADO
}