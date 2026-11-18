package com.example.oftapp.ui.screens.pacientes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// Modelos locales para que la pantalla sea autosuficiente sin depender del ViewModel médico
data class PacienteInfo(
    val nombreCompleto: String = "Mariana González R.",
    val edad: Int = 42,
    val rut: String = "15.420.891-K"
)

data class ExamenDetalleModel(
    val idExamen: String = "CP-2024-8841",
    val paciente: PacienteInfo = PacienteInfo(),
    val estado: String = "Validado",
    val tipoExamen: String = "Campimetría Computarizada Humphrey 30-2",
    val fechaHora: String = "12 Oct 2024 • 10:30 hrs",
    val medico: String = "Dra. Paulina Morales",
    val md: String = "-4.25 dB",
    val mdProb: String = "p < 1%",
    val psd: String = "+3.80 dB",
    val psdProb: String = "p < 2%",
    val vfi: String = "94%",
    val vfiStatus: String = "Preservado",
    val lateralidadCampo: String = "Ojo Derecho (OD) • 30°",
    val fijacion: String = "Fijación: 0/14",
    val fijacionDetalle: String = "Pérdidas de fijación bajo 5%",
    val especialidad: String = "Oftalmología General",
    val observaciones: String = "Paciente presenta campo visual preservado centralmente con leve depresión relativa superonasal en ojo derecho. Se sugiere control de presión intraocular y seguimiento anual.",
    val validadorNombre: String = "Dra. Paulina Morales",
    val validadorIniciales: String = "PM",
    val validadorRegistro: String = "Reg. SIS: 489120"
)

