package com.example.oftapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.oftapp.navigation.AppNavigation
//import com.example.oftapp.ui.theme.OftAppTheme // Si usas el tema por defecto

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Invoca la navegación principal aquí
            AppNavigation()
        }
    }
}


