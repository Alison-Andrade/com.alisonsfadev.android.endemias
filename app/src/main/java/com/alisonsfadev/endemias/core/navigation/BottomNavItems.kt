package com.alisonsfadev.endemias.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem (
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = EndemiasScreens.HOME,
        icon = Icons.Default.Home,
        label = "Início"
    ),
    BottomNavItem(
        route = EndemiasScreens.VISITAS,
        icon = Icons.Default.EditNote,
        label = "Visitas"
    ),
    BottomNavItem(
        route = EndemiasScreens.RELATORIOS,
        icon = Icons.Default.BarChart,
        label = "Relatórios"
    ),
    BottomNavItem(
        route = EndemiasScreens.PERFIL,
        icon = Icons.Default.Person,
        label = "Perfil"
    ),
)