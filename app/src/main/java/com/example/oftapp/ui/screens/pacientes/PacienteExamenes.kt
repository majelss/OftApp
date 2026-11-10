package com.example.oftapp.ui.screens.pacientes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores del diseño de la pantalla Mis Exámenes
private val ScreenBg = Color(0xFFF2F6FC)
private val DarkTitleColor = Color(0xFF0D253A)
private val BrandTealBlue = Color(0xFF006689)
private val CardBg = Color.White
private val ContainerLightBlue = Color(0xFFEDF5FF)
private val BadgeSyncBg = Color(0xFFE2F0FD)
private val BadgeSyncText = Color(0xFF006689)
private val BtnSecondaryBg = Color(0xFFE8F1FC)

// Colores de badges de estado
private val BadgeValidadoBg = Color(0xFFE2F7ED)
private val BadgeValidadoText = Color(0xFF1E8E5A)
private val BadgeEntregadoBg = Color(0xFFE2F0FD)
private val BadgeEntregadoText = Color(0xFF0288D1)
private val BadgeObservadoBg = Color(0xFFFFEBEE)
private val BadgeObservadoText = Color(0xFFD32F2F)

data class ExamenCardModel(
    val id: Long,
    val titulo: String,
    val subtitulo: String,
    val estado: String,
    val estadoTipo: String, // "validado", "entregado", "observado"
    val ojo: String,
    val fecha: String,
    val medico: String,
    val icono: ImageVector,
    val iconoColor: Color,
    val iconoBg: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteExamenes(
    pacienteId: Long = 1L,
    onBack: () -> Unit = {},
    onExamenClick: (Long) -> Unit = {},
    onVerDocumento: (Long) -> Unit = {},
    onNavegarItemBottomBar: (String) -> Unit = {}
) {
    var busquedaText by remember { mutableStateOf("") }
    var filtroSeleccionado by remember { mutableStateOf("Todos (4)") }

    val listaFiltros = listOf("Todos (4)", "Campimetría", "Topografía corr")

    val examenes = listOf(
        ExamenCardModel(
            id = 1,
            titulo = "Campimetría Computa...",
            subtitulo = "Programa 24-2 SITA Standard",
            estado = "Validado",
            estadoTipo = "validado",
            ojo = "AO (Ambos Ojos)",
            fecha = "12 Oct 2024",
            medico = "Dr. R. Santander",
            icono = Icons.Default.Visibility,
            iconoColor = BrandTealBlue,
            iconoBg = Color(0xFFDCEBFA)
        ),
        ExamenCardModel(
            id = 2,
            titulo = "Topografía Corneal P...",
            subtitulo = "Mapa Elevación y Paquimetría",
            estado = "Entregado",
            estadoTipo = "entregado",
            ojo = "OD (Ojo Derecho)",
            fecha = "28 Ago 2024",
            medico = "Dra. M. Morales",
            icono = Icons.Default.Adjust,
            iconoColor = Color(0xFF0288D1),
            iconoBg = Color(0xFFE0F7FA)
        ),
        ExamenCardModel(
            id = 3,
            titulo = "Curva de Tensión Apl...",
            subtitulo = "Tonometría horaria Goldmann",
            estado = "Observado",
            estadoTipo = "observado",
            ojo = "OI (Ojo Izquierdo)",
            fecha = "15 Jun 2024",
            medico = "Dr. C. Herrera",
            icono = Icons.Default.Speed,
            iconoColor = Color(0xFFD32F2F),
            iconoBg = Color(0xFFFFEBEE)
        ),
        ExamenCardModel(
            id = 4,
            titulo = "Agudeza Visual y Ref...",
            subtitulo = "Optometría subjetiva y objetiva",
            estado = "Entregado",
            estadoTipo = "entregado",
            ojo = "AO (Ambos Ojos)",
            fecha = "04 Ene 2024",
            medico = "Dra. L. Campos",
            icono = Icons.Default.Pageview,
            iconoColor = Color(0xFF0097A7),
            iconoBg = Color(0xFFE0F7FA)
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
                            text = "Mis Exámenes",
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
                    selected = true,
                    onClick = { onNavegarItemBottomBar("examenes") },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "Mis exámenes") },
                    label = { Text("Mis exáme...", fontSize = 10.5.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandTealBlue,
                        selectedTextColor = BrandTealBlue,
                        indicatorColor = ContainerLightBlue
                    )
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
            // --- HEADER DE LA PANTALLA ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "HISTORIAL CLÍNICO OCULAR",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandTealBlue,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Mis exámenes",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTitleColor
                    )
                }

                Surface(
                    color = BadgeSyncBg,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = BadgeSyncText,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sincronizado",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BadgeSyncText
                        )
                    }
                }
            }

            Text(
                text = "Revisa y descarga tus evaluaciones visuales con resultados diagnósticos detallados.",
                fontSize = 12.5.sp,
                color = Color.Gray,
                lineHeight = 16.sp
            )

            // --- BARRA DE BÚSQUEDA ---
            OutlinedTextField(
                value = busquedaText,
                onValueChange = { busquedaText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Buscar por nombre de examen o fecha...", fontSize = 13.sp, color = Color.Gray)
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = BrandTealBlue)
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = CardBg,
                    focusedContainerColor = CardBg,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = BrandTealBlue
                ),
                singleLine = true
            )

            // --- CHIPS DE FILTRO ---
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listaFiltros) { filtro ->
                    val esSeleccionado = filtro == filtroSeleccionado
                    FilterChip(
                        selected = esSeleccionado,
                        onClick = { filtroSeleccionado = filtro },
                        label = {
                            Text(
                                text = filtro,
                                fontSize = 13.sp,
                                fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandTealBlue,
                            selectedLabelColor = Color.White,
                            containerColor = CardBg,
                            labelColor = DarkTitleColor
                        ),
                        border = null
                    )
                }
            }

            // --- BANNER DE RESUMEN (MOSTRANDO 4 EVALUACIONES) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ContainerLightBlue)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = BrandTealBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Mostrando ",
                            fontSize = 12.sp,
                            color = DarkTitleColor
                        )
                        Text(
                            text = "4 evaluaciones ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTitleColor
                        )
                        Text(
                            text = "completadas",
                            fontSize = 12.sp,
                            color = DarkTitleColor
                        )
                    }

                    Text(
                        text = "2024",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandTealBlue
                    )
                }
            }

            // --- LISTADO DE EXÁMENES ---
            examenes.forEach { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Header del Examen (Icono, Título y Badge de Estado)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(item.iconoBg, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icono,
                                    contentDescription = null,
                                    tint = item.iconoColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.titulo,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkTitleColor
                                )
                                Text(
                                    text = item.subtitulo,
                                    fontSize = 11.5.sp,
                                    color = Color.Gray
                                )
                            }

                            // Badge de Estado
                            val (badgeBg, badgeText, badgeIcon) = when (item.estadoTipo) {
                                "validado" -> Triple(BadgeValidadoBg, BadgeValidadoText, Icons.Default.CheckCircle)
                                "observado" -> Triple(BadgeObservadoBg, BadgeObservadoText, Icons.Default.Warning)
                                else -> Triple(BadgeEntregadoBg, BadgeEntregadoText, Icons.Default.CheckCircle)
                            }

                            Surface(
                                color = badgeBg,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
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

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bloque gris/celeste con Ojo, Fecha y Médico
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = ContainerLightBlue)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = BrandTealBlue,
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
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
                                                text = item.ojo,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarToday,
                                            contentDescription = null,
                                            tint = Color.Gray,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = item.fecha,
                                            fontSize = 11.5.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MedicalServices,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = item.medico,
                                        fontSize = 11.5.sp,
                                        color = Color.DarkGray,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Botones Ver detalles y Descargar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onExamenClick(item.id) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BtnSecondaryBg,
                                    contentColor = BrandTealBlue
                                )
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ver detalles", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { onVerDocumento(item.id) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrandTealBlue)
                            ) {
                                Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Descargar", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // --- BANNER INFERIOR DE ASISTENCIA / COPIA CERTIFICADA ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ContainerLightBlue)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = BrandTealBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "¿Necesitas una copia certificada?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTitleColor
                        )
                        Text(
                            text = "Puedes solicitar firmas digitales o retirarlo en recepción central.",
                            fontSize = 11.5.sp,
                            color = Color.Gray,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}