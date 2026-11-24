package com.example.oftapp.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.oftapp.PlaceholderScreen
import com.example.oftapp.data.local.SessionTokenManager
import com.example.oftapp.model.EstadoSesion
import com.example.oftapp.model.SesionActual
import com.example.oftapp.model.esAdmin
import com.example.oftapp.model.esPaciente
import com.example.oftapp.model.esTecnico
import com.example.oftapp.ui.components.LocalLogoutAction
import com.example.oftapp.ui.screens.admin.AdminAuditScreen
import com.example.oftapp.ui.screens.admin.AdminSystemConfigScreen
import com.example.oftapp.ui.screens.admin.AdminTab
import com.example.oftapp.ui.screens.admin.AdminUserFormScreen
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
import com.example.oftapp.ui.screens.tm.TmMainScaffold

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

    // Rutas exclusivas del rol Tecnólogo Médico
    const val TM_MAIN = "tm_main"

    // Rutas exclusivas del rol Administrador ("Clinical Clarity")
    const val ADMIN_USUARIOS = "admin_usuarios"
    const val ADMIN_SISTEMA = "admin_sistema"
    const val ADMIN_AUDITORIA = "admin_auditoria"

    const val PACIENTE_ID_DEFECTO = 1L

    fun historialMedico(pacienteId: Long = PACIENTE_ID_DEFECTO): String = "$HISTORIAL/$pacienteId"
}

/**
 * Normaliza y enruta las peticiones de navegación según el rol activo de la sesión.
 */
