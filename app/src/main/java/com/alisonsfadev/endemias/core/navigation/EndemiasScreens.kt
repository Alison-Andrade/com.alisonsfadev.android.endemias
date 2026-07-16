package com.alisonsfadev.endemias.core.navigation

object EndemiasScreens {
    const val HOME = "home"
    const val VISITAS = "visitas"
    const val RELATORIOS = "relatorios"
    const val PERFIL = "perfil"

    const val VISITAS_LISTA = "visitas/lista"
    const val VISITAS_IMOVEIS = "visitas/{quarteiraoId}/imoveis"

    fun visitasImoveisRoute(quarteiraoId: Long) = "visitas/$quarteiraoId/imoveis"
}