package com.alisonsfadev.endemias.core.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.alisonsfadev.endemias.features.home.ui.HomeScreen
import com.alisonsfadev.endemias.features.perfil.ui.PerfilScreen
import com.alisonsfadev.endemias.features.relatorios.ui.RelatoriosScreen
import com.alisonsfadev.endemias.features.visitas.ui.ImoveisScreen
import com.alisonsfadev.endemias.features.visitas.ui.VisitasScreen

@Composable
fun EndemiasNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = EndemiasScreens.HOME,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(EndemiasScreens.HOME) {
            HomeScreen()
        }

        navigation(
            startDestination = EndemiasScreens.VISITAS_LISTA,
            route = EndemiasScreens.VISITAS
        ) {
            composable(EndemiasScreens.VISITAS_LISTA) {
                VisitasScreen(
                    onQuarteiraoClick = { quarteiraoId->
                        navController.navigate(EndemiasScreens.visitasImoveisRoute(quarteiraoId))
                    }
                )
            }
            composable(
                route = EndemiasScreens.VISITAS_IMOVEIS,
                arguments = listOf(navArgument("quarteiraoId") { type = NavType.LongType })
            ) { backStackEntry ->
                val quarteiraoId = backStackEntry.arguments?.getLong("quarteiraoId") ?: 0L
                ImoveisScreen(quarteiraoId = quarteiraoId)
            }
        }

        composable(EndemiasScreens.RELATORIOS) {
            RelatoriosScreen()
        }
        composable(EndemiasScreens.PERFIL) {
            PerfilScreen()
        }
    }
}