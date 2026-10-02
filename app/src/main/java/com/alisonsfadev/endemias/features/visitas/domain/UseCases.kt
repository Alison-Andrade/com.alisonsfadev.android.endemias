package com.alisonsfadev.endemias.features.visitas.domain

import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaContext
import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaTratamento
import javax.inject.Inject

class ObterVisitaContextUseCase @Inject constructor(
    private val repository: VisitaTratamentoRepository
) {
    suspend operator fun invoke(imovelId: Long): VisitaContext = repository.obterContexto(imovelId)
}

class ObterProximoImovelUseCase @Inject constructor(
    private val repository: VisitaTratamentoRepository
) {
    suspend operator fun invoke(imovelId: Long): Long? = repository.obterProximoImovelId(imovelId)
}

class SalvarTratamentoUseCase @Inject constructor(
    private val repository: VisitaTratamentoRepository
) {
    suspend operator fun invoke(visita: VisitaTratamento) = repository.salvar(visita)
}
