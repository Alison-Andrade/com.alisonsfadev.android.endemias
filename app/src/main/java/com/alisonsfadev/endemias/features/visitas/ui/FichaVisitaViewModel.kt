package com.alisonsfadev.endemias.features.visitas.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alisonsfadev.endemias.core.navigation.EndemiasScreens
import com.alisonsfadev.endemias.features.visitas.domain.ObterProximoImovelUseCase
import com.alisonsfadev.endemias.features.visitas.domain.ObterVisitaContextUseCase
import com.alisonsfadev.endemias.features.visitas.domain.SalvarTratamentoUseCase
import com.alisonsfadev.endemias.features.visitas.domain.model.StatusVisita
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FichaVisitaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val obterContexto: ObterVisitaContextUseCase,
    private val obterProximoImovel: ObterProximoImovelUseCase,
    private val salvarTratamento: SalvarTratamentoUseCase,
    private val clock: Clock,
) : ViewModel() {
    private val imovelId: Long = checkNotNull(savedStateHandle[EndemiasScreens.ARG_IMOVEL_ID])
    private val _uiState = MutableStateFlow(FichaVisitaUiState(imovelId = imovelId))
    val uiState = _uiState.asStateFlow()
    private val _eventos = Channel<FichaVisitaEvento>(Channel.BUFFERED)
    val eventos = _eventos.receiveAsFlow()

    init { carregarContexto() }

    fun onAction(action: FichaVisitaAction) {
        if (action == FichaVisitaAction.RecarregarContexto) {
            if (!_uiState.value.carregando && !_uiState.value.salvando && !_uiState.value.salvo) carregarContexto()
            return
        }
        if (!_uiState.value.podeEditar) return
        when (action) {
            is FichaVisitaAction.SituacaoChanged -> _uiState.update { atual ->
                val iniciar = action.valor == StatusVisita.TRABALHADO && !atual.visitaIniciada
                atual.copy(
                    status = action.valor,
                    visitaIniciada = atual.visitaIniciada || iniciar,
                    hora = if (iniciar) LocalTime.now(clock).format(FORMATO_HORA) else atual.hora,
                    erros = emptyMap(),
                    erro = null,
                )
            }
            is FichaVisitaAction.HoraChanged -> if (_uiState.value.horaVisivel) {
                _uiState.update { it.copy(hora = action.valor.take(5)).semErro(CampoFichaVisita.HORA) }
            }
            is FichaVisitaAction.TipoVisitaChanged -> _uiState.update { it.copy(tipoVisita = action.valor) }
            is FichaVisitaAction.DepositosEliminadosChanged -> _uiState.update {
                it.copy(depositosEliminados = action.valor.filter { c -> c in '0'..'9' })
                    .semErro(CampoFichaVisita.DEPOSITOS_ELIMINADOS)
            }
            is FichaVisitaAction.LarvicidaTipoChanged ->
                _uiState.update { it.copy(tipoLarvicida = action.valor).semErro(CampoFichaVisita.LARVICIDA_TIPO) }
            is FichaVisitaAction.LarvicidaCargaChanged -> _uiState.update {
                it.copy(qntdLarvicida = action.valor.filtrarDecimal()).semErro(CampoFichaVisita.LARVICIDA_CARGA)
            }
            is FichaVisitaAction.LarvicidaDepositosChanged -> _uiState.update {
                it.copy(depositosTratados = action.valor.filter { c -> c in '0'..'9' })
                    .semErro(CampoFichaVisita.LARVICIDA_DEPOSITOS)
            }
            is FichaVisitaAction.Salvar -> salvar(action.irParaProximo)
            FichaVisitaAction.RecarregarContexto -> Unit
        }
    }

    private fun carregarContexto() {
        _uiState.update { it.copy(carregando = true, contexto = null, erro = null) }
        viewModelScope.launch {
            try {
                val contexto = obterContexto(imovelId)
                val proximo = obterProximoImovel(imovelId)
                _uiState.update { it.copy(contexto = contexto, proximoImovelId = proximo) }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _uiState.update { it.copy(erro = "Não foi possível carregar o imóvel") }
            } finally {
                _uiState.update { it.copy(carregando = false) }
            }
        }
    }

    private fun salvar(irParaProximo: Boolean) {
        val atual = _uiState.value
        val erros = validarFichaVisita(atual)
        if (erros.isNotEmpty()) {
            _uiState.update { it.copy(erros = erros) }
            return
        }
        _uiState.update { it.copy(salvando = true, erro = null) }
        viewModelScope.launch {
            try {
                salvarTratamento(atual.toDomain(clock))
                _uiState.update { it.copy(salvo = true) }
                _eventos.send(FichaVisitaEvento.Salvo(if (irParaProximo) atual.proximoImovelId else null))
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _uiState.update { it.copy(erro = "Erro ao salvar a visita. Tente novamente.") }
            } finally {
                _uiState.update { it.copy(salvando = false) }
            }
        }
    }

    private fun FichaVisitaUiState.semErro(campo: CampoFichaVisita) = copy(erros = erros - campo, erro = null)

    private fun String.filtrarDecimal(): String {
        var temSeparador = false
        return filter { c ->
            when {
                c in '0'..'9' -> true
                (c == ',' || c == '.') && !temSeparador -> { temSeparador = true; true }
                else -> false
            }
        }
    }

    private companion object {
        val FORMATO_HORA: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }
}
