package com.alisonsfadev.endemias.features.visitas.data

import com.alisonsfadev.endemias.features.visitas.domain.VisitaTratamentoRepository
import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaContext
import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaTratamento
import javax.inject.Inject

class VisitaTratamentoRepositoryImpl @Inject constructor(
    private val dataSource: MockVisitasDataSource
) : VisitaTratamentoRepository {
    override suspend fun obterContexto(imovelId: Long): VisitaContext = dataSource.obterContexto(imovelId)

    override suspend fun obterProximoImovelId(imovelId: Long): Long? =
        dataSource.obterProximoImovelId(imovelId)

    override suspend fun salvar(visita: VisitaTratamento) {
        dataSource.salvar(visita)
    }
}
