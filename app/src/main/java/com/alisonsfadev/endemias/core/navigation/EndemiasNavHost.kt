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
import com.alisonsfadev.endemias.features.auth.ui.LoginScreen
import com.alisonsfadev.endemias.features.perfil.ui.PerfilScreen
import com.alisonsfadev.endemias.features.relatorios.ui.RelatoriosScreen
import com.alisonsfadev.endemias.features.visitas.ui.FichaVisitaRoute
import com.alisonsfadev.endemias.features.visitas.ui.ImoveisScreen
import com.alisonsfadev.endemias.features.visitas.ui.QuarteiroesScreen

@Composable
fun EndemiasNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = EndemiasScreens.LOGIN,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(EndemiasScreens.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(EndemiasScreens.HOME) {
                        popUpTo(EndemiasScreens.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(EndemiasScreens.HOME) {
            HomeScreen()
        }

        navigation(
            startDestination = EndemiasScreens.VISITAS_LISTA,
            route = EndemiasScreens.VISITAS
        ) {
            composable(EndemiasScreens.VISITAS_LISTA) {
                QuarteiroesScreen(
                    onQuarteiraoClick = { quarteiraoId->
                        navController.navigate(EndemiasScreens.visitasImoveisRoute(quarteiraoId))
                    }
                )
            }
            composable(
                route = EndemiasScreens.VISITAS_IMOVEIS,
                arguments = listOf(navArgument(EndemiasScreens.ARG_QUARTEIRAO_ID) { type = NavType.LongType })
            ) { backStackEntry ->
                val quarteiraoId = requireNotNull(backStackEntry.arguments).getLong(EndemiasScreens.ARG_QUARTEIRAO_ID)
                ImoveisScreen(
                    quarteiraoId = quarteiraoId,
                    onNavigateBack = { navController.popBackStack() },
                    onImovelClick = { imovelId ->
                        navController.navigate(EndemiasScreens.visitasFichaRoute(imovelId))
                    }
                )
            }
            composable(
                route = EndemiasScreens.VISITAS_FICHA,
                arguments = listOf(navArgument(EndemiasScreens.ARG_IMOVEL_ID) { type = NavType.LongType })
            ) {
                FichaVisitaRoute(
                    onNavigateBack = { navController.popBackStack() },
                    onSaveSuccess = { proximoImovelId ->
                        if (proximoImovelId == null) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(EndemiasScreens.visitasFichaRoute(proximoImovelId)) {
                                popUpTo(EndemiasScreens.VISITAS_FICHA) { inclusive = true }
                            }
                        }
                    },
                )
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
