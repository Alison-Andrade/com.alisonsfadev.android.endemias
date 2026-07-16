package com.alisonsfadev.endemias.core.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun EndemiasApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController
            )
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