package com.example.oftapp.ui.screems.examenes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oftapp.model.ExamenVisualDetalle
import com.example.oftapp.ui.theme.OftAppAccentBlue
import com.example.oftapp.ui.theme.OftAppBackground
import com.example.oftapp.ui.theme.OftAppBorder
import com.example.oftapp.ui.theme.OftAppBorderSubtle
import com.example.oftapp.ui.theme.OftAppFichaBlue
import com.example.oftapp.ui.theme.OftAppTealContainer
import com.example.oftapp.ui.theme.OftAppTealDark
import com.example.oftapp.ui.theme.OftAppTealPrimary
import com.example.oftapp.ui.theme.OftAppTextMuted
import com.example.oftapp.ui.theme.OftAppTextPrimary
import com.example.oftapp.ui.theme.OftAppTextSecondary
import com.example.oftapp.ui.theme.OftappTheme

/**
 * Pantalla de Detalle y Trazabilidad de Examen Visual (DSY1105 - OftApp).
 * Visualiza resultados de Campimetría Computarizada Humphrey 30-2,
 * mapa numérico, escala de grises, imágenes de fondo de ojo / OCT,
 * índices de confiabilidad (MD, PSD, VFI) y validación médica de especialista.
 */
@Composable
fun ExamDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ExamDetailViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showMenuOverflow by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onDismissSnackbar()
        }
    }

    // Diálogo para Actualizar Estado (RF08, RF09)
    if (uiState.showActualizarEstadoDialog) {
        ActualizarEstadoDialog(
            estadoActual = uiState.examen.estado,
            onDismiss = { viewModel.onCloseActualizarEstadoDialog() },
            onConfirm = { nuevoEstado -> viewModel.onSeleccionarNuevoEstado(nuevoEstado) }
        )
    }

    // Diálogo de confirmación para Reabrir / Corregir
    if (uiState.showReabrirDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onCloseReabrirDialog() },
            title = {
                Text(
                    text = "¿Reabrir examen para corrección?",
                    fontWeight = FontWeight.Bold,
                    color = OftAppTextPrimary
                )
            },
            text = {
                Text(
                    text = "El estado del examen pasará a 'Observado' para permitir el ingreso de nuevas indicaciones técnicas o reclasificación.",
                    fontSize = 14.sp,
                    color = OftAppTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.onConfirmarReapertura() },
                    colors = ButtonDefaults.buttonColors(containerColor = OftAppTealPrimary)
                ) {
                    Text("Reabrir")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onCloseReabrirDialog() }) {
                    Text("Cancelar", color = OftAppTextMuted)
                }
            }
        )
    }

    // Modal a pantalla completa para el mapa visual
    if (uiState.isFullscreenMap) {
        Dialog(
            onDismissRequest = { viewModel.onToggleFullscreen() },
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
                            color = OftAppTealDark
                        )
                        IconButton(onClick = { viewModel.onToggleFullscreen() }) {
                            Icon(Icons.Default.FullscreenExit, contentDescription = "Cerrar")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    VisualFieldChartsContent(
                        zoomPercentage = uiState.zoomPercentage,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OftAppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            // Sub-barra: EXAMEN #CP-2024-8841 y Acciones
            ExamIdSubBar(
                idExamen = uiState.examen.idExamen,
                showMenu = showMenuOverflow,
                onToggleMenu = { showMenuOverflow = it },
                onShareClick = {
                    viewModel.onExportPdf()
                },
                onPrintClick = {
                    viewModel.onExportPdf()
                }
            )

            // Contenedor principal de la ficha
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                // Card Principal: Paciente y Datos del Examen
                PatientExamCard(examen = uiState.examen)

                Spacer(modifier = Modifier.height(16.dp))

                // Barra Interactiva: Controles de Zoom & Mapa
                InteractiveMapControlsHeader(
                    zoomPercentage = uiState.zoomPercentage,
                    onZoomIn = { viewModel.onZoomIn() },
                    onZoomOut = { viewModel.onZoomOut() },
                    onToggleFullscreen = { viewModel.onToggleFullscreen() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Fila de Índices: MD, PSD, VFI
                IndicesCardsRow(examen = uiState.examen)

                Spacer(modifier = Modifier.height(14.dp))

                // Contenedor de Gráficos de Campo Visual (OD 30°)
                VisualFieldPlotContainer(
                    examen = uiState.examen,
                    zoomPercentage = uiState.zoomPercentage
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Galería de Imágenes Diagnósticas (Fondo OD y OCT)
                DiagnosticImagesGallery(
                    onImageClick = { desc ->
                        viewModel.onExportDicom()
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Botones rápidos: PDF, DICOM, Histórico
                QuickActionPills(
                    onPdfClick = { viewModel.onExportPdf() },
                    onDicomClick = { viewModel.onExportDicom() },
                    onHistoricoClick = { viewModel.onOpenHistorico() }
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Sección: Observaciones Médicas & Validación Especialista
                MedicalObservationsCard(examen = uiState.examen)

                Spacer(modifier = Modifier.height(20.dp))

                // Botones Inferiores de Acción Principal
                BottomActionButtons(
                    onReabrirClick = { viewModel.onOpenReabrirDialog() },
                    onActualizarEstadoClick = { viewModel.onOpenActualizarEstadoDialog() }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/**
 * Barra superior de marca OftApp Clinical - Examen Visual.
 */
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
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = OftAppTextPrimary
                )
            }

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
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = OftAppTealPrimary,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Examen Visual",
                    fontSize = 15.sp,
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

/**
 * Sub-barra con ID del examen e iconos de compartir, imprimir y opciones.
 */
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
                tint = OftAppAccentBlue,
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
                tint = OftAppTealPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        IconButton(onClick = onPrintClick, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector = Icons.Default.Print,
                contentDescription = "Imprimir",
                tint = OftAppTealPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        Box {
            IconButton(onClick = { onToggleMenu(!showMenu) }, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Más opciones",
                    tint = OftAppTealPrimary,
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
                DropdownMenuItem(
                    text = { Text("Ver auditoría de cambios") },
                    onClick = { onToggleMenu(false) }
                )
            }
        }
    }
}

/**
 * Tarjeta de información del paciente y examen con borde lateral azul oscuro.
 */
@Composable
private fun PatientExamCard(examen: ExamenVisualDetalle) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shadowElevation = 1.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Franja vertical azul de acento izquierdo
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

                    // Píldora de estado (✓ Validado)
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
                    color = OftAppTextMuted
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Contenedor interno gris/celeste con tipo de examen y fecha
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
                                tint = OftAppTealPrimary,
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
                                tint = OftAppTextMuted,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${examen.fechaHora} • ${examen.medico}",
                                fontSize = 11.5.sp,
                                color = OftAppTextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Barra interactiva con zoom y fullscreen para el mapa visual.
 */
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
                tint = OftAppTealPrimary,
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

            // Controles de Zoom
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
                        tint = OftAppTextPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Text(
                    text = "$zoomPercentage%",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OftAppTextPrimary,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                IconButton(onClick = onZoomIn, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Más",
                        tint = OftAppTextPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(onClick = onToggleFullscreen, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.CropFree,
                        contentDescription = "Fullscreen",
                        tint = OftAppTealPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * Fila con 3 tarjetas de índices clínicos: MD, PSD, VFI.
 */
@Composable
private fun IndicesCardsRow(examen: ExamenVisualDetalle) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // MD (Mean Dev)
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
                    color = OftAppTextMuted
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

        // PSD (Patrón)
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
                    color = OftAppTextMuted
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = examen.psd,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = OftAppTealPrimary
                )
                Text(
                    text = examen.psdProb,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = OftAppAccentBlue
                )
            }
        }

        // VFI (Visual Index)
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
                    color = OftAppTextMuted
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
                    color = OftAppAccentBlue
                )
            }
        }
    }
}

/**
 * Contenedor de gráficos de campimetría (Matriz Numérica y Escala de Grises).
 */
@Composable
private fun VisualFieldPlotContainer(
    examen: ExamenVisualDetalle,
    zoomPercentage: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Encabezado del gráfico
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
                            .background(OftAppTealPrimary)
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
                        color = OftAppTextMuted
                    )
                    Text(
                        text = examen.fijacionDetalle,
                        fontSize = 9.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Gráficos lado a lado
            VisualFieldChartsContent(
                zoomPercentage = zoomPercentage,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Barra de calibración y degradado de sensibilidad
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "> 30 dB (Normal)",
                    fontSize = 10.sp,
                    color = OftAppTextMuted
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
                    color = OftAppTextMuted
                )
            }
        }
    }
}