private fun resolverRuta(destino: String, estadoSesion: EstadoSesion): String {
    val d = destino.lowercase()
    if ("login" in d || "cerrar" in d) return Rutas.LOGIN

    return when {
        estadoSesion.esPaciente() -> {
            when {
                "compartir" in d -> Rutas.PACIENTE_COMPARTIR
                "perfil" in d -> Rutas.PACIENTE_PERFIL
                "examenes" in d -> Rutas.PACIENTE_EXAMENES
                "historial" in d -> Rutas.PACIENTE_HISTORIAL
                "detalle" in d -> Rutas.DETALLE_EXAMEN
                "inicio" in d || "home" in d -> Rutas.PACIENTE_HOME
                else -> Rutas.PACIENTE_HOME
            }
        }
        estadoSesion.esAdmin() -> {
            when {
                "usuarios" in d -> Rutas.ADMIN_USUARIOS
                "sistema" in d || "config" in d -> Rutas.ADMIN_SISTEMA
                "audit" in d -> Rutas.ADMIN_AUDITORIA
                else -> Rutas.ADMIN_USUARIOS
            }
        }
        estadoSesion.esTecnico() -> Rutas.TM_MAIN
        else -> {
            when {
                "historial" in d -> Rutas.historialMedico()
                "detalle" in d -> Rutas.DETALLE_EXAMEN
                "atencion" in d -> Rutas.LISTA_ATENCIONES
                "registr" in d || "examen" in d -> Rutas.REGISTRAR_EXAMEN
                "patolog" in d -> Rutas.PATOLOGIAS
                "dashboard" in d || "inicio" in d -> Rutas.DASHBOARD
                "menu" in d -> Rutas.MAIN_MENU
                else -> Rutas.DASHBOARD
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val tokenManager = remember { SessionTokenManager.getInstance(context) }

    // Estado global de la sesión del usuario
    var estadoSesion by remember { mutableStateOf(EstadoSesion()) }

    val cerrarSesionGlobal: () -> Unit = {
        tokenManager.limpiarSesion()
        SesionActual.cerrarSesion()
        estadoSesion = EstadoSesion(
            usuarioId = null,
            rol = null,
            estaAutenticado = false
        )
        navController.navigate(Rutas.LOGIN) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    val navegar: (String) -> Unit = { destino ->
        val d = destino.lowercase()
        if ("login" in d || "cerrar" in d) {
            cerrarSesionGlobal()
        } else {
            navController.navigate(resolverRuta(destino, estadoSesion))
        }
    }

    val navegarTabAdmin: (AdminTab) -> Unit = { tab ->
        val ruta = when (tab) {
            AdminTab.USUARIOS -> Rutas.ADMIN_USUARIOS
            AdminTab.SISTEMA -> Rutas.ADMIN_SISTEMA
            AdminTab.AUDITORIA -> Rutas.ADMIN_AUDITORIA
        }
        navController.navigate(ruta) {
            launchSingleTop = true
        }
    }

    CompositionLocalProvider(LocalLogoutAction provides cerrarSesionGlobal) {
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

                    val rutaInicio = when {
                        estadoSesion.esPaciente() -> Rutas.PACIENTE_HOME
                        estadoSesion.esTecnico() -> Rutas.TM_MAIN
                        estadoSesion.esAdmin() -> Rutas.ADMIN_USUARIOS
                        else -> Rutas.DASHBOARD
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
                    onCerrarSesion = cerrarSesionGlobal,
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
                    onCerrarSesion = cerrarSesionGlobal,
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

        // --- 2.5 FLUJO EXCLUSIVO DEL ROL TECNÓLOGO MÉDICO (AISLADO) ---
        composable(Rutas.TM_MAIN) {
            if (estadoSesion.estaAutenticado && estadoSesion.esTecnico()) {
                TmMainScaffold(
                    onLogoutToLogin = cerrarSesionGlobal
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        // --- 3. FLUJO DEL PERSONAL MÉDICO OFTALMÓLOGO (AISLADO) ---
        composable(Rutas.MAIN_MENU) {
            if (estadoSesion.estaAutenticado && !estadoSesion.esPaciente() && !estadoSesion.esAdmin() && !estadoSesion.esTecnico()) {
                MainMenuScreen(
                    rolActivo = estadoSesion.rol ?: "MEDICO",
                    onNavigateTo = navegar
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        composable(Rutas.DASHBOARD) {
            if (estadoSesion.estaAutenticado && !estadoSesion.esPaciente() && !estadoSesion.esAdmin()) {
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
            if (estadoSesion.estaAutenticado && !estadoSesion.esPaciente() && !estadoSesion.esAdmin()) {
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
            if (estadoSesion.estaAutenticado && !estadoSesion.esPaciente() && !estadoSesion.esAdmin()) {
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
            if (estadoSesion.estaAutenticado && !estadoSesion.esPaciente() && !estadoSesion.esAdmin()) {
                val pacienteId = backStackEntry.arguments?.getLong("pacienteId")
                    ?: Rutas.PACIENTE_ID_DEFECTO

                PatientHistoryScreen(
                    pacienteId = pacienteId,
                    onBack = { navController.popBackStack() },
                    onExamenClick = { navController.navigate(Rutas.DETALLE_EXAMEN) },
                    onVerDocumento = { navController.navigate(Rutas.DETALLE_EXAMEN) },
                    onNavigateToDashboard = { navController.navigate(Rutas.DASHBOARD) },
                    onNavigateToAtenciones = { navController.navigate(Rutas.LISTA_ATENCIONES) },
                    onNavigateToRegistro = { navController.navigate(Rutas.REGISTRAR_EXAMEN) }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        // --- 4. FLUJO EXCLUSIVO DEL ROL ADMINISTRADOR (AISLADO) ---
        composable(Rutas.ADMIN_USUARIOS) {
            if (estadoSesion.estaAutenticado && estadoSesion.esAdmin()) {
                AdminUserFormScreen(
                    onNavigateBack = cerrarSesionGlobal,
                    onNavigateTab = navegarTabAdmin
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        composable(Rutas.ADMIN_SISTEMA) {
            if (estadoSesion.estaAutenticado && estadoSesion.esAdmin()) {
                AdminSystemConfigScreen(
                    onNavigateBack = cerrarSesionGlobal,
                    onNavigateTab = navegarTabAdmin
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        composable(Rutas.ADMIN_AUDITORIA) {
            if (estadoSesion.estaAutenticado && estadoSesion.esAdmin()) {
                AdminAuditScreen(
                    onNavigateBack = cerrarSesionGlobal,
                    onNavigateTab = navegarTabAdmin
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        // --- 5. DETALLE Y PANTALLAS COMPARTIDAS / GENERALES ---
        composable(Rutas.DETALLE_EXAMEN) {
            if (estadoSesion.estaAutenticado && !estadoSesion.esAdmin()) {
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
            if (estadoSesion.estaAutenticado && !estadoSesion.esPaciente() && !estadoSesion.esAdmin()) {
                PlaceholderScreen(
                    titulo = "Patologías & Agudeza",
                    onBack = { navController.popBackStack() }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }
    }
}
