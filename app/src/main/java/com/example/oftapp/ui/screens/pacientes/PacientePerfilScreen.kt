package com.example.oftapp.ui.screens.pacientes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores del diseño de Perfil
private val ScreenBg = Color(0xFFF2F6FC)
private val DarkTitleColor = Color(0xFF0D253A)
private val BrandTealBlue = Color(0xFF006689)
private val CardBg = Color.White
private val ContainerLightBlue = Color(0xFFEDF5FF)
private val BadgeVerifiedBg = Color(0xFFE2F0FD)
private val BadgeVerifiedText = Color(0xFF0288D1)
private val BtnRedLogout = Color(0xFFC62828)
private val BtnSecondaryBg = Color(0xFFE8F1FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacientePerfilScreen(
    nombrePaciente: String = "Mariana González R.",
    rutPaciente: String = "15.420.891-K",
    correoPaciente: String = "mariana.gonzalez@ejemp...",
    previsionPaciente: String = "Fonasa B / Isapre",
    onCerrarSesion: () -> Unit = {},
    onNavegarItemBottomBar: (String) -> Unit = {}
) {
    var tamanoTextoSeleccionado by remember { mutableStateOf("A+") }
    var altoContrasteActivo by remember { mutableStateOf(true) }
    var lectorPantallaActivo by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "OFTAPP CLÍNICA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandTealBlue,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Perfil",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTitleColor
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notificaciones",
                            tint = DarkTitleColor
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFB0BEC5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ScreenBg)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 6.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavegarItemBottomBar("inicio") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio", fontSize = 10.5.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavegarItemBottomBar("examenes") },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "Mis exámenes") },
                    label = { Text("Mis exáme...", fontSize = 10.5.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavegarItemBottomBar("historial") },
                    icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
                    label = { Text("Historial", fontSize = 10.5.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavegarItemBottomBar("compartir") },
                    icon = { Icon(Icons.Default.QrCode, contentDescription = "Compartir") },
                    label = { Text("Compartir", fontSize = 10.5.sp) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { onNavegarItemBottomBar("perfil") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil", fontSize = 10.5.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandTealBlue,
                        selectedTextColor = BrandTealBlue,
                        indicatorColor = ContainerLightBlue
                    )
                )
            }
        },
        containerColor = ScreenBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // --- 1. DATOS PERSONALES DEL PACIENTE ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Avatar genérico con badge verificado
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFB0BEC5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(BrandTealBlue, CircleShape)
                                    .border(1.5.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Surface(
                                color = BadgeVerifiedBg,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = BadgeVerifiedText,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Cuenta Paciente Verificada",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BadgeVerifiedText
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = nombrePaciente,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTitleColor
                            )
                            Text(
                                text = "RUT: $rutPaciente",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Información protegida
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ContainerLightBlue)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Email, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Correo registrado:", fontSize = 12.sp, color = Color.DarkGray)
                                }
                                Text(correoPaciente, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkTitleColor)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.MedicalServices, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Previsión de salud:", fontSize = 12.sp, color = Color.DarkGray)
                                }
                                Text(previsionPaciente, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkTitleColor)
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Datos clínicos protegidos según Ley N° 20.584. No editables desde la app.",
                                    fontSize = 10.5.sp,
                                    color = Color.Gray,
                                    lineHeight = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            // --- 2. OPCIONES DE ACCESIBILIDAD Y VISIÓN ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(ContainerLightBlue, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = BrandTealBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Opciones de Accesibilidad y Visión",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTitleColor
                            )
                            Text(
                                text = "Adaptaciones visuales para baja visión y confort óptico",
                                fontSize = 11.5.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Tamaño de texto en pantalla ", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = DarkTitleColor)
                        Text("ACTIVO: GRANDE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BrandTealBlue)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Selector de tamaño A, A+, A++
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Triple("A", "Normal", "13"),
                            Triple("A+", "Grande\n(Recomendado)", "16"),
                            Triple("A++", "Muy grande", "19")
                        ).forEach { (letra, label, _) ->
                            val esSel = tamanoTextoSeleccionado == letra
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(65.dp)
                                    .clickable { tamanoTextoSeleccionado = letra },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (esSel) BrandTealBlue else ContainerLightBlue
                                ),
                                border = if (esSel) BorderStroke(2.dp, BrandTealBlue) else null
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = letra,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (esSel) Color.White else DarkTitleColor
                                    )
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        textAlign = TextAlign.Center,
                                        color = if (esSel) Color.White.copy(alpha = 0.9f) else Color.Gray,
                                        lineHeight = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Vista previa de lectura diagnóstica
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = ContainerLightBlue)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Vista previa de lectura diagnóstica:", fontSize = 10.5.sp, color = BrandTealBlue, fontWeight = FontWeight.Bold)
                            Text(
                                text = "\"Agudeza visual con corrección preservada en ojo derecho.\"",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkTitleColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Switches de accesibilidad
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ContainerLightBlue)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Contrast, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Modo Alto Contraste visual", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkTitleColor)
                                Text("Aumenta ratios cromáticos a >7:1 para legibilidad clínica", fontSize = 11.sp, color = Color.Gray, lineHeight = 14.sp)
                            }
                            Switch(
                                checked = altoContrasteActivo,
                                onCheckedChange = { altoContrasteActivo = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = BrandTealBlue)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ContainerLightBlue)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Lector de pantalla y asistencia por voz", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkTitleColor)
                                Text("Anuncia diagnósticos y prescripciones con TalkBack", fontSize = 11.sp, color = Color.Gray, lineHeight = 14.sp)
                            }
                            Switch(
                                checked = lectorPantallaActivo,
                                onCheckedChange = { lectorPantallaActivo = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = BrandTealBlue)
                            )
                        }
                    }
                }
            }

            // --- 3. ALMACENAMIENTO Y CONEXIÓN ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(ContainerLightBlue, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.WifiOff, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Almacenamiento y Conexión", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DarkTitleColor)
                            Text("Disponibilidad segura de informes sin señal de internet", fontSize = 11.5.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = ContainerLightBlue)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Exámenes disponibles sin internet", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = DarkTitleColor)
                                Text("4 exámenes descargados (8.2 MB)", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = BrandTealBlue)
                            }
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(20.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BtnSecondaryBg, contentColor = BrandTealBlue)
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Liberar espacio en dispositivo", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // --- 4. AYUDA Y SOPORTE CLÍNICO ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(ContainerLightBlue, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.HelpOutline, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Ayuda y Soporte Clínico", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DarkTitleColor)
                            Text("Recursos explicativos para pacientes", fontSize = 11.5.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = ContainerLightBlue)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Guía de ayuda para pacientes", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkTitleColor)
                            }
                            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(14.dp))
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = ContainerLightBlue)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Gavel, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Términos y consentimiento informado", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkTitleColor)
                            }
                            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            // --- 5. BOTÓN CERRAR SESIÓN ---
            Button(
                onClick = onCerrarSesion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BtnRedLogout)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar sesión segura", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "Al salir se solicitará verificación biométrica o PIN en el próximo ingreso.",
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}