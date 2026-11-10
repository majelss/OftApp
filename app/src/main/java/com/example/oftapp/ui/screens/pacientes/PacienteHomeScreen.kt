package com.example.oftapp.ui.screens.pacientes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores extraídos exactamente de la pantalla del paciente
private val ScreenBg = Color(0xFFF2F6FC)
private val DarkTitleColor = Color(0xFF0D253A)
private val BrandTealBlue = Color(0xFF006689)
private val LightBlueCard = Color(0xFFEDF5FF)
private val BtnSecondaryBg = Color(0xFFE2EFFD)
private val GreenBadgeBg = Color(0xFFE2F7ED)
private val GreenBadgeText = Color(0xFF1E8E5A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteHomeScreen(
    nombrePaciente: String = "Mariana",
    rutPaciente: String = "15.420.891-K",
    onVerExamenesArchivados: () -> Unit = {},
    onGenerarQR: () -> Unit = {},
    onDescargarPDF: () -> Unit = {},
    onCompartirExamen: () -> Unit = {},
    onNavegarItemBottomBar: (String) -> Unit = {}
) {
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
                            text = "Inicio",
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
                    // Avatar genérico de usuario
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
                    selected = true,
                    onClick = { onNavegarItemBottomBar("inicio") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio", fontSize = 10.5.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandTealBlue,
                        selectedTextColor = BrandTealBlue,
                        indicatorColor = LightBlueCard
                    )
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
                    selected = false,
                    onClick = { onNavegarItemBottomBar("perfil") },
                    icon = { Icon(Icons.Default.PersonOutline, contentDescription = "Perfil") },
                    label = { Text("Perfil", fontSize = 10.5.sp) }
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
            // --- 1. SALUDO Y RUT DEL PACIENTE ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "• RUT: $rutPaciente",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandTealBlue
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "¡Hola, $nombrePaciente!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTitleColor
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Bienvenida a tus exámenes\noftalmológicos",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            lineHeight = 17.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(Color(0xFFE1F0FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = BrandTealBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // --- 2. INFORMACIÓN CLÍNICA AL DÍA ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = LightBlueCard)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(Color(0xFF00BCD4), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Información clínica al día",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTitleColor
                        )
                        Text(
                            text = "Tus exámenes están actualizados y sincronizados",
                            fontSize = 11.5.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // --- 3. ÚLTIMO RESULTADO DISPONIBLE ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ÚLTIMO RESULTADO DISPONIBLE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            letterSpacing = 0.5.sp
                        )
                        Surface(
                            color = GreenBadgeBg,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = GreenBadgeText,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Validado",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenBadgeText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Campimetría Computarizada\nVisual",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTitleColor,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Detalles del examen
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = LightBlueCard)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.FilterCenterFocus, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Ojo evaluado: ", fontSize = 12.5.sp, color = Color.DarkGray)
                                Surface(
                                    color = BrandTealBlue,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "Ambos ojos (AO)",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("12 de Octubre, 2024 • 10:30 hrs", fontSize = 12.sp, color = Color.DarkGray)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MedicalServices, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Dra. Sofía Valenzuela ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                                Text("(Oftalmóloga)", fontSize = 12.sp, color = Color.Gray)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Domain, contentDescription = null, tint = BrandTealBlue, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Clínica Oftalmológica Andes • Box 4", fontSize = 12.sp, color = Color.DarkGray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Área visual simulada del informe gráfico
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFE5EBF2)),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Informe Campimetría",
                                tint = BrandTealBlue,
                                modifier = Modifier.size(44.dp)
                            )
                            Text("Gráfico de Campo Visual", fontSize = 11.sp, color = Color.Gray)
                        }

                        Surface(
                            modifier = Modifier.padding(8.dp),
                            color = Color(0xCC111111),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Informe gráfico disponible",
                                    fontSize = 10.5.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Botones principales de acción
                    Button(
                        onClick = onDescargarPDF,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandTealBlue)
                    ) {
                        Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Descargar PDF", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onCompartirExamen,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BtnSecondaryBg,
                            contentColor = BrandTealBlue
                        )
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Compartir examen", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                    }
                }
            }

            // --- 4. ACCIONES RÁPIDAS ---
            Text(
                text = "Acciones Rápidas",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTitleColor
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onVerExamenesArchivados() },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(LightBlueCard, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderSpecial,
                            contentDescription = null,
                            tint = BrandTealBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ver mis 4 exámenes archivados",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTitleColor
                        )
                        Text(
                            text = "Historial clínico digital consolidado",
                            fontSize = 11.5.sp,
                            color = Color.Gray
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onGenerarQR() },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE0F7FA), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = Color(0xFF00ACC1),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Generar código QR para mi médico",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTitleColor
                        )
                        Text(
                            text = "Lectura directa en consulta sin papel",
                            fontSize = 11.5.sp,
                            color = Color.Gray
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // --- 5. ASISTENCIA TELEFÓNICA ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = null,
                        tint = BrandTealBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "¿Necesitas asistencia con tus lecturas?",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandTealBlue
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Llama gratis al Centro de Apoyo Oftalmológico al\n800 600 320",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp
                )
            }
        }
    }
}