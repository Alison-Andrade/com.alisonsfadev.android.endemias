package com.alisonsfadev.endemias.core.navigation

object EndemiasScreens {
    const val HOME = "home"
    const val VISITAS = "visitas"
    const val RELATORIOS = "relatorios"
    const val PERFIL = "perfil"

    const val VISITAS_LISTA = "visitas/lista"
    const val VISITAS_IMOVEIS = "visitas/{quarteiraoId}/imoveis"
    const val VISITAS_FICHA = "visitas/imovel/{imovelId}/ficha"

    fun visitasImoveisRoute(quarteiraoId: Long) = "visitas/$quarteiraoId/imoveis"
    fun visitasFichaRoute(imovelId: Long) = "visitas/imovel/$imovelId/ficha"
}