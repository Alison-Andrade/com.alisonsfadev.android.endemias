package com.alisonsfadev.endemias.features.visitas.domain

import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaContext
import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaTratamento

interface VisitaTratamentoRepository {
    suspend fun obterContexto(imovelId: Long): VisitaContext
    suspend fun obterProximoImovelId(imovelId: Long): Long?
    suspend fun salvar(visita: VisitaTratamento)
}