/**
 * Contenido interactivo de los gráficos circulares Humphrey.
 */
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
        // Gráfico 1: Matriz Numérica (dB)
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
                    color = OftAppTextSecondary
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

        // Gráfico 2: Escala de Grises (Prob.)
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
                    color = OftAppTextSecondary
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

/**
 * Canvas con la cuadrícula de sensibilidad y números Humphrey.
 */
@Composable
private fun NumericMatrixCanvas(scale: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = (size.minDimension / 2.2f) * scale

        val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)

        // Círculos concéntricos de 10°, 20°, 30°
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

        // Ejes horizontal y vertical
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

        // Caja de alerta en cuadrante superonasal (escotoma 19 - 22)
        val alertRectX = center.x - maxRadius * 0.45f
        val alertRectY = center.y - maxRadius * 0.35f
        drawRect(
            color = Color(0xFFFEE2E2),
            topLeft = Offset(alertRectX, alertRectY),
            size = androidx.compose.ui.geometry.Size(32f * scale, 16f * scale)
        )

        // Mancha ciega temporal (OD: lado derecho)
        drawCircle(
            color = Color(0xFF1E293B),
            radius = 5f * scale,
            center = Offset(center.x + maxRadius * 0.45f, center.y)
        )
    }

    // Capa de texto para los valores de sensibilidad (en dp relativos)
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

