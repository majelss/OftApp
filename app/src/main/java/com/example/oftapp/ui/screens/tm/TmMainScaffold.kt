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

/**
 * Scaffold principal del módulo Tecnólogo Médico.
 * Tiene su propio NavController interno para la barra inferior.
 */
@Composable
fun TmMainScaffold() {
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
                    icon = { Icon(Icons.Default.Badge, contentDescription = "Perfil") },
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
                TmInicioScreen(onNavigate = { tmNavController.navigate(it) })
            }
            composable(TmRutas.REGISTRAR) {
                // TODO: reemplazar con TmRegistrarExamenScreen real
                Text("Pantalla Registrar Examen — En desarrollo")
            }
            composable(TmRutas.REGISTROS) {
                // TODO: reemplazar con TmMisRegistrosScreen real
                Text("Pantalla Mis Registros — En desarrollo")
            }
            composable(TmRutas.DETALLE) {
                // TODO: reemplazar con TmDetalleRegistroScreen real
                Text("Pantalla Detalle Registro — En desarrollo")
            }
            composable(TmRutas.PERFIL) {
                // TODO: reemplazar con TmPerfilScreen real
                Text("Pantalla Perfil T.M. — En desarrollo")
            }
        }
    }
}
