package com.example.oftapp.ui.screens.pacientes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Estilos y Paleta de Colores idénticos a la interfaz gráfica
private val ScreenBg = Color(0xFFF2F6FC)
private val DarkTitleColor = Color(0xFF0D253A)
private val BrandTealBlue = Color(0xFF006488)
private val HeaderBannerBg = Color(0xFF007A9E)
private val TimelineLineColor = Color(0xFF80DEEA)
private val CardBg = Color.White
private val BadgeYearBg = Color(0xFFE2EFFD)
private val EyeBadgeBg = Color(0xFF285C8F)

// Badges de Estado
private val BadgeValidadoBg = Color(0xFFE2F7ED)
private val BadgeValidadoText = Color(0xFF1E8E5A)
private val BadgeEntregadoBg = Color(0xFFE2F0FD)
private val BadgeEntregadoText = Color(0xFF0288D1)
private val BadgeObservadoBg = Color(0xFFFFEBEE)
private val BadgeObservadoText = Color(0xFFD32F2F)

data class TimelineItemModel(
    val id: Long,
    val fecha: String,
    val estado: String,
    val estadoTipo: String, // "validado", "entregado", "observado"
    val titulo: String,
    val ojo: String,
    val lugarMedico: String,
    val notaAdicional: String? = null,
    val notaEsAlerta: Boolean = false,
    val resultadoResumen: String,
    val nodoIcono: ImageVector,
    val nodoBgColor: Color,
    val nodoIconoColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientHistoryScreen(
    pacienteId: Long = 1L,
    onBack: () -> Unit = {},
    onExamenClick: (Long) -> Unit = {},
    onVerDocumento: (Long) -> Unit = {},
    onDescargarHistorialCompleto: () -> Unit = {},
    onNavegarItemBottomBar: (String) -> Unit = {}
) {
    val historiaEventos = listOf(
        TimelineItemModel(
            id = 1,
            fecha = "12 Octubre, 2024",
            estado = "Validado",
            estadoTipo = "validado",
            titulo = "Campimetría\nComputarizada",
            ojo = "AO",
            lugarMedico = "Clínica Andes • Dra. Paulina Morales",
            resultadoResumen = "Campo visual 98% de sensibilidad",
            nodoIcono = Icons.Default.Check,
            nodoBgColor = Color(0xFF80DEEA),
            nodoIconoColor = Color(0xFF004D40)
        ),
        TimelineItemModel(
            id = 2,
            fecha = "28 Agosto, 2024",
            estado = "Entregado",
            estadoTipo = "entregado",
            titulo = "Topografía Corneal\nPentacam",
            ojo = "OD",
            lugarMedico = "Hospital del Salvador - Box 2",
            resultadoResumen = "Mapa paquimétrico normal",
            nodoIcono = Icons.Default.Description,
            nodoBgColor = Color(0xFFE1F5FE),
            nodoIconoColor = Color(0xFF0288D1)
        ),
        TimelineItemModel(
            id = 3,
            fecha = "15 Junio, 2024",
            estado = "Observado",
            estadoTipo = "observado",
            titulo = "Curva de Tensión Aplanática",
            ojo = "OI",
            lugarMedico = "Clínica Andes • Dr. Camilo Arancibia",
            notaAdicional = "Nota: Requiere control en 3 meses",
            notaEsAlerta = true,
            resultadoResumen = "",
            nodoIcono = Icons.Default.Warning,
            nodoBgColor = Color(0xFFFFEBEE),
            nodoIconoColor = Color(0xFFD32F2F)
        ),
        TimelineItemModel(
            id = 4,
            fecha = "04 Enero, 2024",
            estado = "Entregado",
            estadoTipo = "entregado",
            titulo = "Agudeza Visual y Refracción",
            ojo = "AO",
            lugarMedico = "Centro Médico Oftalmológico",
            resultadoResumen = "Agudeza 20/20 corregida",
            nodoIcono = Icons.Default.MedicalServices,
            nodoBgColor = Color(0xFFE1F5FE),
            nodoIconoColor = Color(0xFF0288D1)
        )
    )

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
                            text = "Historial",
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
                    selected = true,
                    onClick = { onNavegarItemBottomBar("historial") },
                    icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
                    label = { Text("Historial", fontSize = 10.5.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandTealBlue,
                        selectedTextColor = BrandTealBlue,
                        indicatorColor = Color(0xFFEDF5FF)
                    )
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
            // --- BANNER AZUL OSCURO DE ENCABEZADO ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = HeaderBannerBg)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Historial de exámenes",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Línea de tiempo cronológica de todas tus atenciones oftalmológicas.",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mostrando 4 exámenes registrados desde Enero 2024",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // --- SECTOR DE AÑO Y CANTIDAD DE REGISTROS ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(BrandTealBlue, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Año 2024",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTitleColor
                    )
                }

                Surface(
                    color = BadgeYearBg,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "4 registros",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandTealBlue,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // --- LÍNEA DE TIEMPO (TIMELINE) ---
            Column(modifier = Modifier.fillMaxWidth()) {
                historiaEventos.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Columna del Eje Cronológico (Ícono de Nodo + Línea Vertical)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(40.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(item.nodoBgColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.nodoIcono,
                                    contentDescription = null,
                                    tint = item.nodoIconoColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Línea conectora entre ítems (salvo el último)
                            if (index < historiaEventos.lastIndex) {
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height(175.dp)
                                        .background(TimelineLineColor)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Tarjeta de Contenido del Examen
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onExamenClick(item.id) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardBg),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                // Fecha y Badge de Estado
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarToday,
                                            contentDescription = null,
                                            tint = BrandTealBlue,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = item.fecha,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BrandTealBlue
                                        )
                                    }

                                    val (badgeBg, badgeText, badgeIcon) = when (item.estadoTipo) {
                                        "validado" -> Triple(BadgeValidadoBg, BadgeValidadoText, Icons.Default.CheckCircle)
                                        "observado" -> Triple(BadgeObservadoBg, BadgeObservadoText, Icons.Default.ErrorOutline)
                                        else -> Triple(BadgeEntregadoBg, BadgeEntregadoText, Icons.Default.CheckCircle)
                                    }

                                    Surface(
                                        color = badgeBg,
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = badgeIcon,
                                                contentDescription = null,
                                                tint = badgeText,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = item.estado,
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = badgeText
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Título y Badge Ojo (AO, OD, OI)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = item.titulo,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkTitleColor,
                                        modifier = Modifier.weight(1f),
                                        lineHeight = 20.sp
                                    )

                                    Surface(
                                        color = EyeBadgeBg,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = item.ojo,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Domain,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = item.lugarMedico,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }

                                // Si tiene nota especial de alerta (ejemplo: Observado)
                                if (item.notaAdicional != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        color = BadgeObservadoBg,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.NewReleases,
                                                contentDescription = null,
                                                tint = BadgeObservadoText,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = item.notaAdicional,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = BadgeObservadoText
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Resumen de Resultado y enlace "Ver examen"
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (item.resultadoResumen.isNotEmpty()) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ShowChart,
                                                contentDescription = null,
                                                tint = BrandTealBlue,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = item.resultadoResumen,
                                                fontSize = 11.5.sp,
                                                color = Color.DarkGray
                                            )
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable { onVerDocumento(item.id) }
                                    ) {
                                        Text(
                                            text = "Ver\nexamen",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BrandTealBlue,
                                            textAlign = TextAlign.End,
                                            lineHeight = 14.sp
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = BrandTealBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // --- BOTÓN PRINCIPAL DE DESCARGA PDF Y FIRMA DIGITAL ---
            Button(
                onClick = onDescargarHistorialCompleto,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandTealBlue)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Descargar historial completo consolidado\n(PDF)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Documento con firma electrónica digital válida para interconsultas",
                    fontSize = 10.5.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}