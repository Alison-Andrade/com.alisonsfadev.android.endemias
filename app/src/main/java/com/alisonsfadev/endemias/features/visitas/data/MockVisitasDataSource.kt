package com.alisonsfadev.endemias.features.visitas.data

import com.alisonsfadev.endemias.features.visitas.domain.Imovel
import com.alisonsfadev.endemias.features.visitas.domain.Quarteirao

object MockVisitasDataSource {

    fun getQuarteiroes(): List<Quarteirao> = listOf(
        Quarteirao(1, 12, "Rua das Flores", totalImoveis = 30, imoveisVisitados = 30),
        Quarteirao(2, 13, "Rua Principal", totalImoveis = 25, imoveisVisitados = 10),
        Quarteirao(3, 14, "Av. Central", totalImoveis = 18, imoveisVisitados = 0),
    )

    fun getImoveisDoQuarteirao(quarteiraoId: Long): List<Imovel> = listOf(
        Imovel(1, "101", "Rua das Flores", "RESIDENCIA", visitado = false),
        Imovel(2, "102", "Rua das Flores", "RESIDENCIA", visitado = true),
        Imovel(3, "S/N", "Rua das Flores", "TERRENO_BALDIO", visitado = false),
    )
}