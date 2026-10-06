package com.example.oftapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.oftapp.ui.screems.pacientes.PatientHistoryScreen
import com.example.oftapp.ui.theme.OftAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OftAppTheme {
                val navController = rememberNavController()
                // TODO(equipo): reemplazar startDestination por login/menu
                NavHost(navController = navController, startDestination = "historial/1") {
                    composable(
                        route = "historial/{pacienteId}",
                        arguments = listOf(navArgument("pacienteId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val pacienteId = backStackEntry.arguments?.getLong("pacienteId") ?: 1L
                        PatientHistoryScreen(
                            pacienteId = pacienteId,
                            onBack = { navController.popBackStack() },
                            onExamenClick = { id -> navController.navigate("examen/$id") },
                            onVerDocumento = { id -> navController.navigate("documento/$id") }
                        )
                    }
                    composable(
                        route = "examen/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getLong("id") ?: 0L
                        PlaceholderScreen(titulo = "Examen #$id") { navController.popBackStack() }
                    }
                    composable(
                        route = "documento/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getLong("id") ?: 0L
                        PlaceholderScreen(titulo = "Documento #$id") { navController.popBackStack() }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    OftAppTheme {
        // preview vacía, la navegación no se puede previsualizar directamente
    }
}