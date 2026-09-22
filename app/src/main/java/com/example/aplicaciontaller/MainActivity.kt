package com.example.aplicaciontaller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.aplicaciontaller.navigation.NavGraph
import com.example.aplicaciontaller.ui.theme.AplicacionTallerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplicacionTallerTheme {
                NavGraph()
            }
        }
    }
}
