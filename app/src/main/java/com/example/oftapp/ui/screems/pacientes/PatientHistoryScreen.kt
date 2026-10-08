package com.example.oftapp.ui.screems.pacientes

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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.ui.theme.OftAppAccentBlue
import com.example.oftapp.ui.theme.OftAppBackground
import com.example.oftapp.ui.theme.OftAppBorder
import com.example.oftapp.ui.theme.OftAppFichaBlue
import com.example.oftapp.ui.theme.OftAppFichaIconBg
import com.example.oftapp.ui.theme.OftAppNavIndicator
import com.example.oftapp.ui.theme.OftAppTealContainer
import com.example.oftapp.ui.theme.OftAppTealDark
import com.example.oftapp.ui.theme.OftAppTealPrimary
import com.example.oftapp.ui.theme.OftAppTextMuted
import com.example.oftapp.ui.theme.OftAppTextPrimary
import com.example.oftapp.ui.theme.OftAppTextSecondary

data class HitoHistorial(
    val fecha: String,
    val examen: String,
    val ojo: String,
    val resultado: String,
    val profesional: String,
    val estado: String
)

/**
 * Pantalla de Historial Clínico de Exámenes Anteriores (RF06).
 * Permite trazabilidad longitudinal por paciente ficticio para comparación académica.
 */
@Composable
fun PatientHistoryScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetalle: () -> Unit = {},
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToAtenciones: () -> Unit = {},
    onNavigateToRegistro: () -> Unit = {}
) {
    val hitos = listOf(
        HitoHistorial(
            fecha = "24 Oct 2024",
            examen = "Campimetría Computarizada Humphrey 30-2",
            ojo = "OD",
            resultado = "Escotoma relativo paracentral superonasal leve. VFI 97%.",
            profesional = "Dr. Carlos Morales",
            estado = "Validado"
        ),
        HitoHistorial(
            fecha = "15 Jun 2024",
            examen = "Curva de Tensión Ocular Diurna (Tonometría)",
            ojo = "AO",
            resultado = "PIO estable: OD 14 mmHg / OI 15 mmHg. Sin picos.",
            profesional = "Dra. Paula Rivas",
            estado = "Validado"
        ),
        HitoHistorial(
            fecha = "10 Ene 2024",
            examen = "Tomografía de Coherencia Óptica (OCT Macular)",
            ojo = "AO",
            resultado = "Espesor foveal normal. Capa de fibras sin adelgazamiento patológico.",
            profesional = "Dr. Carlos Mendoza",
            estado = "Validado"
        ),
        HitoHistorial(
            fecha = "02 Ago 2023",
            examen = "Campimetría Humphrey 24-2 (Estudio Basal)",
            ojo = "OD",
            resultado = "Límites normales. Campo visual simétrico bilateral.",
            profesional = "Dr. Carlos Morales",
            estado = "Validado"
        )
    )

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
                        contentDescription = null,
                        tint = OftAppTealPrimary,
                        modifier = Modifier.size(24.dp)
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
                            text = "Historial del Paciente",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OftAppTextPrimary
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 6.dp) {
                val items = listOf(
                    Triple("Dashboard", Icons.Default.GridView, "Dashboard"),
                    Triple("Atenciones", Icons.AutoMirrored.Filled.Assignment, "Atenciones"),
                    Triple("Registro", Icons.Default.AddBox, "Registro"),
                    Triple("Historial", Icons.Default.History, "Historial")
                )
                items.forEach { (nombre, icono, etiqueta) ->
                    val isSelected = nombre == "Historial"
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            when (nombre) {
                                "Dashboard" -> onNavigateToDashboard()
                                "Atenciones" -> onNavigateToAtenciones()
                                "Registro" -> onNavigateToRegistro()
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
            // Tarjeta de Ficha Paciente
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = OftAppFichaBlue,
                border = BorderStroke(1.dp, Color(0xFFD4E7FA))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(OftAppFichaIconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Badge, contentDescription = null, tint = Color(0xFF006699), modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "FICHA CLÍNICA ACTIVA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OftAppTextMuted)
                        Text(text = "Mariana González Riquelme", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OftAppTextPrimary)
                        Text(text = "RUT: 14.821.903-2 • Edad: 46 años", fontSize = 12.sp, color = OftAppAccentBlue)
                    }
                    Icon(Icons.Default.CheckCircle, contentDescription = "Validada", tint = OftAppAccentBlue, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Resumen de Evolución Clínica
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OftAppBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timeline, contentDescription = null, tint = OftAppTealPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Evolución y Trazabilidad (RF06)", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = OftAppTealDark)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Paciente en seguimiento preventivo de PIO. Se observa estabilidad funcional con leve defecto relativo superonasal detectado en el examen reciente.",
                        fontSize = 12.sp,
                        color = OftAppTextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Línea de Tiempo de Exámenes",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = OftAppTealDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Hitos de la línea de tiempo
            hitos.forEachIndexed { index, hito ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onNavigateToDetalle() },
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
                                Text(text = hito.fecha, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OftAppTealPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(OftAppTealContainer)
                                        .padding(horizontal = 6.dp, vertical = 1.dp)
                                ) {
                                    Text(text = hito.ojo, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OftAppTealPrimary)
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = hito.examen, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = OftAppTextPrimary)
                            Text(text = hito.resultado, fontSize = 11.5.sp, color = OftAppTextSecondary)
                            Text(text = "Profesional: ${hito.profesional}", fontSize = 10.5.sp, color = OftAppTextMuted)
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Ver Detalle",
                            tint = OftAppTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
