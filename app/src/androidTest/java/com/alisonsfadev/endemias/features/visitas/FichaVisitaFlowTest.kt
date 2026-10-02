package com.alisonsfadev.endemias.features.visitas

import androidx.activity.compose.setContent
import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.alisonsfadev.endemias.MainActivity
import com.alisonsfadev.endemias.core.navigation.EndemiasApp
import com.alisonsfadev.endemias.ui.theme.EndemiasTheme
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class FichaVisitaFlowTest(private val darkTheme: Boolean) {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    private fun abrirPrimeiroImovel() {
        compose.activity.runOnUiThread {
            compose.activity.setContent {
                EndemiasTheme(darkTheme = darkTheme) { EndemiasApp() }
            }
        }
        compose.onNodeWithText("CPF, E-mail ou Matrícula").performTextInput("teste")
        compose.onNodeWithText("Senha").performTextInput("teste")
        compose.onNodeWithText("Entrar").performClick()
        compose.waitUntil(5_000) { compose.onAllNodesWithText("Início").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Visitas").performClick()
        compose.onNodeWithText("12").performClick()
        compose.onNodeWithText("Nº 101").performClick()
        compose.onNodeWithText("101 · RESIDENCIA").assertExists()
    }

    @Test fun inicioExplicitoPreservaHorarioEAvancaSemEmpilharFichas() {
        abrirPrimeiroImovel()
        listOf("TRABALHADO", "FECHADO", "RECUSADO").forEach {
            compose.onNodeWithTag("situacao_$it").assertIsNotSelected()
        }
        compose.onNodeWithTag("hora_visita").assertDoesNotExist()
        compose.onNodeWithTag("imovel_tratado").assertDoesNotExist()
        capturar("inicial")
        compose.onNodeWithTag("salvar_visita").performScrollTo().performClick()
        compose.onNodeWithText("Selecione a situação do imóvel").assertExists()

        compose.onNodeWithTag("situacao_TRABALHADO").performScrollTo().performClick()
        compose.onNodeWithTag("hora_visita").assertExists()
        compose.onNodeWithTag("hora_visita").performTextClearance()
        compose.onNodeWithTag("hora_visita").performTextInput("08:15")
        compose.onNodeWithTag("situacao_FECHADO").performClick()
        compose.onNodeWithTag("hora_visita").assertDoesNotExist()
        compose.onNodeWithTag("situacao_RECUSADO").performClick()
        compose.onNodeWithTag("hora_visita").assertTextContains("08:15")
        compose.onNodeWithTag("situacao_TRABALHADO").performClick()
        compose.onNodeWithTag("hora_visita").assertTextContains("08:15")
        compose.onNodeWithTag("imovel_tratado").assertTextContains("Imóvel tratado: Não")
        compose.onNodeWithContentDescription("Aumentar depósitos eliminados").performScrollTo().performClick()
        compose.onNodeWithTag("imovel_tratado").assertTextContains("Imóvel tratado: Sim")
        compose.onNodeWithContentDescription("Diminuir depósitos eliminados").performClick()
        compose.onNodeWithTag("imovel_tratado").assertTextContains("Imóvel tratado: Não")
        compose.onNodeWithContentDescription("Aumentar depósitos eliminados").performClick()
        capturar("iniciada")
        compose.onNodeWithTag("salvar_proximo").performScrollTo().performClick()
        compose.onNodeWithText("102 · RESIDENCIA").assertExists()
        compose.onNodeWithTag("situacao_TRABALHADO").assertIsNotSelected()
        compose.onNodeWithTag("hora_visita").assertDoesNotExist()
        compose.onNodeWithTag("situacao_RECUSADO").performClick()
        compose.onNodeWithTag("hora_visita").assertDoesNotExist()
        compose.onNodeWithTag("salvar_proximo").performScrollTo().performClick()
        compose.onNodeWithText("S/N · TERRENO_BALDIO").assertExists()
        compose.onNodeWithTag("situacao_FECHADO").performClick()
        compose.onNodeWithTag("salvar_proximo").performScrollTo().performClick()
        compose.onNodeWithText("Selecione um imóvel para vistoriar").assertExists()
        compose.onNodeWithText("Nº 101").performClick()
        compose.onNodeWithTag("situacao_TRABALHADO").assertIsNotSelected()
        compose.onNodeWithTag("hora_visita").assertDoesNotExist()
        compose.onNodeWithText("101 · RESIDENCIA").assertExists()
        compose.onNodeWithTag("situacao_FECHADO").performClick()
        compose.onNodeWithTag("salvar_visita").performScrollTo().performClick()
        compose.onNodeWithText("Selecione um imóvel para vistoriar").assertExists()
    }

    @Test fun larvicidaConsideraTratadoSemDepositosEliminados() {
        abrirPrimeiroImovel()
        compose.onNodeWithTag("situacao_TRABALHADO").performClick()
        compose.onNodeWithTag("imovel_tratado").assertTextContains("Imóvel tratado: Não")
        compose.onNodeWithTag("recipientes_tratados").performScrollTo().performTextInput("2")
        compose.onNodeWithTag("salvar_visita").performScrollTo().performClick()
        compose.onNodeWithText("Selecione o larvicida").assertExists()
        compose.onNodeWithText("Sem larvicida").performScrollTo().performClick()
        compose.onNodeWithText("B").performClick()
        compose.onNodeWithTag("carga_larvicida").performScrollTo().performTextInput("1,5")
        compose.onNodeWithTag("imovel_tratado").assertTextContains("Imóvel tratado: Sim")
        compose.onNodeWithTag("depositos_eliminados").assertTextContains("0")
        compose.onNodeWithTag("salvar_visita").performScrollTo().performClick()
        compose.onNodeWithText("Selecione um imóvel para vistoriar").assertExists()
    }

    @Test fun quantidadesAceitamDigitacaoEControlesNosDoisCampos() {
        abrirPrimeiroImovel()
        compose.onNodeWithTag("situacao_TRABALHADO").performClick()
        compose.onNodeWithContentDescription("Diminuir depósitos eliminados").assertIsNotEnabled()
        compose.onNodeWithTag("depositos_eliminados").performTextClearance()
        compose.onNodeWithTag("depositos_eliminados").performTextInput("12")
        compose.onNodeWithContentDescription("Aumentar depósitos eliminados").performClick()
        compose.onNodeWithTag("depositos_eliminados").assertTextContains("13")
        compose.onNodeWithContentDescription("Diminuir depósitos eliminados").performClick()
        compose.onNodeWithTag("depositos_eliminados").assertTextContains("12")
        compose.onNodeWithTag("depositos_eliminados").performTextClearance()
        compose.onNodeWithTag("depositos_eliminados").performTextInput(Int.MAX_VALUE.toString())
        compose.onNodeWithContentDescription("Aumentar depósitos eliminados").assertIsNotEnabled()
        compose.onNodeWithTag("depositos_eliminados").performTextClearance()
        compose.onNodeWithTag("depositos_eliminados").performTextInput("0")
        compose.onNodeWithTag("imovel_tratado").assertTextContains("Imóvel tratado: Não")

        compose.onNodeWithContentDescription("Diminuir depósitos tratados").performScrollTo().assertIsNotEnabled()
        compose.onNodeWithContentDescription("Aumentar depósitos tratados").performScrollTo().performClick()
        compose.onNodeWithTag("recipientes_tratados").assertTextContains("1")
        compose.onNodeWithTag("imovel_tratado").assertTextContains("Imóvel tratado: Sim")
        compose.onNodeWithContentDescription("Diminuir depósitos tratados").performClick()
        compose.onNodeWithTag("recipientes_tratados").assertTextContains("0")
        compose.onNodeWithTag("imovel_tratado").assertTextContains("Imóvel tratado: Não")
        compose.onNodeWithTag("recipientes_tratados").performTextClearance()
        compose.onNodeWithTag("recipientes_tratados").performTextInput("7")
        compose.onNodeWithContentDescription("Aumentar depósitos tratados").performClick()
        compose.onNodeWithTag("recipientes_tratados").assertTextContains("8")
        compose.onNodeWithContentDescription("Diminuir depósitos tratados").performClick()
        compose.onNodeWithTag("recipientes_tratados").assertTextContains("7")
        compose.onNodeWithText("Sem larvicida").performScrollTo().performClick()
        compose.onNodeWithText("B").performClick()
        compose.onNodeWithTag("carga_larvicida").performScrollTo().performTextInput("1,5")
        capturar("quantidades")
        compose.onNodeWithTag("salvar_visita").performScrollTo().performClick()
        compose.onNodeWithText("Selecione um imóvel para vistoriar").assertExists()
    }

    private fun capturar(estado: String) {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        val arquivo = File(contexto.cacheDir, "ficha_${if (darkTheme) "escura" else "clara"}_$estado.png")
        arquivo.outputStream().use {
            compose.onRoot().captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "temaEscuro={0}")
        fun temas(): List<Array<Boolean>> = listOf(arrayOf(false), arrayOf(true))
    }
}
