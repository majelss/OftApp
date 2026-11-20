package com.example.oftapp.ui.screens.tm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.ui.theme.ClinicalClarityColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TmPerfilScreen(
    onNavigateBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ClinicalClarityColors.Background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, tint = ClinicalClarityColors.Primary, contentDescription = "Retroceso")
            }
            Text(
                text = "Perfil de Tecnólogo Médico",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ClinicalClarityColors.Primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN 1: IDENTIDAD PROFESIONAL Y CREDENCIALES ---
        Text("Identidad Profesional", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ClinicalClarityColors.Primary, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ClinicalClarityColors.SurfaceContainer,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar + Nombre
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(72.dp),
                            shape = RoundedCornerShape(36.dp),
                            color = ClinicalClarityColors.PrimaryContainer
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(
                                    Icons.Default.Person,
                                    tint = ClinicalClarityColors.OnPrimary,
                                    contentDescription = "Avatar",
                                    modifier = Modifier.size(42.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Felipe Araya M.", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ClinicalClarityColors.OnSurface)
                            Text("Tecnólogo Médico • Oftalmología", fontSize = 14.sp, color = ClinicalClarityColors.OnSurfaceVariant)
                        }
                    }
                    Icon(Icons.Default.Verified, tint = ClinicalClarityColors.Primary, contentDescription = "Verificado")
                }

                Divider(color = ClinicalClarityColors.OutlineVariant, modifier = Modifier.padding(vertical = 16.dp))

                // Credenciales en grid
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CredencialCard(icon = Icons.Outlined.Badge, label = "RUN Profesional", value = "15.420.891-K", modifier = Modifier.weight(1f))
                    CredencialCard(icon = Icons.Outlined.Shield, label = "Registro SIS", value = "SIS-48201", modifier = Modifier.weight(1f))
                    CredencialCard(icon = Icons.Outlined.Fingerprint, label = "Especialidad", value = "Oftalmología", modifier = Modifier.weight(1f))
                }

                Divider(color = ClinicalClarityColors.OutlineVariant, modifier = Modifier.padding(vertical = 16.dp))

                // Firma electrónica y timbre digital
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FirmaCard(titulo = "Timbre Digital", estado = "Activo", expira = "12/2026", icon = Icons.Filled.Security, modifier = Modifier.weight(1f))
                    FirmaCard(titulo = "Token Firma Electrónica", estado = "Conectado", expira = "Sesión activa", icon = Icons.Filled.Verified, modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN 2: EQUIPOS DEL BOX Y CALIBRACIÓN ---
        Text("Equipos del Box 4 Diagnóstico", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ClinicalClarityColors.Primary, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ClinicalClarityColors.SurfaceContainer,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                EquipoBoxRow(
                    nombre = "Humphrey Field Analyzer 3",
                    modelo = "Modelo 850 • Estrategia 24-2",
                    ultimaCalibracion = "Hoy 08:00 hrs",
                    estado = "Óptimo",
                    colorEstado = ClinicalClarityColors.Tertiary
                )
                Divider(color = ClinicalClarityColors.OutlineVariant, modifier = Modifier.padding(vertical = 8.dp))
                EquipoBoxRow(
                    nombre = "Pentacam HR",
                    modelo = "Topógrafo Scheimpflug",
                    ultimaCalibracion = "Ayer 14:30 hrs",
                    estado = "Óptimo",
                    colorEstado = ClinicalClarityColors.Tertiary
                )
                Divider(color = ClinicalClarityColors.OutlineVariant, modifier = Modifier.padding(vertical = 8.dp))
                EquipoBoxRow(
                    nombre = "OCT Spectralis",
                    modelo = "Heidelberg Engineering",
                    ultimaCalibracion = "Hace 3 días",
                    estado = "Requiere control",
                    colorEstado = Color(0xFFF59E0B)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN 3: MÉTRICAS DE RENDIMIENTO MENSUAL ---
        Text("Rendimiento Técnico (Mes Actual)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ClinicalClarityColors.Primary, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ClinicalClarityColors.SurfaceContainer,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricaCard(icon = Icons.Filled.Biotech, titulo = "Confiabilidad Técnica", valor = "98.7%", subtitulo = "Promedio nacional: 96.2%", modifier = Modifier.weight(1f))
                    MetricaCard(icon = Icons.Filled.Timer, titulo = "Tiempo Promedio/Examen", valor = "12 min", subtitulo = "Objetivo: < 15 min", modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricaCard(icon = Icons.Filled.Speed, titulo = "Exámenes Completados", valor = "142", subtitulo = "Este mes", modifier = Modifier.weight(1f))
                    MetricaCard(icon = Icons.Filled.Devices, titulo = "Sesiones sin errores", valor = "97%", subtitulo = "Calidad de captura", modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN 4: PREFERENCIAS DE ADQUISICIÓN ---
        Text("Preferencias de Adquisición", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ClinicalClarityColors.Primary, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ClinicalClarityColors.SurfaceContainer,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SwitchRow(
                    icon = Icons.Filled.DarkMode,
                    titulo = "Modo cabina oscura",
                    subtitulo = "Reduce fatiga visual en capturas prolongadas",
                    valorInicial = true
                )
                Divider(color = ClinicalClarityColors.OutlineVariant, modifier = Modifier.padding(vertical = 8.dp))
                SwitchRow(
                    icon = Icons.Filled.VolumeUp,
                    titulo = "Alertas sonoras de guía",
                    subtitulo = "Tonos de fijación y pérdida de fijación",
                    valorInicial = true
                )
                Divider(color = ClinicalClarityColors.OutlineVariant, modifier = Modifier.padding(vertical = 8.dp))
                SwitchRow(
                    icon = Icons.Filled.Settings,
                    titulo = "Auto-guardado de borradores",
                    subtitulo = "Cada 30 segundos durante captura",
                    valorInicial = true
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN 5: ACCIONES DE GESTIÓN ---
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { /* Cerrar turno de box */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ClinicalClarityColors.Primary,
                    contentColor = ClinicalClarityColors.OnPrimary
                )
            ) {
                Icon(Icons.Filled.PowerSettingsNew, contentDescription = "Cerrar turno")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Turno de Box", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ClinicalClarityColors.ErrorContainer,
                    contentColor = ClinicalClarityColors.Error
                )
            ) {
                Icon(Icons.Filled.Logout, contentDescription = "Logout")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Footer legal
        Text(
            "OFTAPP CLÍNICA v2.4.1 • Tecnólogo Médico Oftalmológico\nCertificación ISO 15189 • Cumplimiento Ley 20.584",
            fontSize = 10.sp,
            color = ClinicalClarityColors.OnSurfaceVariant.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
    }
}

@Composable
private fun CredencialCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        color = ClinicalClarityColors.Surface,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = ClinicalClarityColors.Primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, fontSize = 10.sp, color = ClinicalClarityColors.OnSurfaceVariant, textAlign = TextAlign.Center)
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ClinicalClarityColors.OnSurface, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun FirmaCard(
    titulo: String,
    estado: String,
    expira: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        color = ClinicalClarityColors.Surface,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = ClinicalClarityColors.Primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(titulo, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = ClinicalClarityColors.OnSurface)
                Text(estado, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ClinicalClarityColors.Tertiary)
                Text(expira, fontSize = 10.sp, color = ClinicalClarityColors.OnSurfaceVariant)
            }
        }
    }
}

@Composable
private fun MetricaCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    valor: String,
    subtitulo: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = ClinicalClarityColors.Surface,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = ClinicalClarityColors.Primary, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(titulo, fontSize = 11.sp, color = ClinicalClarityColors.OnSurfaceVariant, textAlign = TextAlign.Center)
            Text(valor, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ClinicalClarityColors.Primary, textAlign = TextAlign.Center)
            Text(subtitulo, fontSize = 9.sp, color = ClinicalClarityColors.OnSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun EquipoBoxRow(
    nombre: String,
    modelo: String,
    ultimaCalibracion: String,
    estado: String,
    colorEstado: androidx.compose.ui.graphics.Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(nombre, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ClinicalClarityColors.OnSurface)
            Text(modelo, fontSize = 11.sp, color = ClinicalClarityColors.OnSurfaceVariant)
            Text("Última calibración: $ultimaCalibracion", fontSize = 10.sp, color = ClinicalClarityColors.OnSurfaceVariant.copy(alpha = 0.7f))
        }
        Surface(color = colorEstado.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)) {
            Text(estado, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colorEstado, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
        }
    }
}

@Composable
private fun SwitchRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    subtitulo: String,
    valorInicial: Boolean
) {
    var checked by remember { mutableStateOf(valorInicial) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = ClinicalClarityColors.Primary, modifier = Modifier.size(24.dp).padding(end = 12.dp))
            Column {
                Text(titulo, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = ClinicalClarityColors.OnSurface)
                Text(subtitulo, fontSize = 11.sp, color = ClinicalClarityColors.OnSurfaceVariant)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = ClinicalClarityColors.Primary,
                checkedTrackColor = ClinicalClarityColors.PrimaryContainer,
                uncheckedThumbColor = ClinicalClarityColors.OnSurfaceVariant,
                uncheckedTrackColor = ClinicalClarityColors.OutlineVariant
            )
        )
    }
}