@Composable
fun PacienteExamDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    val examen = remember { ExamenDetalleModel() }
    var zoomPercentage by remember { mutableStateOf(100) }
    var isFullscreenMap by remember { mutableStateOf(false) }
    var showMenuOverflow by remember { mutableStateOf(false) }

    // Modal a pantalla completa para el mapa visual
    if (isFullscreenMap) {
        Dialog(
            onDismissRequest = { isFullscreenMap = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mapa Visual & Sensibilidad Retiniana",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF004D63)
                        )
                        IconButton(onClick = { isFullscreenMap = false }) {
                            Icon(Icons.Default.FullscreenExit, contentDescription = "Cerrar")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    VisualFieldChartsContent(
                        zoomPercentage = zoomPercentage,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF2F6FC),
        topBar = {
            ExamDetailTopAppBar(
                onBackClick = onNavigateBack,
                onProfileClick = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Sub-barra: ID Examen y Acciones
            ExamIdSubBar(
                idExamen = examen.idExamen,
                showMenu = showMenuOverflow,
                onToggleMenu = { showMenuOverflow = it },
                onShareClick = { },
                onPrintClick = { }
            )

            // Contenedor principal de la ficha
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                // Card Principal: Paciente y Datos del Examen
                PatientExamCard(examen = examen)

                Spacer(modifier = Modifier.height(16.dp))

                // Barra Interactiva: Controles de Zoom & Mapa
                InteractiveMapControlsHeader(
                    zoomPercentage = zoomPercentage,
                    onZoomIn = { if (zoomPercentage < 150) zoomPercentage += 10 },
                    onZoomOut = { if (zoomPercentage > 70) zoomPercentage -= 10 },
                    onToggleFullscreen = { isFullscreenMap = !isFullscreenMap }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Fila de Índices: MD, PSD, VFI
                IndicesCardsRow(examen = examen)

                Spacer(modifier = Modifier.height(14.dp))

                // Contenedor de Gráficos de Campo Visual
                VisualFieldPlotContainer(
                    examen = examen,
                    zoomPercentage = zoomPercentage
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Galería de Imágenes Diagnósticas
                DiagnosticImagesGallery(
                    onImageClick = { }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Botones rápidos: PDF, DICOM, Histórico
                QuickActionPills(
                    onPdfClick = { },
                    onDicomClick = { },
                    onHistoricoClick = { }
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Sección: Observaciones Médicas & Validación Especialista
                MedicalObservationsCard(examen = examen)

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ExamDetailTopAppBar(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color(0xFF0F172A)
                )
            }

            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = null,
                tint = Color(0xFF006689),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = "OFTAPP CLINICAL",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF006689),
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Examen Visual",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00384D))
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ExamIdSubBar(
    idExamen: String,
    showMenu: Boolean,
    onToggleMenu: (Boolean) -> Unit,
    onShareClick: () -> Unit,
    onPrintClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color(0xFFDCEEFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = null,
                tint = Color(0xFF006689),
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = idExamen,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF005B77)
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onShareClick, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Compartir",
                tint = Color(0xFF006689),
                modifier = Modifier.size(18.dp)
            )
        }

        IconButton(onClick = onPrintClick, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector = Icons.Default.Print,
                contentDescription = "Imprimir",
                tint = Color(0xFF006689),
                modifier = Modifier.size(18.dp)
            )
        }

        Box {
            IconButton(onClick = { onToggleMenu(!showMenu) }, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Más opciones",
                    tint = Color(0xFF006689),
                    modifier = Modifier.size(18.dp)
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { onToggleMenu(false) },
                modifier = Modifier.background(Color.White)
            ) {
                DropdownMenuItem(
                    text = { Text("Exportar DICOM") },
                    onClick = {
                        onToggleMenu(false)
                        onShareClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun PatientExamCard(examen: ExamenDetalleModel) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shadowElevation = 1.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(130.dp)
                    .background(Color(0xFF004F69))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = examen.paciente.nombreCompleto,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFE0F2FE))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF007A99))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "✓ ${examen.estado}",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF005B77)
                            )
                        }
                    }
                }

                Text(
                    text = "Edad: ${examen.paciente.edad} años • RUT: ${examen.paciente.rut}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF1F6FB),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = Color(0xFF006689),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = examen.tipoExamen,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF004D63),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${examen.fechaHora} • ${examen.medico}",
                                fontSize = 11.5.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InteractiveMapControlsHeader(
    zoomPercentage: Int,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onToggleFullscreen: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFEBF4FC)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.GridOn,
                contentDescription = null,
                tint = Color(0xFF006689),
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Mapa Visual & Sensibilid...",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004F69),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                IconButton(onClick = onZoomOut, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Menos",
                        tint = Color(0xFF0F172A),
                        modifier = Modifier.size(14.dp)
                    )
                }

                Text(
                    text = "$zoomPercentage%",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0F172A),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                IconButton(onClick = onZoomIn, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Más",
                        tint = Color(0xFF0F172A),
                        modifier = Modifier.size(14.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(onClick = onToggleFullscreen, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.CropFree,
                        contentDescription = "Fullscreen",
                        tint = Color(0xFF006689),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun IndicesCardsRow(examen: ExamenDetalleModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MD (Mean Dev)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = examen.md,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDC2626)
                )
                Text(
                    text = examen.mdProb,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFDC2626)
                )
            }
        }

        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "PSD (Patrón)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = examen.psd,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF006689)
                )
                Text(
                    text = examen.psdProb,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0288D1)
                )
            }
        }

        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "VFI (Visual Index)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = examen.vfi,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = examen.vfiStatus,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0288D1)
                )
            }
        }
    }
}

@Composable
private fun VisualFieldPlotContainer(
    examen: ExamenDetalleModel,
    zoomPercentage: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF006689))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = examen.lateralidadCampo,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004D63)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = examen.fijacion,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Text(
                        text = examen.fijacionDetalle,
                        fontSize = 9.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            VisualFieldChartsContent(
                zoomPercentage = zoomPercentage,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "> 30 dB (Normal)",
                    fontSize = 10.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(7.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFCBD5E1),
                                    Color(0xFF64748B),
                                    Color(0xFF1E293B),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "< 0 dB (Defecto)",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun VisualFieldChartsContent(
    zoomPercentage: Int,
    modifier: Modifier = Modifier
) {
    val scale = zoomPercentage / 100f

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF8FAFC),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Matriz Numérica (dB)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    NumericMatrixCanvas(scale = scale)
                }
            }
        }

        Surface(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF8FAFC),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Escala de Grises (Prob.)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    GrayscaleScotomaCanvas(scale = scale)
                }
            }
        }
    }
}

