package com.example.oftapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.oftapp.PlaceholderScreen
import com.example.oftapp.ui.screens.auth.LoginScreen
import com.example.oftapp.ui.screens.dashboard.DashboardScreen
import com.example.oftapp.ui.screens.dashboard.MainMenuScreen
import com.example.oftapp.ui.screens.examenes.ExamDetailScreen
import com.example.oftapp.ui.screens.examenes.ExamListScreen
import com.example.oftapp.ui.screens.examenes.ExamRegisterScreen
import com.example.oftapp.ui.screens.pacientes.PatientHistoryScreen

/**
 * Rutas canónicas del sistema OftApp.
 */
object Rutas {
    const val LOGIN = "login"
    const val MAIN_MENU = "main_menu"
    const val DASHBOARD = "dashboard"
    const val LISTA_ATENCIONES = "lista_atenciones"
    const val REGISTRAR_EXAMEN = "registrar_examen"
    const val HISTORIAL = "historial"
    const val DETALLE_EXAMEN = "detalle_examen"
    const val PATOLOGIAS = "patologias"

    const val PACIENTE_ID_DEFECTO = 1L

    fun historial(pacienteId: Long = PACIENTE_ID_DEFECTO): String = "$HISTORIAL/$pacienteId"
}

/**
 * Normaliza los textos que envían las distintas pantallas (claves de ruta del
 * menú principal y etiquetas amigables del dashboard) hacia una ruta válida.
 * Las opciones aún no implementadas caen en una pantalla "en construcción"
 * para no dejar botones muertos ni provocar crashes.
 */
private fun resolverRuta(destino: String): String {
    val d = destino.lowercase()
    return when {
        "historial" in d -> Rutas.historial()
        "detalle" in d -> Rutas.DETALLE_EXAMEN
        "atencion" in d -> Rutas.LISTA_ATENCIONES
        "registr" in d || "examen" in d -> Rutas.REGISTRAR_EXAMEN
        "dashboard" in d || "inicio" in d -> Rutas.DASHBOARD
        else -> Rutas.PATOLOGIAS
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navegar: (String) -> Unit = { destino ->
        navController.navigate(resolverRuta(destino))
    }

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
    ) {
        composable(Rutas.LOGIN) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Rutas.MAIN_MENU) }
            )
        }

        composable(Rutas.MAIN_MENU) {
            MainMenuScreen(onNavigateTo = navegar)
        }

        composable(Rutas.DASHBOARD) {
            DashboardScreen(onNavegar = navegar)
        }

        composable(Rutas.LISTA_ATENCIONES) {
            ExamListScreen(
                onNavigateToDetalle = { navController.navigate(Rutas.DETALLE_EXAMEN) },
                onNavigateToRegistro = { navController.navigate(Rutas.REGISTRAR_EXAMEN) },
                onNavigateToDashboard = { navController.navigate(Rutas.DASHBOARD) },
                onNavigateToHistorial = { navController.navigate(Rutas.historial()) }
            )
        }

        composable(Rutas.REGISTRAR_EXAMEN) {
            ExamRegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDashboard = { navController.navigate(Rutas.DASHBOARD) },
                onNavigateToAtenciones = { navController.navigate(Rutas.LISTA_ATENCIONES) },
                onNavigateToHistorial = { navController.navigate(Rutas.historial()) }
            )
        }

        composable(
            route = "${Rutas.HISTORIAL}/{pacienteId}",
            arguments = listOf(navArgument("pacienteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val pacienteId = backStackEntry.arguments?.getLong("pacienteId")
                ?: Rutas.PACIENTE_ID_DEFECTO
            PatientHistoryScreen(
                pacienteId = pacienteId,
                onBack = { navController.popBackStack() },
                onExamenClick = { navController.navigate(Rutas.DETALLE_EXAMEN) },
                onVerDocumento = { navController.navigate(Rutas.DETALLE_EXAMEN) }
            )
        }

        composable(Rutas.DETALLE_EXAMEN) {
            ExamDetailScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Rutas.PATOLOGIAS) {
            PlaceholderScreen(
                titulo = "Patologías & Agudeza",
                onBack = { navController.popBackStack() }
            )
        }
    }
}
