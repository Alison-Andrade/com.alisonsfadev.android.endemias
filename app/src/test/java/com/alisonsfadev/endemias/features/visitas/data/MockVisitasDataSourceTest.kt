package com.alisonsfadev.endemias.features.visitas.data

import com.alisonsfadev.endemias.features.visitas.domain.model.StatusVisita
import com.alisonsfadev.endemias.features.visitas.domain.model.TipoVisita
import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaTratamento
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class MockVisitasDataSourceTest {
    private val source = MockVisitasDataSource()
    private fun visita(uuid: String, status: StatusVisita, id: Long = 1) = VisitaTratamento(
        uuid, id, TipoVisita.NORMAL, status, null, Instant.parse("2026-10-01T12:00:00Z"),
        0, false, null, null, null,
    )

    @Test fun `cadastro tem ids unicos enderecos e totais consistentes`() {
        val quarteiroes = source.getQuarteiroes()
        val ids = quarteiroes.flatMap { source.getImoveisDoQuarteirao(it.id).map { imovel -> imovel.id } }
        assertEquals(ids.size, ids.toSet().size)
        quarteiroes.forEach { q ->
            val imoveis = source.getImoveisDoQuarteirao(q.id)
            assertEquals(imoveis.size, q.totalImoveis)
            assertEquals(imoveis.size, q.residencias + q.comercios + q.terrenosBaldios + q.outros)
            assertEquals(imoveis.count { it.visitado }, q.imoveisVisitados)
            imoveis.forEach { imovel ->
                val ctx = source.obterContexto(imovel.id)
                assertEquals(q.numero.toString(), ctx.numeroQuarteirao)
                assertEquals(imovel.logradouro, ctx.logradouro)
                assertEquals(imovel.numero, ctx.numeroImovel)
                assertEquals("—", ctx.lado)
                assertEquals("—", ctx.cicloAno)
                assertEquals("T", ctx.atividade)
            }
        }
    }

    @Test fun `avanco segue lista inclusive visitados e nunca troca quarteirao`() {
        assertTrue(source.getImoveisDoQuarteirao(1)[1].visitado)
        assertEquals(2L, source.obterProximoImovelId(1))
        assertEquals(3L, source.obterProximoImovelId(2))
        listOf(3L, 6L, 9L).forEach { assertNull(source.obterProximoImovelId(it)) }
        assertEquals(5L, source.obterProximoImovelId(4))
    }

    @Test fun `ultima visita define conclusao e tentativa repetida nao sobrescreve`() {
        source.salvar(visita("a", StatusVisita.TRABALHADO))
        assertTrue(source.getImoveisDoQuarteirao(1).first().visitado)
        source.salvar(visita("b", StatusVisita.FECHADO))
        assertFalse(source.getImoveisDoQuarteirao(1).first().visitado)
        // Nem alterar o conteúdo nem repetir uma tentativa antiga pode mudar a última visita.
        source.salvar(visita("b", StatusVisita.TRABALHADO))
        source.salvar(visita("a", StatusVisita.TRABALHADO))
        assertFalse(source.getImoveisDoQuarteirao(1).first().visitado)
        source.salvar(visita("c", StatusVisita.TRABALHADO))
        assertTrue(source.getImoveisDoQuarteirao(1).first().visitado)
        source.salvar(visita("d", StatusVisita.RECUSADO))
        assertFalse(source.getImoveisDoQuarteirao(1).first().visitado)
    }

    @Test fun `fluxos atualizam imoveis e contadores sem afetar outro quarteirao`() = runTest {
        val listas = mutableListOf<List<com.alisonsfadev.endemias.features.visitas.domain.Imovel>>()
        val totais = mutableListOf<List<com.alisonsfadev.endemias.features.visitas.domain.Quarteirao>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { source.observarImoveis(1).collect { listas += it } }
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { source.observarQuarteiroes().collect { totais += it } }
        val outro = source.getImoveisDoQuarteirao(2)
        source.salvar(visita("a", StatusVisita.TRABALHADO))
        assertTrue(listas.last().first().visitado)
        assertEquals(2, totais.last().first().imoveisVisitados)
        assertEquals(outro, source.getImoveisDoQuarteirao(2))
        source.salvar(visita("b", StatusVisita.RECUSADO))
        assertFalse(listas.last().first().visitado)
        assertEquals(1, totais.last().first().imoveisVisitados)
    }

    @Test fun `imovel inexistente gera erro e nao altera cadastro`() {
        val before = source.getQuarteiroes()
        assertThrows(IllegalArgumentException::class.java) { source.obterContexto(999) }
        assertThrows(IllegalArgumentException::class.java) { source.obterProximoImovelId(999) }
        assertThrows(IllegalArgumentException::class.java) { source.salvar(visita("a", StatusVisita.FECHADO, 999)) }
        assertEquals(before, source.getQuarteiroes())
    }

    @Test fun `nova fonte inicia sem as visitas da instancia anterior`() {
        source.salvar(visita("a", StatusVisita.TRABALHADO))
        assertFalse(MockVisitasDataSource().getImoveisDoQuarteirao(1).first().visitado)
    }
}
