package com.example.oftapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val UnifiedPrimaryBlue = Color(0xFF006689)
val UnifiedDarkNavy = Color(0xFF0D1C2F)
val UnifiedMutedSlate = Color(0xFF475569)
val UnifiedIndicatorBlue = Color(0xFFD6E4F0)
val UnifiedIconBoxBg = Color(0xFFDDE9FF)
val UnifiedBorderGray = Color(0xFFE2E8F0)

/**
 * CompositionLocal global para cerrar sesión desde cualquier barra superior de la aplicación.
 */
val LocalLogoutAction = staticCompositionLocalOf<(() -> Unit)?> { null }

/**
 * Barra superior unificada (TopAppBar) para todas las pantallas de OftApp
 * (Médico, Paciente y Administrador). Garantiza consistencia visual y de navegación.
 */
@Composable
fun OftAppUnifiedTopBar(
    title: String,
    badgeSubtitle: String = "OFTAPP CLINICAL",
    onBackClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
    onSearchClick: (() -> Unit)? = null,
    showNotifications: Boolean = true,
    onNotificationClick: (() -> Unit)? = null,
    onProfileClick: (() -> Unit)? = null,
    onLogoutClick: (() -> Unit)? = null
) {
    var menuPerfilExpandido by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 2.dp,
        border = BorderStroke(0.5.dp, UnifiedBorderGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                when {
                    onBackClick != null -> {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = UnifiedDarkNavy
                            )
                        }
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                    onMenuClick != null -> {
                        IconButton(
                            onClick = onMenuClick,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú principal",
                                tint = UnifiedDarkNavy
                            )
                        }
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(UnifiedIconBoxBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = UnifiedPrimaryBlue,
                        modifier = Modifier.size(21.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = badgeSubtitle,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnifiedPrimaryBlue,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnifiedDarkNavy,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.semantics { heading() }
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onSearchClick != null) {
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = UnifiedDarkNavy,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                if (showNotifications) {
                    IconButton(
                        onClick = { onNotificationClick?.invoke() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notificaciones",
                            tint = UnifiedDarkNavy,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                }

                // Botón visible de Cerrar Sesión / Salir en todas las pantallas
                val globalLogout = LocalLogoutAction.current
                val accionSalir = onLogoutClick ?: globalLogout ?: onProfileClick ?: onBackClick
                if (accionSalir != null) {
                    Surface(
                        onClick = accionSalir,
                        shape = RoundedCornerShape(50),
                        color = Color(0xFFFEE2E2),
                        border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                        modifier = Modifier.padding(start = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Cerrar Sesión",
                                tint = Color(0xFFBA1A1A),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Salir",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBA1A1A)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Barra de navegación inferior unificada para el flujo Médico / Especialista (4 pestañas).
 */
@Composable
fun OftAppMedicoBottomBar(
    selectedTab: String,
    onNavigateToDashboard: () -> Unit,
    onNavigateToAtenciones: () -> Unit,
    onNavigateToRegistro: () -> Unit,
    onNavigateToHistorial: () -> Unit
) {
    val items = listOf(
        Triple("Dashboard", Icons.Default.GridView, "Dashboard"),
        Triple("Atenciones", Icons.AutoMirrored.Filled.Assignment, "Atenciones"),
        Triple("Registro", Icons.Default.AddBox, "Registro"),
        Triple("Historial", Icons.Default.History, "Historial")
    )

    Surface(
        color = Color.White,
        shadowElevation = 8.dp,
        border = BorderStroke(1.dp, UnifiedBorderGray)
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            items.forEach { (key, icon, label) ->
                val isSelected = selectedTab.equals(key, ignoreCase = true)
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            when (key) {
                                "Dashboard" -> onNavigateToDashboard()
                                "Atenciones" -> onNavigateToAtenciones()
                                "Registro" -> onNavigateToRegistro()
                                "Historial" -> onNavigateToHistorial()
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = label,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UnifiedPrimaryBlue,
                        selectedTextColor = UnifiedPrimaryBlue,
                        indicatorColor = UnifiedIndicatorBlue,
                        unselectedIconColor = UnifiedMutedSlate,
                        unselectedTextColor = UnifiedMutedSlate
                    )
                )
            }
        }
    }
}

/**
 * Barra de navegación inferior unificada para el flujo Paciente (5 pestañas).
 */
@Composable
fun OftAppPacienteBottomBar(
    selectedTab: String,
    onNavegarItemBottomBar: (String) -> Unit
) {
    val items = listOf(
        Triple("inicio", Icons.Default.Home, "Inicio"),
        Triple("examenes", Icons.AutoMirrored.Filled.Assignment, "Exámenes"),
        Triple("historial", Icons.Default.History, "Historial"),
        Triple("compartir", Icons.Default.QrCode, "Compartir"),
        Triple("perfil", Icons.Default.PersonOutline, "Perfil")
    )

    Surface(
        color = Color.White,
        shadowElevation = 8.dp,
        border = BorderStroke(1.dp, UnifiedBorderGray)
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            items.forEach { (key, icon, label) ->
                val isSelected = selectedTab.equals(key, ignoreCase = true)
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            onNavegarItemBottomBar(key)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UnifiedPrimaryBlue,
                        selectedTextColor = UnifiedPrimaryBlue,
                        indicatorColor = UnifiedIndicatorBlue,
                        unselectedIconColor = UnifiedMutedSlate,
                        unselectedTextColor = UnifiedMutedSlate
                    )
                )
            }
        }
    }
}

/**
 * Botón de acción flotante unificado (FAB) para las pantallas de Médico.
 */
@Composable
fun OftAppUnifiedFab(
    onClick: () -> Unit,
    contentDescription: String = "Registrar Nuevo Examen"
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = UnifiedPrimaryBlue,
        contentColor = Color.White,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}
