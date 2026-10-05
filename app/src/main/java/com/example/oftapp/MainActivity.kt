package com.example.oftapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.oftapp.ui.screens.auth.LoginScreen
import com.example.oftapp.ui.screens.dashboard.MainMenuScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "login"
            ) {
                // Ruta 1: Login
                composable("login") {
                    LoginScreen(
                        onLoginSuccess = {
                            navController.navigate("main_menu") {
                                popUpTo("login") { inclusive = true } // No regresa al login al presionar 'Atrás'
                            }
                        }
                    )
                }

                // Ruta 2: Menú Principal
                composable("main_menu") {
                    MainMenuScreen()
                }
            }
        }
    }
}



