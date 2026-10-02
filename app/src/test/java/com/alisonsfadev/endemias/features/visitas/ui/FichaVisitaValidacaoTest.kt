package com.alisonsfadev.endemias.features.visitas.ui

import com.alisonsfadev.endemias.features.visitas.domain.model.StatusVisita
import com.alisonsfadev.endemias.features.visitas.domain.model.TipoLarvicida
import org.junit.Assert.*
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneOffset

class FichaVisitaValidacaoTest {
    private val clock = Clock.fixed(Instant.parse("2026-10-01T12:00:00Z"), ZoneOffset.ofHours(-3))
    private val tratada = FichaVisitaUiState(
        imovelId = 1, status = StatusVisita.TRABALHADO, visitaIniciada = true,
        hora = "08:42", tipoLarvicida = TipoLarvicida.B,
        qntdLarvicida = "1,5", depositosTratados = "2", depositosEliminados = "3",
    )

    @Test fun `ficha inicial exige uma situacao`() {
        val initial = FichaVisitaUiState()
        assertNull(initial.status)
        assertEquals("", initial.hora)
        assertFalse(initial.horaVisivel)
        assertFalse(initial.trabalhado)
        assertEquals(setOf(CampoFichaVisita.SITUACAO), validarFichaVisita(initial).keys)
    }

    @Test fun `horario aceita apenas HH mm valido`() {
        listOf("", "8:42", "24:00", "12:60", "abcde", "08:42:00").forEach { hora ->
            assertTrue(hora, CampoFichaVisita.HORA in validarFichaVisita(tratada.copy(hora = hora)))
        }
        listOf("00:00", "08:42", "23:59").forEach { hora ->
            assertFalse(hora, CampoFichaVisita.HORA in validarFichaVisita(tratada.copy(hora = hora)))
        }
    }

    @Test fun `larvicida informado exige tipo carga finita positiva e recipientes positivos`() {
        val empty = tratada.copy(tipoLarvicida = null, qntdLarvicida = "", depositosTratados = "")
        assertTrue(validarFichaVisita(empty).isEmpty())
        assertEquals(2, validarFichaVisita(empty.copy(qntdLarvicida = "1")).size)
        listOf("0", "-1", "NaN", "Infinity", "1e999", "abc").forEach { carga ->
            assertTrue(carga, CampoFichaVisita.LARVICIDA_CARGA in validarFichaVisita(tratada.copy(qntdLarvicida = carga)))
        }
        listOf("0", "-1", "1,5", "2147483648", "abc").forEach { depositos ->
            assertTrue(depositos, CampoFichaVisita.LARVICIDA_DEPOSITOS in validarFichaVisita(tratada.copy(depositosTratados = depositos)))
        }
        listOf("1,5", "1.5").forEach { assertTrue(validarFichaVisita(tratada.copy(qntdLarvicida = it)).isEmpty()) }
    }

    @Test fun `trabalhado sem iniciar nao pode ser mapeado`() {
        val state = tratada.copy(visitaIniciada = false)
        assertTrue(CampoFichaVisita.HORA in validarFichaVisita(state))
        assertThrows(IllegalArgumentException::class.java) { state.toDomain(clock) }
    }

    @Test fun `fechado e recusado diretos nao exigem horario nem tratamento`() {
        listOf(StatusVisita.FECHADO, StatusVisita.RECUSADO).forEach { status ->
            val state = FichaVisitaUiState(imovelId = 1, status = status)
            assertTrue(validarFichaVisita(state).isEmpty())
            assertNull(state.toDomain(clock).hora)
        }
    }

    @Test fun `recusado depois de iniciar exige horario valido`() {
        assertTrue(CampoFichaVisita.HORA in validarFichaVisita(tratada.copy(status = StatusVisita.RECUSADO, hora = "")))
        assertEquals(LocalTime.of(8, 42), tratada.copy(status = StatusVisita.RECUSADO).toDomain(clock).hora)
    }

    @Test fun `mapper preserva uuid e separa horario inicial do instante de salvamento`() {
        val domain = tratada.toDomain(clock)
        assertEquals(tratada.clientUuid, domain.clientUuid)
        assertEquals(1L, domain.imovelId)
        assertEquals(LocalTime.of(8, 42), domain.hora)
        assertEquals(clock.instant(), domain.registradoEm)
        assertEquals(1.5, requireNotNull(domain.qntdLarvicida), 0.0)
        assertEquals(2, domain.depositosTratados)
        assertEquals(3, domain.depositosEliminados)
    }

