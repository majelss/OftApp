package com.example.oftapp.ui.screens.tm.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.ui.components.OftAppUnifiedTopBar
import com.example.oftapp.ui.theme.ClinicalClarityColors

@Composable
fun TmInicioScreen(
    onNuevoExamen: () -> Unit = {},
    onVerMisRegistros: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onVerPerfil: () -> Unit = {}
) {
    val bgSurface = Color(0xFFF8F9FF)
    val cardBg = Color(0xFFFFFFFF)
    val primaryColor = Color(0xFF006385)
    val secondaryColor = Color(0xFF006970)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgSurface)
    ) {
        // --- TOP APP BAR CLÍNICO UNIFICADO ---
        OftAppUnifiedTopBar(
            title = "Portal Tecnólogo Médico",
            badgeSubtitle = "OFTAPP CLINICAL • TM",
            onProfileClick = onVerPerfil,
            onLogoutClick = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
        // --- SALUDO & TURNO CARD ---
        Surface(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            color = cardBg,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF3F484E))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Miércoles, 16 de Octubre de 2024", fontSize = 11.sp, color = Color(0xFF3F484E))
                    }
                    Surface(color = Color(0xFFE0F2FE), shape = RoundedCornerShape(12.dp)) {
                        Text("SIS: 48201", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = primaryColor, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("¡Buen dia, T.M. Felipe Araya!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                Text("Área de Oftalmología Diagnóstica", fontSize = 12.sp, color = Color(0xFF3F484E))
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Business, contentDescription = null, modifier = Modifier.size(14.dp), tint = primaryColor)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Turno Mañana • Box 4 Diagnóstico", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = primaryColor)
                }
            }
        }

        // --- RESUMEN DE JORNADA ---
        Text("Resumen de Jornada", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F), modifier = Modifier.padding(bottom = 8.dp))
        
        // Tarjeta Alerta Pendientes
        Surface(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            color = Color(0xFFEFF6FF),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(color = Color(0xFFFEF3C7), shape = CircleShape, modifier = Modifier.size(36.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.Timer, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("3", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                        Text("Exámenes pendientes por registrar hoy", fontSize = 12.sp, color = Color(0xFF3F484E))
                    }
                }
                Text("!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
            }
        }

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(modifier = Modifier.weight(1f), color = Color(0xFFEFF4FF), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Completados", fontSize = 11.sp, color = Color(0xFF3F484E))
                    Text("5", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                    Text("registrados hoy", fontSize = 10.sp, color = Color(0xFF3F484E))
                }
            }
            Surface(modifier = Modifier.weight(1f), color = Color(0xFFEFF4FF), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Validados", fontSize = 11.sp, color = Color(0xFF3F484E))
                    Text("2", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = secondaryColor)
                    Text("por oftalmología", fontSize = 10.sp, color = Color(0xFF3F484E))
                }
            }
        }

        // --- BOTÓN NUEVO EXAMEN ---
        Button(
            onClick = onNuevoExamen,
            modifier = Modifier.fillMaxWidth().height(50.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Icon(Icons.Default.AddCircleOutline, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Registrar nuevo examen", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        // --- ASIGNADOS PENDIENTES HOY ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Asignados pendientes hoy", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
            Surface(color = Color(0xFFE2E8F0), shape = CircleShape) {
                Text("3", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
            }
        }

        // Paciente 1
        PacienteAsignadoCard("Mariana González R.", "15.420.891-K", "Campimetría Humphrey", "AO (Ambos Ojos)", "10:30 hrs", onNuevoExamen)
        Spacer(modifier = Modifier.height(12.dp))
        // Paciente 2
        PacienteAsignadoCard("Carlos Mendoza Silva", "12.873.402-3", "Topografía Pentacam", "OD (Ojo Derecho)", "11:15 hrs", onNuevoExamen)
        Spacer(modifier = Modifier.height(12.dp))
        // Paciente 3
        PacienteAsignadoCard("Elena Morales Castro", "18.234.901-5", "Curva Tensión Aplanática", "OI (Ojo Izquierdo)", "12:00 hrs", onNuevoExamen)

        Spacer(modifier = Modifier.height(20.dp))

        // --- ACCIONES RÁPIDAS ---
        Text("Acciones Rápidas", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F), modifier = Modifier.padding(bottom = 8.dp))

        Surface(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { onVerMisRegistros() },
            color = cardBg,
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Folder, contentDescription = null, tint = primaryColor)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Ver mis registros guardados", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Acceso a historial técnico local", fontSize = 11.sp, color = Color(0xFF3F484E))
                    }
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF3F484E))
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            color = cardBg,
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Devices, contentDescription = null, tint = secondaryColor)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Equipos Box 4", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Calibrado hoy a las 08:00 hrs.", fontSize = 11.sp, color = Color(0xFF3F484E))
                    }
                }
                Surface(color = Color(0xFFDCFCE7), shape = RoundedCornerShape(8.dp)) {
                    Text("Óptimo", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }
        }

        // Footer disclaimer
        Surface(color = Color(0xFFEFF4FF), shape = RoundedCornerShape(12.dp)) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                Icon(Icons.Outlined.Lock, contentDescription = null, tint = primaryColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "ALCANCE TECNÓLOGO MÉDICO OFTALMOLÓGICO\nMódulo de captura y digitalización diagnóstica. La validación médica final corresponde al especialista tratante.",
                    fontSize = 10.sp,
                    color = Color(0xFF3F484E)
                )
            }
        }
        }
    }
}

@Composable
fun PacienteAsignadoCard(
    nombre: String,
    rut: String,
    examen: String,
    ojo: String,
    hora: String,
    onStart: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(nombre, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                    Text("RUT: $rut", fontSize = 11.sp, color = Color(0xFF3F484E))
                }
                Surface(color = Color(0xFFFEF3C7), shape = RoundedCornerShape(12.dp)) {
                    Text("Pendiente", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Surface(color = Color(0xFFEFF4FF), shape = RoundedCornerShape(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(examen, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF006385))
                    Surface(color = Color(0xFF006385), shape = RoundedCornerShape(4.dp)) {
                        Text(ojo, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Schedule, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFF3F484E))
                Spacer(modifier = Modifier.width(4.dp))
                Text("$hora • Box 4 Diagnóstico", fontSize = 11.sp, color = Color(0xFF3F484E))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(42.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006385))
            ) {
                Icon(Icons.Default.PlayCircleOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Iniciar registro", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
