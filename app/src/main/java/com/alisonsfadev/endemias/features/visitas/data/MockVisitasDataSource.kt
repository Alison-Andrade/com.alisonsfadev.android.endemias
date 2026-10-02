package com.alisonsfadev.endemias.features.visitas.data

import com.alisonsfadev.endemias.features.visitas.domain.Imovel
import com.alisonsfadev.endemias.features.visitas.domain.Quarteirao
import com.alisonsfadev.endemias.features.visitas.domain.model.StatusVisita
import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaContext
import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaTratamento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/** Cadastro de exemplo e visitas compartilhados apenas durante a execução do processo. */
@Singleton
class MockVisitasDataSource @Inject constructor() {
    private val cadastro = linkedMapOf(
        1L to listOf(
            Imovel(1, "101", "Rua das Flores", "RESIDENCIA", visitado = false),
            Imovel(2, "102", "Rua das Flores", "RESIDENCIA", visitado = true),
            Imovel(3, "S/N", "Rua das Flores", "TERRENO_BALDIO", visitado = false),
        ),
        2L to listOf(
            Imovel(4, "201", "Rua Principal", "RESIDENCIA", visitado = false),
            Imovel(5, "202", "Rua Principal", "COMERCIO", visitado = true),
            Imovel(6, "203", "Rua Principal", "RESIDENCIA", visitado = false),
        ),
        3L to listOf(
            Imovel(7, "301", "Av. Central", "RESIDENCIA", visitado = false),
            Imovel(8, "302", "Av. Central", "OUTROS", visitado = false),
            Imovel(9, "S/N", "Av. Central", "TERRENO_BALDIO", visitado = false),
        ),
    )
    private val visitas = MutableStateFlow<Map<String, VisitaTratamento>>(emptyMap())

    fun getQuarteiroes(): List<Quarteirao> = quarteiroes(visitas.value)

    fun getImoveisDoQuarteirao(quarteiraoId: Long): List<Imovel> =
        imoveis(quarteiraoId, visitas.value)

    fun observarQuarteiroes(): Flow<List<Quarteirao>> =
        visitas.map(::quarteiroes).distinctUntilChanged()

    fun observarImoveis(quarteiraoId: Long): Flow<List<Imovel>> =
        visitas.map { imoveis(quarteiraoId, it) }.distinctUntilChanged()

    fun obterContexto(imovelId: Long): VisitaContext {
        val (quarteiraoId, lista) = cadastroDoImovel(imovelId)
        val imovel = lista.first { it.id == imovelId }
        return VisitaContext(
            numeroQuarteirao = numeroQuarteirao(quarteiraoId).toString(),
            lado = "—",
            logradouro = imovel.logradouro,
            numeroImovel = imovel.numero,
            tipoImovel = imovel.tipo,
            cicloAno = "—",
        )
    }

    fun obterProximoImovelId(imovelId: Long): Long? {
        val lista = cadastroDoImovel(imovelId).value
        return lista.getOrNull(lista.indexOfFirst { it.id == imovelId } + 1)?.id
    }

    fun salvar(visita: VisitaTratamento) {
        cadastroDoImovel(visita.imovelId)
        visitas.update { atuais ->
            // Repetir uma tentativa não deve substituir a visita nem mudar sua ordem.
            if (visita.clientUuid in atuais) atuais else atuais + (visita.clientUuid to visita)
        }
    }

    private fun cadastroDoImovel(imovelId: Long): Map.Entry<Long, List<Imovel>> =
        requireNotNull(cadastro.entries.firstOrNull { (_, lista) -> lista.any { it.id == imovelId } }) {
            "Imóvel não encontrado"
        }

    private fun numeroQuarteirao(id: Long): Int = 11 + id.toInt()

    private fun imoveis(id: Long, registros: Map<String, VisitaTratamento>): List<Imovel> =
        cadastro[id].orEmpty().map { imovel ->
            val ultima = registros.values.lastOrNull { it.imovelId == imovel.id }
            if (ultima == null) imovel else imovel.copy(visitado = ultima.situacao == StatusVisita.TRABALHADO)
        }

    private fun quarteiroes(registros: Map<String, VisitaTratamento>): List<Quarteirao> =
        cadastro.map { (id, lista) ->
            val atuais = imoveis(id, registros)
            Quarteirao(
                id = id,
                numero = numeroQuarteirao(id),
                logradouroPrincipal = lista.first().logradouro,
                totalImoveis = atuais.size,
                imoveisVisitados = atuais.count { it.visitado },
                residencias = atuais.count { it.tipo == "RESIDENCIA" },
                comercios = atuais.count { it.tipo == "COMERCIO" },
                terrenosBaldios = atuais.count { it.tipo == "TERRENO_BALDIO" },
                outros = atuais.count { it.tipo !in setOf("RESIDENCIA", "COMERCIO", "TERRENO_BALDIO") },
            )
        }
}
