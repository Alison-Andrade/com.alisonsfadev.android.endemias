package com.alisonsfadev.endemias.core.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun EndemiasApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != EndemiasScreens.LOGIN) {
                BottomBar(
                    navController = navController
                )
            }
        },
        contentWindowInsets = WindowInsets(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        EndemiasNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}