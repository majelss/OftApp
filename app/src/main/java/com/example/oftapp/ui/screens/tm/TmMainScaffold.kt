package com.example.oftapp.ui.screens.tm

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.oftapp.model.TmEstadoExamen
import com.example.oftapp.model.TmRegistroExamen
import com.example.oftapp.ui.components.UnifiedBorderGray
import com.example.oftapp.ui.components.UnifiedIndicatorBlue
import com.example.oftapp.ui.components.UnifiedMutedSlate
import com.example.oftapp.ui.components.UnifiedPrimaryBlue
import com.example.oftapp.ui.screens.tm.inicio.TmInicioScreen
import com.example.oftapp.ui.screens.tm.registro.TmRegistroScreen

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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = UnifiedPrimaryBlue,
        selectedTextColor = UnifiedPrimaryBlue,
        indicatorColor = UnifiedIndicatorBlue,
        unselectedIconColor = UnifiedMutedSlate,
        unselectedTextColor = UnifiedMutedSlate
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerShape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp)
            ) {
                TmDrawerMenu(
                    opcionSeleccionada = when (currentRoute) {
                        TmRutas.INICIO -> TmRutas.INICIO
                        TmRutas.REGISTRAR -> TmRutas.REGISTRAR
                        TmRutas.REGISTROS -> TmRutas.REGISTROS
                        TmRutas.PERFIL -> TmRutas.PERFIL
                        else -> TmRutas.INICIO
                    },
                    onSeleccionar = { destino ->
                        scope.launch { drawerState.close() }
                        if (destino == "login") {
                            onLogoutToLogin()
                        } else {
                            tmNavController.navigate(destino) {
                                popUpTo(TmRutas.INICIO)
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, UnifiedBorderGray)
            ) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = currentRoute == TmRutas.INICIO,
                        onClick = {
                            tmNavController.navigate(TmRutas.INICIO) {
                                popUpTo(TmRutas.INICIO) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = "Inicio") },
                        label = { Text("Inicio") },
                        colors = navItemColors
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
                        label = { Text("Registrar") },
                        colors = navItemColors
                    )
                    NavigationBarItem(
                        selected = currentRoute == TmRutas.REGISTROS,
                        onClick = {
                            tmNavController.navigate(TmRutas.REGISTROS) {
                                popUpTo(TmRutas.INICIO)
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = "Mis Registros") },
                        label = { Text("Registros") },
                        colors = navItemColors
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
                        label = { Text("Perfil") },
                        colors = navItemColors
                    )
                }
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
                    onNavigateBack = onLogoutToLogin,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onNuevoExamen = { tmNavController.navigate(TmRutas.REGISTRAR) },
                    onVerMisRegistros = { tmNavController.navigate(TmRutas.REGISTROS) },
                    onVerPerfil = { tmNavController.navigate(TmRutas.PERFIL) }
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
}