@Composable
private fun NumericMatrixCanvas(scale: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = (size.minDimension / 2.2f) * scale
        val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)

        drawCircle(
            color = Color(0xFFCBD5E1),
            radius = maxRadius * 0.35f,
            center = center,
            style = Stroke(width = 1f, pathEffect = dashedEffect)
        )
        drawCircle(
            color = Color(0xFFCBD5E1),
            radius = maxRadius * 0.70f,
            center = center,
            style = Stroke(width = 1f, pathEffect = dashedEffect)
        )
        drawCircle(
            color = Color(0xFFCBD5E1),
            radius = maxRadius,
            center = center,
            style = Stroke(width = 1f, pathEffect = dashedEffect)
        )

        drawLine(
            color = Color(0xFFCBD5E1),
            start = Offset(center.x - maxRadius, center.y),
            end = Offset(center.x + maxRadius, center.y),
            strokeWidth = 1f,
            pathEffect = dashedEffect
        )
        drawLine(
            color = Color(0xFFCBD5E1),
            start = Offset(center.x, center.y - maxRadius),
            end = Offset(center.x, center.y + maxRadius),
            strokeWidth = 1f,
            pathEffect = dashedEffect
        )

        val alertRectX = center.x - maxRadius * 0.45f
        val alertRectY = center.y - maxRadius * 0.35f
        drawRect(
            color = Color(0xFFFEE2E2),
            topLeft = Offset(alertRectX, alertRectY),
            size = Size(32f * scale, 16f * scale)
        )

        drawCircle(
            color = Color(0xFF1E293B),
            radius = 5f * scale,
            center = Offset(center.x + maxRadius * 0.45f, center.y)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "21",
            color = Color(0xFFDC2626),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center).padding(bottom = 44.dp, end = 26.dp)
        )
        Text(
            text = "19  22",
            color = Color(0xFFDC2626),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center).padding(bottom = 20.dp, end = 24.dp)
        )
        Text(
            text = "20  21",
            color = Color(0xFF334155),
            fontSize = 9.sp,
            modifier = Modifier.align(Alignment.Center).padding(bottom = 2.dp, end = 10.dp)
        )
        Text(
            text = "30  32  35",
            color = Color(0xFF334155),
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Center).padding(top = 18.dp)
        )
        Text(
            text = "27  28  26",
            color = Color(0xFF64748B),
            fontSize = 9.sp,
            modifier = Modifier.align(Alignment.Center).padding(top = 38.dp)
        )
    }
}

@Composable
private fun GrayscaleScotomaCanvas(scale: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = (size.minDimension / 2.2f) * scale

        drawCircle(
            color = Color(0xFFF1F5F9),
            radius = maxRadius,
            center = center
        )

        drawCircle(
            color = Color(0xFFE2E8F0),
            radius = maxRadius * 0.7f,
            center = center,
            style = Stroke(width = 1f)
        )
        drawCircle(
            color = Color(0xFFCBD5E1),
            radius = maxRadius * 0.35f,
            center = center,
            style = Stroke(width = 1f)
        )

        drawCircle(
            color = Color(0xFFEF4444),
            radius = 3.5f * scale,
            center = center
        )

        val scotomaCenter = Offset(center.x - maxRadius * 0.32f, center.y - maxRadius * 0.30f)
        drawCircle(
            color = Color(0xFF334155),
            radius = 12f * scale,
            center = scotomaCenter
        )
        drawCircle(
            color = Color(0xFF475569).copy(alpha = 0.6f),
            radius = 18f * scale,
            center = scotomaCenter
        )

        val blindSpotCenter = Offset(center.x + maxRadius * 0.50f, center.y + maxRadius * 0.08f)
        drawCircle(
            color = Color(0xFF0F172A),
            radius = 10f * scale,
            center = blindSpotCenter
        )
    }
}

