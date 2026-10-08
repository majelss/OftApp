package com.example.oftapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.oftapp.ui.screems.auth.LoginScreen
import com.example.oftapp.ui.screems.dashboard.DashboardScreen
import com.example.oftapp.ui.screems.dashboard.MainMenuScreen
import com.example.oftapp.ui.screems.examenes.ExamDetailScreen
import com.example.oftapp.ui.screems.examenes.ExamListScreen
import com.example.oftapp.ui.screems.examenes.ExamRegisterScreen
import com.example.oftapp.ui.screems.pacientes.PatientHistoryScreen
import com.example.oftapp.ui.theme.OftAppBackground
import com.example.oftapp.ui.theme.OftappTheme

/**
 * Rutas de navegación completas del sistema OftApp (DSY1105).
 * Todas las pantallas del proyecto están integradas y conectadas.
 */
enum class OftAppScreen {
    LOGIN,
    DASHBOARD,
    ATENCIONES,
    REGISTRO_EXAMEN,
    HISTORIAL,
    DETALLE_EXAMEN,
    MENU_PRINCIPAL
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OftappTheme {
                OftAppNavigationRoot()
            }
        }
    }
}

@Composable
fun OftAppNavigationRoot() {
    var currentScreen by remember { mutableStateOf(OftAppScreen.DASHBOARD) }
    var previousScreen by remember { mutableStateOf(OftAppScreen.DASHBOARD) }
    var currentRol by remember { mutableStateOf("Médico Oftalmólogo") }

    fun navigateTo(destination: OftAppScreen) {
        previousScreen = currentScreen
        currentScreen = destination
    }

    // Soporte para botón atrás del sistema
    BackHandler(enabled = currentScreen != OftAppScreen.DASHBOARD && currentScreen != OftAppScreen.LOGIN) {
        currentScreen = previousScreen
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = OftAppBackground
    ) {
        Crossfade(targetState = currentScreen, label = "MainNavigationCrossfade") { screen ->
            when (screen) {
                OftAppScreen.LOGIN -> {
                    LoginScreen(
                        onLoginSuccess = { rol ->
                            currentRol = rol
                            navigateTo(OftAppScreen.DASHBOARD)
                        }
                    )
                }

                OftAppScreen.DASHBOARD -> {
                    DashboardScreen(
                        rolUsuario = currentRol,
                        onNavigateToRegistro = { navigateTo(OftAppScreen.REGISTRO_EXAMEN) },
                        onNavigateToAtenciones = { navigateTo(OftAppScreen.ATENCIONES) },
                        onNavigateToHistorial = { navigateTo(OftAppScreen.HISTORIAL) },
                        onNavigateToDetalleExamen = { navigateTo(OftAppScreen.DETALLE_EXAMEN) },
                        onLogout = { navigateTo(OftAppScreen.LOGIN) }
                    )
                }

                OftAppScreen.ATENCIONES -> {
                    ExamListScreen(
                        onNavigateToDetalle = { navigateTo(OftAppScreen.DETALLE_EXAMEN) },
                        onNavigateToRegistro = { navigateTo(OftAppScreen.REGISTRO_EXAMEN) },
                        onNavigateToDashboard = { navigateTo(OftAppScreen.DASHBOARD) },
                        onNavigateToHistorial = { navigateTo(OftAppScreen.HISTORIAL) }
                    )
                }

                OftAppScreen.REGISTRO_EXAMEN -> {
                    ExamRegisterScreen(
                        onNavigateBack = { navigateTo(OftAppScreen.DASHBOARD) },
                        onNavigateToDashboard = { navigateTo(OftAppScreen.DASHBOARD) },
                        onNavigateToAtenciones = { navigateTo(OftAppScreen.ATENCIONES) },
                        onNavigateToHistorial = { navigateTo(OftAppScreen.HISTORIAL) }
                    )
                }

                OftAppScreen.HISTORIAL -> {
                    PatientHistoryScreen(
                        onNavigateToDetalle = { navigateTo(OftAppScreen.DETALLE_EXAMEN) },
                        onNavigateToDashboard = { navigateTo(OftAppScreen.DASHBOARD) },
                        onNavigateToAtenciones = { navigateTo(OftAppScreen.ATENCIONES) },
                        onNavigateToRegistro = { navigateTo(OftAppScreen.REGISTRO_EXAMEN) }
                    )
                }

                OftAppScreen.DETALLE_EXAMEN -> {
                    ExamDetailScreen(
                        onNavigateBack = { navigateTo(previousScreen) }
                    )
                }

                OftAppScreen.MENU_PRINCIPAL -> {
                    MainMenuScreen(
                        onNavigateBack = { navigateTo(OftAppScreen.DASHBOARD) },
                        onNavigateToRegistro = { navigateTo(OftAppScreen.REGISTRO_EXAMEN) },
                        onNavigateToAtenciones = { navigateTo(OftAppScreen.ATENCIONES) },
                        onNavigateToHistorial = { navigateTo(OftAppScreen.HISTORIAL) },
                        onNavigateToDetalle = { navigateTo(OftAppScreen.DETALLE_EXAMEN) },
                        onLogout = { navigateTo(OftAppScreen.LOGIN) }
                    )
                }
            }
        }
    }
}