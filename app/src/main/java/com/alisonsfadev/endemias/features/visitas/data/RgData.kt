package com.alisonsfadev.endemias.features.visitas.data

//enum class StatusImovel {
//    PENDENTE,
//    VISITADO,
//    FECHADO,
//    FOCO
//}
//
//data class Imovel(
//    val numero: Int,
//    val complemento: String? = null,
//    val status: StatusImovel = StatusImovel.PENDENTE
//)
//
//data class LadoQuarteirao(
//    val nomeRua: String,
//    val lado: Int,
//    val imoveis: List<Imovel>
//)
//
//data class RgData(
//    val id: Int,
//    val localidade: String,      // "Gafuringa"
//    val tipo: String,              // "BR"
//    val cidade: String,          // "João Câmara"
//    val quarteirao: Int,         // 128
//    val lados: List<LadoQuarteirao>
//)
//
//fun getMockRgData(): RgData {
//    return RgData(
//        id = 1,
//        localidade = "Gafuringa",
//        tipo = "BR",
//        cidade = "João Câmara",
//        quarteirao = 128,
//        lados = listOf(
//            LadoQuarteirao(
//                nomeRua = "R. Mangueira",
//                lado = 1,
//                imoveis = listOf(
//                    Imovel(1), Imovel(36), Imovel(42), Imovel(48),
//                    Imovel(54), Imovel(66), Imovel(74), Imovel(82),
//                    Imovel(85), Imovel(96), Imovel(101), Imovel(108),
//                    Imovel(115), Imovel(122)
//                )
//            ),
//            LadoQuarteirao(
//                nomeRua = "R. Mangueira",
//                lado = 2,
//                imoveis = listOf(
//                    Imovel(142), Imovel(150)
//                )
//            ),
//            LadoQuarteirao(
//                nomeRua = "R. Orquídea",
//                lado = 3,
//                imoveis = listOf(
//                    Imovel(223), Imovel(215), Imovel(207), Imovel(167),
//                    Imovel(175), Imovel(183), Imovel(191), Imovel(199),
//                    Imovel(153), Imovel(149), Imovel(141), Imovel(133),
//                    Imovel(125), Imovel(117), Imovel(109)
//                )
//            ),
//            LadoQuarteirao(
//                nomeRua = "R. Goiabeira",
//                lado = 4,
//                imoveis = listOf(Imovel(1))
//            )
//        )
//    )
//}