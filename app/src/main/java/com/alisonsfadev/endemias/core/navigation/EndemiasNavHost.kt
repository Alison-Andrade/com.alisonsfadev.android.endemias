package com.alisonsfadev.endemias.core.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alisonsfadev.endemias.features.home.ui.HomeScreen
import com.alisonsfadev.endemias.features.perfil.ui.PerfilScreen
import com.alisonsfadev.endemias.features.relatorios.ui.RelatoriosScreen
import com.alisonsfadev.endemias.features.visitas.ui.VisitasScreen

@Composable
fun EndemiasNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = EndemiasScreens.HOME,
        modifier = modifier
    ) {
        composable(EndemiasScreens.HOME) {
            HomeScreen()
        }
        composable(EndemiasScreens.VISITAS) {
            VisitasScreen()
        }
        composable(EndemiasScreens.RELATORIOS) {
            RelatoriosScreen()
        }
        composable(EndemiasScreens.PERFIL) {
            PerfilScreen()
        }
    }
}