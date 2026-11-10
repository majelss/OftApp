package com.example.oftapp.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.oftapp.PlaceholderScreen
import com.example.oftapp.model.EstadoSesion
import com.example.oftapp.model.esPaciente
import com.example.oftapp.ui.screens.auth.LoginScreen
import com.example.oftapp.ui.screens.dashboard.DashboardScreen
import com.example.oftapp.ui.screens.dashboard.MainMenuScreen
import com.example.oftapp.ui.screens.examenes.ExamDetailScreen
import com.example.oftapp.ui.screens.examenes.ExamListScreen
import com.example.oftapp.ui.screens.examenes.ExamRegisterScreen
import com.example.oftapp.ui.screens.pacientes.PacienteCompartirScreen
import com.example.oftapp.ui.screens.pacientes.PacienteExamDetailScreen
import com.example.oftapp.ui.screens.pacientes.PacienteExamenes
import com.example.oftapp.ui.screens.pacientes.PacienteHomeScreen
import com.example.oftapp.ui.screens.pacientes.PacientePerfilScreen
import com.example.oftapp.ui.screens.pacientes.PatientHistoryScreen

/**
 * Rutas canónicas del sistema OftApp.
 */
object Rutas {
    const val LOGIN = "login"

    // Rutas específicas del rol Paciente
    const val PACIENTE_HOME = "paciente_home"
    const val PACIENTE_EXAMENES = "paciente_examenes"
    const val PACIENTE_HISTORIAL = "paciente_historial"
    const val PACIENTE_PERFIL = "paciente_perfil"
    const val PACIENTE_COMPARTIR = "paciente_compartir"

    // Rutas específicas del rol Médico / Especialista
    const val MAIN_MENU = "main_menu"
    const val DASHBOARD = "dashboard"
    const val LISTA_ATENCIONES = "lista_atenciones"
    const val REGISTRAR_EXAMEN = "registrar_examen"
    const val HISTORIAL = "historial"
    const val DETALLE_EXAMEN = "detalle_examen"
    const val PATOLOGIAS = "patologias"

    const val PACIENTE_ID_DEFECTO = 1L

    fun historialMedico(pacienteId: Long = PACIENTE_ID_DEFECTO): String = "$HISTORIAL/$pacienteId"
}

/**
 * Normaliza y enruta las peticiones de navegación según el rol activo de la sesión.
 */
