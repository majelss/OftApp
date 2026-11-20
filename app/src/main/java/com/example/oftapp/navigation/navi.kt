package com.example.oftapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.oftapp.PlaceholderScreen
import com.example.oftapp.model.RolUsuario
import com.example.oftapp.model.SesionActual
import com.example.oftapp.ui.screens.auth.LoginScreen
import com.example.oftapp.ui.screens.dashboard.DashboardScreen
import com.example.oftapp.ui.screens.dashboard.MainMenuScreen
import com.example.oftapp.ui.screens.examenes.ExamDetailScreen
import com.example.oftapp.ui.screens.examenes.ExamListScreen
import com.example.oftapp.ui.screens.examenes.ExamRegisterScreen
import com.example.oftapp.ui.screens.pacientes.PacienteHomeScreen
import com.example.oftapp.ui.screens.pacientes.PatientHistoryScreen
import com.example.oftapp.ui.screens.tm.TmMainScaffold

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
    const val TM_MAIN = "tm_main"
    const val PACIENTE_VIEW = "paciente_view"

    const val PACIENTE_ID_DEFECTO = 1L

    fun historial(pacienteId: Long = PACIENTE_ID_DEFECTO): String = "$HISTORIAL/$pacienteId"
}

/**
 * Normaliza los textos que envían las distintas pantallas hacia una ruta válida.
 */
private fun resolverRuta(destino: String): String {
    val d = destino.lowercase()
    return when {
        "login" in d || "cerrar" in d -> Rutas.LOGIN
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
        // ─── Login ───────────────────────────────────────────────
        composable(Rutas.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    val destino = when (SesionActual.usuario.rol) {
                        RolUsuario.TECNOLOGO_MEDICO -> Rutas.TM_MAIN
                        RolUsuario.PACIENTE -> Rutas.PACIENTE_VIEW
                        RolUsuario.MEDICO_OFTALMOLOGO -> Rutas.MAIN_MENU
                        RolUsuario.ADMINISTRATIVO -> Rutas.MAIN_MENU
                    }
                    navController.navigate(destino) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // ─── Tecnólogo Médico (scaffold propio con bottom bar) ──
        composable(Rutas.TM_MAIN) {
            TmMainScaffold(
                onLogoutToLogin = {
                    SesionActual.cerrarSesion()
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ─── Paciente (pantalla principal del paciente) ─────────
        composable(Rutas.PACIENTE_VIEW) {
            PacienteHomeScreen()
        }

        // ─── Menú principal (Médico / Administrativo) ───────────
        composable(Rutas.MAIN_MENU) {
            MainMenuScreen(onNavigateTo = navegar)
        }

        // ─── Dashboard ──────────────────────────────────────────
        composable(Rutas.DASHBOARD) {
            DashboardScreen(onNavegar = navegar)
        }

        // ─── Lista de Atenciones ────────────────────────────────
        composable(Rutas.LISTA_ATENCIONES) {
            ExamListScreen(
                onNavigateToDetalle = { navController.navigate(Rutas.DETALLE_EXAMEN) },
                onNavigateToRegistro = { navController.navigate(Rutas.REGISTRAR_EXAMEN) },
                onNavigateToDashboard = { navController.navigate(Rutas.DASHBOARD) },
                onNavigateToHistorial = { navController.navigate(Rutas.historial()) }
            )
        }

        // ─── Registrar Examen ───────────────────────────────────
        composable(Rutas.REGISTRAR_EXAMEN) {
            ExamRegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDashboard = { navController.navigate(Rutas.DASHBOARD) },
                onNavigateToAtenciones = { navController.navigate(Rutas.LISTA_ATENCIONES) },
                onNavigateToHistorial = { navController.navigate(Rutas.historial()) }
            )
        }

        // ─── Historial del Paciente ─────────────────────────────
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

        // ─── Detalle de Examen ──────────────────────────────────
        composable(Rutas.DETALLE_EXAMEN) {
            ExamDetailScreen(onNavigateBack = { navController.popBackStack() })
        }

        // ─── Patologías (placeholder) ───────────────────────────
        composable(Rutas.PATOLOGIAS) {
            PlaceholderScreen(
                titulo = "Patologías & Agudeza",
                onBack = { navController.popBackStack() }
            )
        }
    }
}
