package com.alisonsfadev.endemias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alisonsfadev.endemias.core.navigation.EndemiasApp
import com.alisonsfadev.endemias.ui.theme.EndemiasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EndemiasTheme {
                EndemiasApp()
            }
        }
    }
}