private fun resolverRuta(destino: String, estadoSesion: EstadoSesion): String {
    val d = destino.lowercase()
    if ("login" in d || "cerrar" in d) return Rutas.LOGIN

    return if (estadoSesion.esPaciente()) {
        when {
            "compartir" in d -> Rutas.PACIENTE_COMPARTIR
            "perfil" in d -> Rutas.PACIENTE_PERFIL
            "examenes" in d -> Rutas.PACIENTE_EXAMENES
            "historial" in d -> Rutas.PACIENTE_HISTORIAL
            "detalle" in d -> Rutas.DETALLE_EXAMEN
            "inicio" in d || "home" in d -> Rutas.PACIENTE_HOME
            else -> Rutas.PACIENTE_HOME
        }
    } else {
        when {
            "historial" in d -> Rutas.historialMedico()
            "detalle" in d -> Rutas.DETALLE_EXAMEN
            "atencion" in d -> Rutas.LISTA_ATENCIONES
            "registr" in d || "examen" in d -> Rutas.REGISTRAR_EXAMEN
            "dashboard" in d -> Rutas.DASHBOARD
            "inicio" in d || "menu" in d -> Rutas.MAIN_MENU
            else -> Rutas.MAIN_MENU
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Estado global de la sesión del usuario
    var estadoSesion by remember { mutableStateOf(EstadoSesion()) }

    val navegar: (String) -> Unit = { destino ->
        navController.navigate(resolverRuta(destino, estadoSesion))
    }

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
    ) {
        // --- 1. AUTENTICACIÓN ---
        composable(Rutas.LOGIN) {
            LoginScreen(
                onLoginSuccess = { usuarioId, rol ->
                    estadoSesion = EstadoSesion(
                        usuarioId = usuarioId,
                        rol = rol,
                        estaAutenticado = true
                    )

                    val rutaInicio = if (estadoSesion.esPaciente()) {
                        Rutas.PACIENTE_HOME
                    } else {
                        Rutas.MAIN_MENU
                    }

                    navController.navigate(rutaInicio) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // --- 2. FLUJO DEL ROL PACIENTE (AISLADO) ---
        composable(Rutas.PACIENTE_HOME) {
            if (estadoSesion.estaAutenticado && estadoSesion.esPaciente()) {
                PacienteHomeScreen(
                    nombrePaciente = "Mariana",
                    rutPaciente = "15.420.891-K",
                    onVerExamenesArchivados = {
                        navController.navigate(Rutas.PACIENTE_EXAMENES)
                    },
                    onNavegarItemBottomBar = { destino ->
                        when (destino) {
                            "inicio" -> navController.navigate(Rutas.PACIENTE_HOME)
                            "examenes" -> navController.navigate(Rutas.PACIENTE_EXAMENES)
                            "historial" -> navController.navigate(Rutas.PACIENTE_HISTORIAL)
                            "perfil" -> navController.navigate(Rutas.PACIENTE_PERFIL)
                            "compartir" -> navController.navigate(Rutas.PACIENTE_COMPARTIR)
                            else -> navController.navigate(Rutas.PACIENTE_HOME)
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        composable(Rutas.PACIENTE_EXAMENES) {
            if (estadoSesion.estaAutenticado && estadoSesion.esPaciente()) {
                PacienteExamenes(
                    pacienteId = estadoSesion.usuarioId ?: Rutas.PACIENTE_ID_DEFECTO,
                    onBack = {
                        navController.navigate(Rutas.PACIENTE_HOME) {
                            popUpTo(Rutas.PACIENTE_HOME) { inclusive = true }
                        }
                    },
                    onExamenClick = { navController.navigate(Rutas.DETALLE_EXAMEN) },
                    onVerDocumento = { navController.navigate(Rutas.DETALLE_EXAMEN) },
                    onNavegarItemBottomBar = { destino ->
                        when (destino) {
                            "inicio" -> navController.navigate(Rutas.PACIENTE_HOME)
                            "examenes" -> { /* Ya estamos en Mis Exámenes */ }
                            "historial" -> navController.navigate(Rutas.PACIENTE_HISTORIAL)
                            "perfil" -> navController.navigate(Rutas.PACIENTE_PERFIL)
                            "compartir" -> navController.navigate(Rutas.PACIENTE_COMPARTIR)
                            else -> navController.navigate(Rutas.PACIENTE_HOME)
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        composable(Rutas.PACIENTE_HISTORIAL) {
            if (estadoSesion.estaAutenticado && estadoSesion.esPaciente()) {
                PatientHistoryScreen(
                    pacienteId = estadoSesion.usuarioId ?: Rutas.PACIENTE_ID_DEFECTO,
                    onBack = {
                        navController.navigate(Rutas.PACIENTE_HOME) {
                            popUpTo(Rutas.PACIENTE_HOME) { inclusive = true }
                        }
                    },
                    onExamenClick = { navController.navigate(Rutas.DETALLE_EXAMEN) },
                    onVerDocumento = { navController.navigate(Rutas.DETALLE_EXAMEN) },
                    onNavegarItemBottomBar = { destino ->
                        when (destino) {
                            "inicio" -> navController.navigate(Rutas.PACIENTE_HOME)
                            "examenes" -> navController.navigate(Rutas.PACIENTE_EXAMENES)
                            "historial" -> { /* Ya estamos en Historial Paciente */ }
                            "perfil" -> navController.navigate(Rutas.PACIENTE_PERFIL)
                            "compartir" -> navController.navigate(Rutas.PACIENTE_COMPARTIR)
                            else -> navController.navigate(Rutas.PACIENTE_HOME)
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        composable(Rutas.PACIENTE_PERFIL) {
            if (estadoSesion.estaAutenticado && estadoSesion.esPaciente()) {
                PacientePerfilScreen(
                    nombrePaciente = "Mariana González R.",
                    rutPaciente = "15.420.891-K",
                    onCerrarSesion = {
                        // Resetear la sesión por completo
                        estadoSesion = EstadoSesion(
                            usuarioId = null,
                            rol = null,
                            estaAutenticado = false
                        )
                        // Redirigir al login limpiando toda la pila de pantallas
                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavegarItemBottomBar = { destino ->
                        when (destino) {
                            "inicio" -> navController.navigate(Rutas.PACIENTE_HOME)
                            "examenes" -> navController.navigate(Rutas.PACIENTE_EXAMENES)
                            "historial" -> navController.navigate(Rutas.PACIENTE_HISTORIAL)
                            "perfil" -> { /* Ya estamos en Perfil */ }
                            "compartir" -> navController.navigate(Rutas.PACIENTE_COMPARTIR)
                            else -> navController.navigate(Rutas.PACIENTE_HOME)
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        composable(Rutas.PACIENTE_COMPARTIR) {
            if (estadoSesion.estaAutenticado && estadoSesion.esPaciente()) {
                PacienteCompartirScreen(
                    onNavegarItemBottomBar = { destino ->
                        when (destino) {
                            "inicio" -> navController.navigate(Rutas.PACIENTE_HOME)
                            "examenes" -> navController.navigate(Rutas.PACIENTE_EXAMENES)
                            "historial" -> navController.navigate(Rutas.PACIENTE_HISTORIAL)
                            "perfil" -> navController.navigate(Rutas.PACIENTE_PERFIL)
                            "compartir" -> { /* Ya estamos en Compartir */ }
                            else -> navController.navigate(Rutas.PACIENTE_HOME)
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        // --- 3. FLUJO DEL PERSONAL MÉDICO (AISLADO) ---
        composable(Rutas.MAIN_MENU) {
            if (estadoSesion.estaAutenticado && !estadoSesion.esPaciente()) {
                MainMenuScreen(onNavigateTo = navegar)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        composable(Rutas.DASHBOARD) {
            if (estadoSesion.estaAutenticado && !estadoSesion.esPaciente()) {
                DashboardScreen(onNavegar = navegar)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        composable(Rutas.LISTA_ATENCIONES) {
            if (estadoSesion.estaAutenticado && !estadoSesion.esPaciente()) {
                ExamListScreen(
                    onNavigateToDetalle = { navController.navigate(Rutas.DETALLE_EXAMEN) },
                    onNavigateToRegistro = { navController.navigate(Rutas.REGISTRAR_EXAMEN) },
                    onNavigateToDashboard = { navController.navigate(Rutas.DASHBOARD) },
                    onNavigateToHistorial = { navController.navigate(Rutas.historialMedico()) }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        composable(Rutas.REGISTRAR_EXAMEN) {
            if (estadoSesion.estaAutenticado && !estadoSesion.esPaciente()) {
                ExamRegisterScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDashboard = { navController.navigate(Rutas.DASHBOARD) },
                    onNavigateToAtenciones = { navController.navigate(Rutas.LISTA_ATENCIONES) },
                    onNavigateToHistorial = { navController.navigate(Rutas.historialMedico()) }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        // Historial clínico visto desde el rol Médico
        composable(
            route = "${Rutas.HISTORIAL}/{pacienteId}",
            arguments = listOf(navArgument("pacienteId") { type = NavType.LongType })
        ) { backStackEntry ->
            if (estadoSesion.estaAutenticado && !estadoSesion.esPaciente()) {
                val pacienteId = backStackEntry.arguments?.getLong("pacienteId")
                    ?: Rutas.PACIENTE_ID_DEFECTO

                PatientHistoryScreen(
                    pacienteId = pacienteId,
                    onBack = { navController.popBackStack() },
                    onExamenClick = { navController.navigate(Rutas.DETALLE_EXAMEN) },
                    onVerDocumento = { navController.navigate(Rutas.DETALLE_EXAMEN) }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        // --- 4. DETALLE Y PANTALLAS COMPARTIDAS / GENERALES ---
        composable(Rutas.DETALLE_EXAMEN) {
            if (estadoSesion.estaAutenticado) {
                if (estadoSesion.esPaciente()) {
                    PacienteExamDetailScreen(onNavigateBack = { navController.popBackStack() })
                } else {
                    ExamDetailScreen(onNavigateBack = { navController.popBackStack() })
                }
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        composable(Rutas.PATOLOGIAS) {
            PlaceholderScreen(
                titulo = "Patologías & Agudeza",
                onBack = { navController.popBackStack() }
            )
        }
    }
}