/**
 * Canvas con la representación del campo visual en escala de grises y escotoma paracentral.
 */
@Composable
private fun GrayscaleScotomaCanvas(scale: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = (size.minDimension / 2.2f) * scale

        // Fondo del campo visual (grisáceo claro normal)
        drawCircle(
            color = Color(0xFFF1F5F9),
            radius = maxRadius,
            center = center
        )

        // Anillos guía
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

        // Punto de fijación central foveal (Rojo)
        drawCircle(
            color = Color(0xFFEF4444),
            radius = 3.5f * scale,
            center = center
        )

        // Escotoma paracentral superonasal leve (Mancha oscura)
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

        // Mancha ciega fisiológica temporal
        val blindSpotCenter = Offset(center.x + maxRadius * 0.50f, center.y + maxRadius * 0.08f)
        drawCircle(
            color = Color(0xFF0F172A),
            radius = 10f * scale,
            center = blindSpotCenter
        )
    }
}

/**
 * Galería con miniaturas de diagnóstico oftalmológico (Fondo OD y OCT Capa Fibras).
 */
@Composable
private fun DiagnosticImagesGallery(
    onImageClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Miniatura: Fondo de Ojo OD
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(95.dp)
                .clickable { onImageClick("Fondo OD") },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Ilustración de retinografía / fondo de ojo
                RetinaFundusIllustration()

                // Badge inferior izquierdo
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

        // Miniatura: OCT Capa de Fibras Nerviosas
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(95.dp)
                .clickable { onImageClick("OCT Capa Fibras") },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Ilustración de gráfico OCT de espesor de capa de fibras
                OctScanIllustration()

                // Badge inferior izquierdo
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

/**
 * Representación visual simulada del fondo de ojo con disco óptico y arcadas vasculares.
 */
@Composable
private fun RetinaFundusIllustration() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Fondo retiniano anaranjado cálido
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFEA580C), Color(0xFF9A3412), Color(0xFF451A03)),
                center = Offset(size.width * 0.45f, size.height * 0.5f),
                radius = size.width * 0.7f
            )
        )

        // Papila óptica (disco óptico nacarado)
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

        // Arcadas vasculares retinianas simuladas
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

        // Mácula / Fóvea
        drawCircle(
            color = Color(0xFF7C2D12),
            radius = 10f,
            center = Offset(size.width * 0.65f, size.height * 0.52f)
        )
    }
}