@Composable
private fun DiagnosticImagesGallery(
    onImageClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(95.dp)
                .clickable { onImageClick("Fondo OD") },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                RetinaFundusIllustration()
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF0F172A).copy(alpha = 0.75f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Fondo OD",
                        color = Color.White,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .weight(1f)
                .height(95.dp)
                .clickable { onImageClick("OCT Capa Fibras") },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                OctScanIllustration()
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF0F172A).copy(alpha = 0.75f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "OCT Capa Fibras",
                        color = Color.White,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun RetinaFundusIllustration() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFEA580C), Color(0xFF9A3412), Color(0xFF451A03)),
                center = Offset(size.width * 0.45f, size.height * 0.5f),
                radius = size.width * 0.7f
            )
        )

        val discCenter = Offset(size.width * 0.32f, size.height * 0.50f)
        drawCircle(
            color = Color(0xFFFEF08A),
            radius = 16f,
            center = discCenter
        )
        drawCircle(
            color = Color(0xFFFEF9C3),
            radius = 9f,
            center = discCenter
        )

        val vesselPath = Path().apply {
            moveTo(discCenter.x, discCenter.y)
            cubicTo(
                discCenter.x + 20f, discCenter.y - 35f,
                discCenter.x + 50f, discCenter.y - 25f,
                size.width * 0.9f, discCenter.y - 30f
            )
            moveTo(discCenter.x, discCenter.y)
            cubicTo(
                discCenter.x + 25f, discCenter.y + 35f,
                discCenter.x + 55f, discCenter.y + 25f,
                size.width * 0.9f, discCenter.y + 30f
            )
        }
        drawPath(
            path = vesselPath,
            color = Color(0xFF7F1D1D),
            style = Stroke(width = 3.5f)
        )

        drawCircle(
            color = Color(0xFF7C2D12),
            radius = 10f,
            center = Offset(size.width * 0.65f, size.height * 0.52f)
        )
    }
}

@Composable
private fun OctScanIllustration() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(color = Color(0xFFF8FAFC))

        drawRect(
            color = Color(0xFFDCFCE7),
            topLeft = Offset(0f, size.height * 0.25f),
            size = Size(size.width, size.height * 0.35f)
        )

        val octPath = Path().apply {
            moveTo(0f, size.height * 0.50f)
            cubicTo(
                size.width * 0.15f, size.height * 0.20f,
                size.width * 0.25f, size.height * 0.25f,
                size.width * 0.35f, size.height * 0.45f
            )
            cubicTo(
                size.width * 0.50f, size.height * 0.55f,
                size.width * 0.65f, size.height * 0.22f,
                size.width * 0.85f, size.height * 0.35f
            )
            lineTo(size.width, size.height * 0.50f)
        }
        drawPath(
            path = octPath,
            color = Color(0xFF0284C7),
            style = Stroke(width = 3f)
        )

        drawRect(
            color = Color(0xFF0F172A),
            topLeft = Offset(0f, size.height * 0.70f),
            size = Size(size.width, size.height * 0.30f)
        )
        drawLine(
            color = Color(0xFF22C55E),
            start = Offset(0f, size.height * 0.82f),
            end = Offset(size.width, size.height * 0.82f),
            strokeWidth = 2.5f
        )
    }
}

@Composable
private fun QuickActionPills(
    onPdfClick: () -> Unit,
    onDicomClick: () -> Unit,
    onHistoricoClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onPdfClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF006689)
            )
        ) {
            Icon(
                imageVector = Icons.Default.PictureAsPdf,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = onDicomClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF006689)
            )
        ) {
            Icon(
                imageVector = Icons.Default.LocalHospital,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("DICOM", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = onHistoricoClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD8EEFD),
                contentColor = Color(0xFF006689)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Histórico", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun MedicalObservationsCard(examen: ExamenDetalleModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Assignment,
                    contentDescription = null,
                    tint = Color(0xFF006689),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Observaciones Médicas",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004D63)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0EDF8))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = examen.especialidad,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF005B77)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFF1F6FB),
            border = BorderStroke(1.dp, Color(0xFFE2EAF3))
        ) {
            Text(
                text = examen.observaciones,
                fontSize = 12.5.sp,
                color = Color(0xFF1E293B),
                lineHeight = 18.sp,
                modifier = Modifier.padding(14.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDCEBF8)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = examen.validadorIniciales,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004F69)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Validado por ${examen.validadorNombre}",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004F69)
                )
                Text(
                    text = examen.validadorRegistro,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Verificado",
                tint = Color(0xFF0288D1),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}