    @Test fun `mapper anula tratamento ao fechar ou recusar mesmo com valores preenchidos`() {
        listOf(StatusVisita.FECHADO, StatusVisita.RECUSADO).forEach { status ->
            val domain = tratada.copy(status = status).toDomain(clock)
            assertFalse(domain.imovelTratado)
            assertEquals(0, domain.depositosEliminados)
            assertNull(domain.tipoLarvicida)
            assertNull(domain.qntdLarvicida)
            assertNull(domain.depositosTratados)
            if (status == StatusVisita.FECHADO) assertNull(domain.hora)
        }
    }

    @Test fun `somente eliminados considera imovel tratado e dispensa larvicida`() {
        val state = tratada.copy(tipoLarvicida = null, qntdLarvicida = "", depositosTratados = "")
        assertTrue(validarFichaVisita(state).isEmpty())
        val domain = state.toDomain(clock)
        assertEquals(3, domain.depositosEliminados)
        assertTrue(state.imovelTratado)
        assertTrue(domain.imovelTratado)
        assertNull(domain.tipoLarvicida)
        assertNull(domain.qntdLarvicida)
        assertNull(domain.depositosTratados)
    }

    @Test fun `somente recipientes com larvicida considera imovel tratado`() {
        val state = tratada.copy(depositosEliminados = "0")
        assertTrue(state.imovelTratado)
        val domain = state.toDomain(clock)
        assertTrue(domain.imovelTratado)
        assertEquals(0, domain.depositosEliminados)
        assertEquals(2, domain.depositosTratados)
        assertEquals(TipoLarvicida.B, domain.tipoLarvicida)
    }

    @Test fun `sem eliminados nem larvicida salva como nao tratado`() {
        val state = tratada.copy(depositosEliminados = "0", tipoLarvicida = null, qntdLarvicida = "", depositosTratados = "")
        assertFalse(state.imovelTratado)
        assertTrue(validarFichaVisita(state).isEmpty())
        assertFalse(state.toDomain(clock).imovelTratado)
        assertNull(state.toDomain(clock).depositosTratados)
    }

    @Test fun `quantidades zeradas sem tipo nao indicam aplicacao de larvicida`() {
        val state = tratada.copy(depositosEliminados = "0", tipoLarvicida = null, qntdLarvicida = "0,0", depositosTratados = "0")
        assertTrue(validarFichaVisita(state).isEmpty())
        assertFalse(state.toDomain(clock).imovelTratado)
        assertNull(state.toDomain(clock).qntdLarvicida)
    }

    @Test fun `eliminados nao dispensam validar dados parciais de larvicida`() {
        val state = tratada.copy(tipoLarvicida = null, qntdLarvicida = "", depositosTratados = "1")
        assertTrue(state.imovelTratado)
        assertEquals(setOf(CampoFichaVisita.LARVICIDA_TIPO, CampoFichaVisita.LARVICIDA_CARGA), validarFichaVisita(state).keys)
        assertThrows(IllegalArgumentException::class.java) { state.toDomain(clock) }
    }

    @Test fun `fechado e recusado nao ficam tratados mesmo com quantidades no formulario`() {
        listOf(StatusVisita.FECHADO, StatusVisita.RECUSADO).forEach { status ->
            assertFalse(tratada.copy(status = status).imovelTratado)
        }
    }


    @Test fun `eliminados aceita vazio zero e limite inteiro mas rejeita quantidade invalida`() {
        val base = tratada.copy(tipoLarvicida = null, qntdLarvicida = "", depositosTratados = "0")
        listOf("", "0", "12", Int.MAX_VALUE.toString()).forEach { quantidade ->
            val state = base.copy(depositosEliminados = quantidade)
            assertTrue(quantidade, validarFichaVisita(state).isEmpty())
            assertEquals(quantidade.toIntOrNull() ?: 0, state.toDomain(clock).depositosEliminados)
        }
        listOf("-1", "abc", "1,5", "2147483648").forEach { quantidade ->
            val state = base.copy(depositosEliminados = quantidade)
            assertTrue(quantidade, CampoFichaVisita.DEPOSITOS_ELIMINADOS in validarFichaVisita(state))
            assertThrows(IllegalArgumentException::class.java) { state.toDomain(clock) }
        }
    }

    @Test fun `apagar ambas as quantidades equivale a nenhum tratamento`() {
        val state = tratada.copy(tipoLarvicida = null, qntdLarvicida = "", depositosEliminados = "", depositosTratados = "")
        val visita = state.toDomain(clock)
        assertFalse(visita.imovelTratado)
        assertEquals(0, visita.depositosEliminados)
        assertNull(visita.depositosTratados)
    }

}
