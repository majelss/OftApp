package com.example.oftapp.ui.screems.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.ui.theme.OftAppAccentBlue
import com.example.oftapp.ui.theme.OftAppBackground
import com.example.oftapp.ui.theme.OftAppBorder
import com.example.oftapp.ui.theme.OftAppNavIndicator
import com.example.oftapp.ui.theme.OftAppTealContainer
import com.example.oftapp.ui.theme.OftAppTealDark
import com.example.oftapp.ui.theme.OftAppTealPrimary
import com.example.oftapp.ui.theme.OftAppTextMuted
import com.example.oftapp.ui.theme.OftAppTextPrimary
import com.example.oftapp.ui.theme.OftAppTextSecondary

/**
 * Pantalla de Panel Principal / Dashboard Oftalmológico (DSY1105).
 * Presenta métricas operativas, accesos rápidos y lista de exámenes recientes.
 */
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    rolUsuario: String = "Médico Oftalmólogo",
    onNavigateToRegistro: () -> Unit = {},
    onNavigateToAtenciones: () -> Unit = {},
    onNavigateToHistorial: () -> Unit = {},
    onNavigateToDetalleExamen: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OftAppBackground,
        topBar = {
            Surface(color = Color.White, shadowElevation = 1.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "OftApp",
                        tint = OftAppTealPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "OFTAPP CLINICAL",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = OftAppTealPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Dashboard Clínico",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OftAppTextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00384D))
                            .clickable { onLogout() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Usuario",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 6.dp
            ) {
                val items = listOf(
                    Triple("Dashboard", Icons.Default.GridView, "Dashboard"),
                    Triple("Atenciones", Icons.AutoMirrored.Filled.Assignment, "Atenciones"),
                    Triple("Registro", Icons.Default.AddBox, "Registro"),
                    Triple("Historial", Icons.Default.History, "Historial")
                )
                items.forEach { (nombre, icono, etiqueta) ->
                    val isSelected = nombre == "Dashboard"
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            when (nombre) {
                                "Atenciones" -> onNavigateToAtenciones()
                                "Registro" -> onNavigateToRegistro()
                                "Historial" -> onNavigateToHistorial()
                            }
                        },
                        icon = { Icon(icono, contentDescription = etiqueta) },
                        label = {
                            Text(
                                text = etiqueta,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = OftAppTealPrimary,
                            selectedTextColor = OftAppTealPrimary,
                            indicatorColor = OftAppNavIndicator,
                            unselectedIconColor = OftAppTextMuted,
                            unselectedTextColor = OftAppTextMuted
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Bienvenida y rol
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEBF5FF)),
                border = BorderStroke(1.dp, Color(0xFFD0E3FA))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SESIÓN CLÍNICA ACTIVA",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OftAppAccentBlue,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Dr. Carlos Mendoza",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OftAppTextPrimary
                        )
                        Text(
                            text = "Rol: $rolUsuario • Sucursal Providencia",
                            fontSize = 12.sp,
                            color = OftAppTextSecondary
                        )
                    }
                    Button(
                        onClick = onNavigateToRegistro,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OftAppTealPrimary)
                    ) {
                        Text("+ Registrar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Métricas Clínicas
            Text(
                text = "Resumen del Flujo Clínico (RF02/RF08)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = OftAppTealDark
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricItem(titulo = "Hoy", valor = "14", color = OftAppTealPrimary, modifier = Modifier.weight(1f))
                MetricItem(titulo = "Validados", valor = "28", color = Color(0xFF16A34A), modifier = Modifier.weight(1f))
                MetricItem(titulo = "Pendientes", valor = "3", color = Color(0xFFEA580C), modifier = Modifier.weight(1f))
                MetricItem(titulo = "Observados", valor = "2", color = Color(0xFFDC2626), modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Accesos Rápidos
            Text(
                text = "Módulos de Atención",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = OftAppTealDark
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickModuleCard(
                    titulo = "Nuevo Examen",
                    subtitulo = "Triage y Registro",
                    icono = Icons.Default.AddBox,
                    onClick = onNavigateToRegistro,
                    modifier = Modifier.weight(1f)
                )
                QuickModuleCard(
                    titulo = "Atenciones",
                    subtitulo = "Listado y Filtros",
                    icono = Icons.AutoMirrored.Filled.Assignment,
                    onClick = onNavigateToAtenciones,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickModuleCard(
                    titulo = "Visor Visual",
                    subtitulo = "Campimetría #8841",
                    icono = Icons.Default.Visibility,
                    onClick = onNavigateToDetalleExamen,
                    modifier = Modifier.weight(1f)
                )
                QuickModuleCard(
                    titulo = "Historial",
                    subtitulo = "Pacientes Ficticios",
                    icono = Icons.Default.History,
                    onClick = onNavigateToHistorial,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Exámenes Recientes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Últimos Exámenes Evaluados",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = OftAppTealDark
                )
                Text(
                    text = "Ver todos",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OftAppAccentBlue,
                    modifier = Modifier.clickable { onNavigateToAtenciones() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Item destacado Mariana González
            RecentExamItem(
                codigo = "#CP-2024-8841",
                paciente = "Mariana González Riquelme",
                examen = "Campimetría Computarizada Humphrey 30-2 (OD)",
                estado = "Validado",
                fecha = "24 Oct 2024 • 10:30",
                onClick = onNavigateToDetalleExamen
            )

            Spacer(modifier = Modifier.height(8.dp))

            RecentExamItem(
                codigo = "#OCT-2024-4112",
                paciente = "Carlos Venegas Soto",
                examen = "OCT Macular Alta Resolución (AO)",
                estado = "Pendiente",
                fecha = "24 Oct 2024 • 09:15",
                onClick = onNavigateToDetalleExamen
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MetricItem(
    titulo: String,
    valor: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, OftAppBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = titulo, fontSize = 11.sp, color = OftAppTextMuted)
            Text(text = valor, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun QuickModuleCard(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, OftAppBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0F2FE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = OftAppTealPrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = titulo, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = OftAppTextPrimary)
                Text(text = subtitulo, fontSize = 10.5.sp, color = OftAppTextMuted)
            }
        }
    }
}

@Composable
private fun RecentExamItem(
    codigo: String,
    paciente: String,
    examen: String,
    estado: String,
    fecha: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, OftAppBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = codigo, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OftAppTealPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (estado == "Validado") Color(0xFFDCFCE7) else Color(0xFFFEF3C7))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = estado,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (estado == "Validado") Color(0xFF15803D) else Color(0xFFB45309)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = paciente, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = OftAppTextPrimary)
                Text(text = examen, fontSize = 11.5.sp, color = OftAppTextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = fecha, fontSize = 10.5.sp, color = OftAppTextMuted)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Ver",
                tint = OftAppTextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
