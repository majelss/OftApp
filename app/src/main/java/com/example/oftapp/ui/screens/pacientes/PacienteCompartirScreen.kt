package com.example.oftapp.ui.screens.pacientes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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

// Colores del diseño de la pantalla Compartir
private val ScreenBg = Color(0xFFF2F6FC)
private val DarkTitleColor = Color(0xFF0D253A)
private val BrandTealBlue = Color(0xFF006689)
private val CardBg = Color.White
private val ContainerLightBlue = Color(0xFFEDF5FF)
private val BadgeSelectedBg = Color(0xFFE2F0FD)
private val BadgeSelectedText = Color(0xFF006689)
private val EyeBadgeBg = Color(0xFF285C8F)
private val InfoNoticeBg = Color(0xFFE0F7FA)
private val InfoNoticeText = Color(0xFF00838F)

data class ExamenCompartirItem(
    val id: Long,
    val titulo: String,
    val ojo: String,
    val fechaMedico: String,
    val seleccionadoInicial: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteCompartirScreen(
    onNavegarItemBottomBar: (String) -> Unit = {}
) {
    var examenes by remember {
        mutableStateOf(
            listOf(
                ExamenCompartirItem(1, "Campimetría Computarizada", "AO", "12 Oct 2024 • Dr. Alejandro Morales", true),
                ExamenCompartirItem(2, "Topografía Corneal Pentacam", "OD", "28 Ago 2024 • Dra. Silvia Benítez", true),
                ExamenCompartirItem(3, "Curva de Tensión Aplanática", "OI", "15 Jun 2024 • Dr. Alejandro Morales", false),
                ExamenCompartirItem(4, "Agudeza Visual y Refracción", "AO", "04 Ene 2024 • Lic. Roberto Casal", false)
            )
        )
    }

    var validezSeleccionada by remember { mutableStateOf("Expira en: 48 horas (Recomendado)") }
    var menuValidezExpandido by remember { mutableStateOf(false) }

    val totalSeleccionados = examenes.count { it.seleccionadoInicial }

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
                            text = "Compartir",
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
                    selected = true,
                    onClick = { onNavegarItemBottomBar("compartir") },
                    icon = { Icon(Icons.Default.QrCode, contentDescription = "Compartir") },
                    label = { Text("Compartir", fontSize = 10.5.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandTealBlue,
                        selectedTextColor = BrandTealBlue,
                        indicatorColor = ContainerLightBlue
                    )
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
            // --- HEADER AZUL CLARO CON CANDADO ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ContainerLightBlue)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color.White, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LockReset,
                            contentDescription = null,
                            tint = BrandTealBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Compartir exámenes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTitleColor
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Comparte tus resultados de forma segura con otro médico o clínica oftalmológica.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // --- SECCIÓN 1: ELIGE LOS EXÁMENES A COMPARTIR (SIN NÚMERO "1") ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Elige los exámenes a compartir",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandTealBlue
                )

                Surface(
                    color = BadgeSelectedBg,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "$totalSeleccionados seleccionados",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BadgeSelectedText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // TARJETAS SELECCIONABLES DE EXÁMENES
            examenes.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            examenes = examenes.map {
                                if (it.id == item.id) it.copy(seleccionadoInicial = !it.seleccionadoInicial)
                                else it
                            }
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item.seleccionadoInicial,
                            onCheckedChange = { checked ->
                                examenes = examenes.map {
                                    if (it.id == item.id) it.copy(seleccionadoInicial = checked)
                                    else it
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = BrandTealBlue,
                                uncheckedColor = Color.Gray
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = item.titulo,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkTitleColor
                                )

                                Surface(
                                    color = EyeBadgeBg,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = item.ojo,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(3.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = item.fechaMedico,
                                    fontSize = 11.5.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // --- SECCIÓN 2: VIGENCIA Y SEGURIDAD DEL ACCESO (SIN NÚMERO "2") ---
            Text(
                text = "Vigencia y seguridad del acceso",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = BrandTealBlue
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = BrandTealBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Tiempo de validez del enlace:",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTitleColor
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // DROPDOWN DE VIGENCIA
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { menuValidezExpandido = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = ContainerLightBlue)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = validezSeleccionada,
                                    fontSize = 12.5.sp,
                                    color = DarkTitleColor,
                                    fontWeight = FontWeight.Medium
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = BrandTealBlue
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = menuValidezExpandido,
                            onDismissRequest = { menuValidezExpandido = false }
                        ) {
                            listOf(
                                "Expira en: 24 horas",
                                "Expira en: 48 horas (Recomendado)",
                                "Expira en: 7 días"
                            ).forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion, fontSize = 12.5.sp) },
                                    onClick = {
                                        validezSeleccionada = opcion
                                        menuValidezExpandido = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // NOTA DE SEGURIDAD CIFRADA
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = InfoNoticeBg,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = InfoNoticeText,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "El profesional solo podrá ver y descargar los exámenes seleccionados durante el tiempo fijado. Acceso de solo lectura cifrado.",
                                fontSize = 11.sp,
                                color = InfoNoticeText,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }

            // --- ACCIONES DE COMPARTIR Y CÓDIGO NUMÉRICO ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CÓDIGO NUMÉRICO DE ACCESO MÉDICO:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 0.5.sp
                    )

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = ContainerLightBlue,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Numbers,
                                contentDescription = null,
                                tint = BrandTealBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "OFT - 839 204",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandTealBlue,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ContainerLightBlue,
                            contentColor = BrandTealBlue
                        )
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copiar enlace web temporal", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // --- BOTÓN FINAL DE ENVÍO POR WHATSAPP O CORREO ---
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandTealBlue)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Enviar enlace por WhatsApp o Correo", fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "Protegido bajo la normativa médica de datos clínicos personales",
                fontSize = 10.5.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}