/**
 * Representación visual simulada de curva de espesor OCT (TSNIT).
 */
@Composable
private fun OctScanIllustration() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla de equipo de diagnóstico
        drawRect(color = Color(0xFFF8FAFC))

        // Franjas de normalidad verde / amarillo
        drawRect(
            color = Color(0xFFDCFCE7),
            topLeft = Offset(0f, size.height * 0.25f),
            size = androidx.compose.ui.geometry.Size(size.width, size.height * 0.35f)
        )

        // Curva TSNIT de doble joroba (espesor de fibras)
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

        // Simulación de escaneo B-scan en la parte inferior
        drawRect(
            color = Color(0xFF0F172A),
            topLeft = Offset(0f, size.height * 0.70f),
            size = androidx.compose.ui.geometry.Size(size.width, size.height * 0.30f)
        )
        drawLine(
            color = Color(0xFF22C55E),
            start = Offset(0f, size.height * 0.82f),
            end = Offset(size.width, size.height * 0.82f),
            strokeWidth = 2.5f
        )
    }
}

/**
 * Botones de acción rápida: PDF, DICOM, Histórico.
 */
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
        // Botón PDF
        OutlinedButton(
            onClick = onPdfClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = OftAppTealPrimary
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

        // Botón DICOM
        OutlinedButton(
            onClick = onDicomClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = OftAppTealPrimary
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

        // Botón Histórico
        Button(
            onClick = onHistoricoClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD8EEFD),
                contentColor = OftAppTealPrimary
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

/**
 * Sección de Observaciones Médicas y firma de validación.
 */
@Composable
private fun MedicalObservationsCard(examen: ExamenVisualDetalle) {
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
                    tint = OftAppTealPrimary,
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

        // Contenedor de la nota médica
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

        // Ficha del especialista validador
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
                    color = OftAppTextMuted
                )
            }

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Verificado",
                tint = OftAppAccentBlue,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Botones inferiores de acción: Reabrir / Corregir y Actualizar Estado.
 */
@Composable
private fun BottomActionButtons(
    onReabrirClick: () -> Unit,
    onActualizarEstadoClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Botón Reabrir / Corregir
        Button(
            onClick = onReabrirClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE0EEFA),
                contentColor = Color(0xFF004F69)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Reabrir / Corregir",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Botón Actualizar Estado
        Button(
            onClick = onActualizarEstadoClick,
            modifier = Modifier
                .weight(1.1f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OftAppTealPrimary,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Actualizar Estado",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Diálogo modal para actualizar el estado del examen (RF08, RF09).
 */
@Composable
private fun ActualizarEstadoDialog(
    estadoActual: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val estados = listOf("Validado", "Observado", "Pendiente", "Entregado")
    var estadoSeleccionado by remember { mutableStateOf(estadoActual) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Actualizar Estado del Examen",
                fontWeight = FontWeight.Bold,
                color = OftAppTealDark
            )
        },
        text = {
            Column {
                Text(
                    text = "Seleccione el nuevo estado del flujo clínico para registrar trazabilidad y firma:",
                    fontSize = 13.sp,
                    color = OftAppTextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                estados.forEach { estado ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { estadoSeleccionado = estado }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = estado == estadoSeleccionado,
                            onClick = { estadoSeleccionado = estado },
                            colors = RadioButtonDefaults.colors(selectedColor = OftAppTealPrimary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = estado,
                            fontSize = 14.sp,
                            fontWeight = if (estado == estadoSeleccionado) FontWeight.Bold else FontWeight.Normal,
                            color = OftAppTextPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(estadoSeleccionado) },
                colors = ButtonDefaults.buttonColors(containerColor = OftAppTealPrimary)
            ) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = OftAppTextMuted)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ExamDetailScreenPreview() {
    OftappTheme {
        ExamDetailScreen()
    }
}
