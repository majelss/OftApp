package com.example.oftapp.ui.screens.tm

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.oftapp.ui.screens.tm.inicio.TmInicioScreen
import com.example.oftapp.ui.screens.tm.registro.TmRegistroScreen
import com.example.oftapp.model.TmRegistroExamen
import com.example.oftapp.model.TmEstadoExamen
import com.example.oftapp.ui.screens.tm.TmDetalleRegistroScreen
import com.example.oftapp.ui.screens.tm.TmPerfilScreen

/**
 * Scaffold principal del módulo Tecnólogo Médico.
 * Tiene su propio NavController interno para la barra inferior.
 */
@Composable
fun TmMainScaffold(
    onLogoutToLogin: () -> Unit = {}
) {
    val tmNavController = rememberNavController()
    val navBackStackEntry by tmNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == TmRutas.INICIO,
                    onClick = {
                        tmNavController.navigate(TmRutas.INICIO) {
                            popUpTo(TmRutas.INICIO) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = currentRoute == TmRutas.REGISTRAR,
                    onClick = {
                        tmNavController.navigate(TmRutas.REGISTRAR) {
                            popUpTo(TmRutas.INICIO)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = "Registrar") },
                    label = { Text("Registrar") }
                )
                NavigationBarItem(
                    selected = currentRoute == TmRutas.REGISTROS,
                    onClick = {
                        tmNavController.navigate(TmRutas.REGISTROS) {
                            popUpTo(TmRutas.INICIO)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "Mis Registros") },
                    label = { Text("Registros") }
                )
                NavigationBarItem(
                    selected = currentRoute == TmRutas.PERFIL,
                    onClick = {
                        tmNavController.navigate(TmRutas.PERFIL) {
                            popUpTo(TmRutas.INICIO)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = tmNavController,
            startDestination = TmRutas.INICIO,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(TmRutas.INICIO) {
                TmInicioScreen(
                    onNavigateBack = { /* root, no-op */ },
                    onNuevoExamen = { tmNavController.navigate(TmRutas.REGISTRAR) },
                    onVerMisRegistros = { tmNavController.navigate(TmRutas.REGISTROS) }
                )
            }
            composable(TmRutas.REGISTRAR) {
                TmRegistroScreen(
                    onNavigateBack = { tmNavController.popBackStack() },
                    onExamenEnviado = { tmNavController.navigate(TmRutas.REGISTROS) }
                )
            }
            composable(TmRutas.REGISTROS) {
                TmMisRegistrosScreen(
                    onNavigateBack = { tmNavController.popBackStack() },
                    onVerDetalle = { tmNavController.navigate(TmRutas.DETALLE) },
                    onEditarRegistro = { tmNavController.navigate(TmRutas.REGISTRAR) },
                    listaExamenes = emptyList()
                )
            }
            composable(TmRutas.DETALLE) {
                TmDetalleRegistroScreen(
                    onNavigateBack = { tmNavController.popBackStack() },
                    onEditarExamen = { tmNavController.navigate(TmRutas.REGISTRAR) },
                    examen = TmRegistroExamen(
                        id = "1",
                        paciente = com.example.oftapp.model.TmPaciente(rut = "15.420.891-K", nombreCompleto = "Mariana González R."),
                        tipoExamen = "Campimetría Computarizada Humphrey",
                        estado = TmEstadoExamen.VALIDADO
                    )
                )
            }
            composable(TmRutas.PERFIL) {
                TmPerfilScreen(
                    onNavigateBack = { tmNavController.popBackStack() },
                    onLogout = onLogoutToLogin
                )
            }
        }
    }
}