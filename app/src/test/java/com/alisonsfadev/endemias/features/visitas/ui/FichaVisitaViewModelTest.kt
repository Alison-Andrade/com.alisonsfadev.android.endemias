package com.alisonsfadev.endemias.features.visitas.ui

import androidx.lifecycle.SavedStateHandle
import com.alisonsfadev.endemias.core.navigation.EndemiasScreens
import com.alisonsfadev.endemias.features.visitas.domain.ObterProximoImovelUseCase
import com.alisonsfadev.endemias.features.visitas.domain.ObterVisitaContextUseCase
import com.alisonsfadev.endemias.features.visitas.domain.SalvarTratamentoUseCase
import com.alisonsfadev.endemias.features.visitas.domain.VisitaTratamentoRepository
import com.alisonsfadev.endemias.features.visitas.domain.model.StatusVisita
import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaContext
import com.alisonsfadev.endemias.features.visitas.domain.model.VisitaTratamento
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalCoroutinesApi::class)
class FichaVisitaViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val repository = FakeRepository()
    private val clock = MutableClock()

    @Before fun setUp() { Dispatchers.setMain(dispatcher) }
    @After fun tearDown() { Dispatchers.resetMain() }

    private fun criarViewModel(id: Long = 1) = FichaVisitaViewModel(
        SavedStateHandle(mapOf(EndemiasScreens.ARG_IMOVEL_ID to id)),
        ObterVisitaContextUseCase(repository), ObterProximoImovelUseCase(repository),
        SalvarTratamentoUseCase(repository), clock,
    )

    @Test fun `carregar contexto nao inicia visita`() = runTest {
        val vm = criarViewModel()
        assertTrue(vm.uiState.value.carregando)
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.TRABALHADO))
        assertNull(vm.uiState.value.status)
        advanceUntilIdle()
        assertFalse(vm.uiState.value.carregando)
        assertNotNull(vm.uiState.value.contexto)
        assertEquals(2L, vm.uiState.value.proximoImovelId)
        assertNull(vm.uiState.value.status)
        assertFalse(vm.uiState.value.visitaIniciada)
        assertEquals("", vm.uiState.value.hora)
    }

    @Test fun `primeiro clique em trabalhado carimba horario e transicoes preservam inicio`() = runTest {
        val vm = criarViewModel()
        advanceUntilIdle()
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.TRABALHADO))
        assertEquals("09:00", vm.uiState.value.hora)
        assertTrue(vm.uiState.value.visitaIniciada)
        clock.now = Instant.parse("2026-10-01T13:00:00Z")
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.TRABALHADO))
        assertEquals("09:00", vm.uiState.value.hora)
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.FECHADO))
        assertFalse(vm.uiState.value.horaVisivel)
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.RECUSADO))
        assertTrue(vm.uiState.value.horaVisivel)
        assertEquals("09:00", vm.uiState.value.hora)
        vm.onAction(FichaVisitaAction.HoraChanged("08:15"))
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.TRABALHADO))
        assertEquals("08:15", vm.uiState.value.hora)
    }

    @Test fun `fechado e recusado diretos salvam sem iniciar`() = runTest {
        for (status in listOf(StatusVisita.FECHADO, StatusVisita.RECUSADO)) {
            val vm = criarViewModel()
            advanceUntilIdle()
            vm.onAction(FichaVisitaAction.SituacaoChanged(status))
            vm.onAction(FichaVisitaAction.HoraChanged("09:00"))
            assertEquals("", vm.uiState.value.hora)
            assertFalse(vm.uiState.value.visitaIniciada)
            vm.onAction(FichaVisitaAction.Salvar(false))
            advanceUntilIdle()
            assertNull(repository.saved.last().hora)
            assertEquals(status, repository.saved.last().situacao)
            assertEquals(FichaVisitaEvento.Salvo(null), vm.eventos.first())
        }
    }

    @Test fun `sem situacao ou tratamento invalido nao envia nem navega`() = runTest {
        val vm = criarViewModel()
        advanceUntilIdle()
        vm.onAction(FichaVisitaAction.Salvar(false))
        assertTrue(CampoFichaVisita.SITUACAO in vm.uiState.value.erros)
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.TRABALHADO))
        assertTrue(vm.uiState.value.erros.isEmpty())
        vm.onAction(FichaVisitaAction.LarvicidaDepositosChanged("1"))
        vm.onAction(FichaVisitaAction.Salvar(false))
        advanceUntilIdle()
        assertEquals(2, vm.uiState.value.erros.size)
        assertTrue(repository.attempts.isEmpty())
        assertFalse(vm.uiState.value.salvo)
    }

    @Test fun `carregamento com falha permite tentar novamente sem iniciar`() = runTest {
        repository.contextFailure = IllegalStateException("offline")
        val vm = criarViewModel()
        advanceUntilIdle()
        assertNotNull(vm.uiState.value.erro)
        assertFalse(vm.uiState.value.podeEditar)
        vm.onAction(FichaVisitaAction.Salvar(false))
        assertTrue(repository.attempts.isEmpty())
        repository.contextFailure = null
        vm.onAction(FichaVisitaAction.RecarregarContexto)
        assertTrue(vm.uiState.value.carregando)
        advanceUntilIdle()
        assertNull(vm.uiState.value.erro)
        assertTrue(vm.uiState.value.podeEditar)
        assertNull(vm.uiState.value.status)
    }

    @Test fun `falha ao salvar preserva valores e uuid para nova tentativa`() = runTest {
        val vm = criarViewModel()
        advanceUntilIdle()
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.TRABALHADO))
        vm.onAction(FichaVisitaAction.DepositosEliminadosChanged("3"))
        repository.saveFailure = IllegalStateException("falha")
        vm.onAction(FichaVisitaAction.Salvar(true))
        advanceUntilIdle()
        assertFalse(vm.uiState.value.salvando)
        assertFalse(vm.uiState.value.salvo)
        assertNotNull(vm.uiState.value.erro)
        assertEquals("3", vm.uiState.value.depositosEliminados)
        assertEquals("09:00", vm.uiState.value.hora)
        repository.saveFailure = null
        clock.now = Instant.parse("2026-10-01T12:10:00Z")
        vm.onAction(FichaVisitaAction.Salvar(true))
        advanceUntilIdle()
        assertEquals(repository.attempts.first().clientUuid, repository.saved.single().clientUuid)
        assertEquals(clock.instant(), repository.saved.single().registradoEm)
        assertEquals(FichaVisitaEvento.Salvo(2L), vm.eventos.first())
    }

    @Test fun `envio bloqueia edicao repeticao e evento so chega apos salvar`() = runTest {
        val vm = criarViewModel()
        advanceUntilIdle()
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.RECUSADO))
        repository.gate = CompletableDeferred()
        val evento = async { vm.eventos.first() }
        vm.onAction(FichaVisitaAction.Salvar(true))
        assertTrue(vm.uiState.value.salvando)
        vm.onAction(FichaVisitaAction.Salvar(true))
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.TRABALHADO))
        advanceUntilIdle()
        assertEquals(1, repository.attempts.size)
        assertEquals(StatusVisita.RECUSADO, vm.uiState.value.status)
        assertFalse(evento.isCompleted)
        repository.gate?.complete(Unit)
        advanceUntilIdle()
        assertEquals(FichaVisitaEvento.Salvo(2L), evento.await())
        vm.onAction(FichaVisitaAction.Salvar(true))
        advanceUntilIdle()
        assertEquals(1, repository.attempts.size)
    }

    @Test fun `ultimo imovel volta a lista e nova ficha abre vazia com outro uuid`() = runTest {
        repository.next = null
        val vm = criarViewModel(3)
        advanceUntilIdle()
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.FECHADO))
        vm.onAction(FichaVisitaAction.Salvar(true))
        advanceUntilIdle()
        assertEquals(FichaVisitaEvento.Salvo(null), vm.eventos.first())
        val nova = criarViewModel(4)
        advanceUntilIdle()
        assertNotEquals(vm.uiState.value.clientUuid, nova.uiState.value.clientUuid)
        assertNull(nova.uiState.value.status)
        assertEquals("", nova.uiState.value.hora)
    }

    @Test fun `cancelamento nao vira erro de carregamento nem salvamento`() = runTest {
        repository.contextFailure = CancellationException("cancelado")
        val cancelada = criarViewModel()
        advanceUntilIdle()
        assertNull(cancelada.uiState.value.erro)
        assertNull(cancelada.uiState.value.contexto)
        repository.contextFailure = null
        val vm = criarViewModel()
        advanceUntilIdle()
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.FECHADO))
        repository.saveFailure = CancellationException("cancelado")
        vm.onAction(FichaVisitaAction.Salvar(false))
        advanceUntilIdle()
        assertNull(vm.uiState.value.erro)
        assertFalse(vm.uiState.value.salvando)
        assertFalse(vm.uiState.value.salvo)
        assertTrue(repository.saved.isEmpty())
    }

    @Test fun `tratamento acompanha eliminados e recipientes sem acao manual`() = runTest {
        val vm = criarViewModel()
        advanceUntilIdle()
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.TRABALHADO))
        assertFalse(vm.uiState.value.imovelTratado)
        vm.onAction(FichaVisitaAction.DepositosEliminadosChanged("1"))
        assertTrue(vm.uiState.value.imovelTratado)
        vm.onAction(FichaVisitaAction.DepositosEliminadosChanged("0"))
        assertFalse(vm.uiState.value.imovelTratado)
        vm.onAction(FichaVisitaAction.LarvicidaDepositosChanged("2"))
        assertTrue(vm.uiState.value.imovelTratado)
        vm.onAction(FichaVisitaAction.LarvicidaDepositosChanged("0"))
        assertFalse(vm.uiState.value.imovelTratado)
        vm.onAction(FichaVisitaAction.Salvar(false))
        advanceUntilIdle()
        assertFalse(repository.saved.single().imovelTratado)
        assertNull(repository.saved.single().tipoLarvicida)
    }

    @Test fun `quantidades digitadas atualizam tratamento e sao persistidas como inteiros`() = runTest {
        val vm = criarViewModel()
        advanceUntilIdle()
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.TRABALHADO))
        vm.onAction(FichaVisitaAction.DepositosEliminadosChanged("12"))
        assertEquals("12", vm.uiState.value.depositosEliminados)
        assertTrue(vm.uiState.value.imovelTratado)
        vm.onAction(FichaVisitaAction.DepositosEliminadosChanged(""))
        assertFalse(vm.uiState.value.imovelTratado)
        vm.onAction(FichaVisitaAction.DepositosEliminadosChanged("4"))
        vm.onAction(FichaVisitaAction.LarvicidaDepositosChanged("7"))
        vm.onAction(FichaVisitaAction.LarvicidaTipoChanged(com.alisonsfadev.endemias.features.visitas.domain.model.TipoLarvicida.B))
        vm.onAction(FichaVisitaAction.LarvicidaCargaChanged("1,5"))
        vm.onAction(FichaVisitaAction.Salvar(false))
        advanceUntilIdle()
        assertEquals(4, repository.saved.single().depositosEliminados)
        assertEquals(7, repository.saved.single().depositosTratados)
        assertTrue(repository.saved.single().imovelTratado)
    }

    @Test fun `entrada numerica filtra caracteres e quantidade acima do limite bloqueia salvamento`() = runTest {
        val vm = criarViewModel()
        advanceUntilIdle()
        vm.onAction(FichaVisitaAction.SituacaoChanged(StatusVisita.TRABALHADO))
        vm.onAction(FichaVisitaAction.DepositosEliminadosChanged("abc12"))
        assertEquals("12", vm.uiState.value.depositosEliminados)
        vm.onAction(FichaVisitaAction.LarvicidaDepositosChanged("abc7"))
        assertEquals("7", vm.uiState.value.depositosTratados)
        vm.onAction(FichaVisitaAction.LarvicidaDepositosChanged("0"))
        vm.onAction(FichaVisitaAction.DepositosEliminadosChanged("2147483648"))
        vm.onAction(FichaVisitaAction.Salvar(false))
        advanceUntilIdle()
        assertTrue(CampoFichaVisita.DEPOSITOS_ELIMINADOS in vm.uiState.value.erros)
        assertTrue(repository.attempts.isEmpty())
        vm.onAction(FichaVisitaAction.DepositosEliminadosChanged("0"))
        assertFalse(CampoFichaVisita.DEPOSITOS_ELIMINADOS in vm.uiState.value.erros)
        vm.onAction(FichaVisitaAction.Salvar(false))
        advanceUntilIdle()
        assertEquals(0, repository.saved.single().depositosEliminados)
        assertFalse(repository.saved.single().imovelTratado)
    }

    private class MutableClock(var now: Instant = Instant.parse("2026-10-01T12:00:00Z")) : Clock() {
        override fun instant(): Instant = now
        override fun getZone(): ZoneId = ZoneOffset.ofHours(-3)
        override fun withZone(zone: ZoneId): Clock = fixed(now, zone)
    }

    private class FakeRepository : VisitaTratamentoRepository {
        var contextFailure: Exception? = null
        var saveFailure: Exception? = null
        var gate: CompletableDeferred<Unit>? = null
        var next: Long? = 2
        val attempts = mutableListOf<VisitaTratamento>()
        val saved = mutableListOf<VisitaTratamento>()
        override suspend fun obterContexto(imovelId: Long): VisitaContext {
            contextFailure?.let { throw it }
            return VisitaContext("12", "—", "Rua das Flores", "101", "RESIDENCIA", "—")
        }
        override suspend fun obterProximoImovelId(imovelId: Long): Long? = next
        override suspend fun salvar(visita: VisitaTratamento) {
            attempts += visita
            gate?.await()
            saveFailure?.let { throw it }
            saved += visita
        }
    }
}
