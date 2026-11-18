package com.example.oftapp.ui.screens.tm

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Background
import androidx.compose.foundation.Clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DeleteForever
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
import com.example.oftapp.model.EstadoExamen
import com.example.oftapp.ui.theme.ClinicalClarityColors
import com.example.oftapp.ui.theme.Color
import com.example.oftapp.ui.theme.OftAppAccentBlue
import com.example.oftapp.ui.theme.OftAppBackground
import com.example.oftapp.ui.theme.OftAppBorder
import com.example.oftapp.ui.theme.OftAppBorderSubtle
import com.example.oftapp.ui.theme.OftAppFichaBlue
import com.example.oftapp.ui.theme.OftAppFichaIconBg
import com.example.oftapp.ui.theme.OftAppNavIndicator
import com.example.oftapp.ui.theme.OftAppTealContainer
import com.example.oftapp.ui.theme.OftAppTealDark
import com.example.oftapp.ui.theme.OftAppTealPrimary
import com.example.oftapp.ui.theme.OftAppTextMuted
import com.example.oftapp.ui.theme.OftAppTextPrimary
import com.example.oftapp.ui.theme.OftAppTextSecondary
import com.example.oftapp.ui.theme.OftappTheme
import com.example.oftapp.model.ExamenRegistro
import androidx.compose.foundation.interaction.MemoryInteractionSource

/** Pantalla Mis Registros - Tecnólogo Médico */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TmMisRegistrosScreen(
    onNavigateBack: () -> Unit = {},
    onVerDetalle: (ExamenRegistro) -> Unit = {},
    onEliminar: (ExamenRegistro) -> Unit = {},
    listaExamenes: List<ExamenRegistro> = emptyList()
) {
    val scrollState = rememberScrollState()
    val uiState = remember { mutableStateOf(TmMisRegistrosUiState()) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Estados de filtro
    val filtroActual by remember { mutableStateOf(FiltroRegistro.Todos) }
    val (pendientes, enRevision, validados) = when (filtroActual) {
        FiltroRegistro.Todos -> listaExamenes.partition { it.estado == EstadoExamen.PENDIENTE }
        FiltroRegistro.EN_REVISION -> listaExamenes.partition { it.estado == EstadoExamen.EN_REVISION }
        FiltroRegistro.VALIDADOS -> listaExamenes.partition { it.estado == EstadoExamen.VALIDADO }
    }

    // Column principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ClinicalClarityColors.Background)
            .verticalScroll(scrollState)
    ) {
        // --- HEADER SUPERIOR ---
        AppBarHeading(
            title = "Mis Registros",
            subtitle = "Exámenes realizados en la última jornada",
            onBack = onNavigateBack,
            color = ClinicalClarityColors.Primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN DE FILTROS ---
        TabFiltrado(
            filtroActual = filtroActual,
            onFiltroSeleccionado = { filtroActual = it },
            color = ClinicalClarityColors.Primary,
            secondaryColor = ClinicalClarityColors.Secondary,
            onSecondaryColor = ClinicalClarityColors.OnSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- LISTA DE REGISTROS ---
        if (when (filtroActual) {
            FiltroRegistro.Todos -> listaExamenes.isEmpty()
            FiltroRegistro.EN_REVISION -> pendientes.isEmpty()
            FiltroRegistro.VALIDADOS -> validados.isEmpty()
        }) {
            // Estado vacío
            Spacer(modifier = Modifier.height(48.dp))
            EmptyStateIcon(
                icon = Icons.Default.CloudOff,
                title = "No hay registros",
                description = "Aún no hay exámenes registrados. Registra un nuevo examen para comenzar.",
                color = ClinicalClarityColors.OnSurfaceVariant
            )
        } else {
            // Lista con scroll
            LazyColumn(
                modifier = Modifier.fillMaxSize(1f),
                state = scrollState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                items(
                    when (filtroActual) {
                        FiltroRegistro.Todos -> listaExamenes.size
                        FiltroRegistro.EN_REVISION -> pendientes.size
                        FiltroRegistro.VALIDADOS -> validados.size
                    }
                ) { index ->
                    // Determinar qué items mostrar según filtro
                    var examenActual: ExamenRegistro
                    when (filtroActual) {
                        FiltroRegistro.Todos -> listaExamenes[index]
                        FiltroRegistro.EN_REVISION -> pendientes[index]
                        FiltroRegistro.VALIDADOS -> validados[index]
                    }

                    RegistroTarjeta(
                        examen = examenActual,
                        onVerDetalle = onVerDetalle,
                        onEliminar = onEliminar,
                        colorFondo = when (examenActual.estado) {
                            EstadoExamen.PENDIENTE -> Color(0xFFFEF3C7)
                            EstadoExamen.EN_REVISION -> Color(0xFFE0F2FE)
                            EstadoExamen.VALIDADO -> Color(0xFFDCFCE7)
                            else -> Color(0xFFF8FAFC)
                        },
                        textColor = when (examenActual.estado) {
                            EstadoExamen.PENDIENTE -> ClinicalClarityColors.PendingText
                            EstadoExamen.EN_REVISION -> ClinicalClarityColors.InReviewText
                            EstadoExamen.VALIDADO -> ClinicalClarityColors.ValidatedText
                            else -> ClinicalClarityColors.OnSurface
                        }
                    )
                }
            }
        }
    }
}

/** AppBar con heading simplificado */
@Composable
fun AppBarHeading(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Flecha retroceso
        IconButton(onClick = onBack) {
            Icon(
                Icons.Default.ArrowBack,
                tint = color,
                contentDescription = "Retroceso"
            )
        }

        // Título principal
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Spacer(modifier = Modifier.weight(1f))

        // Subtítulo secundario
        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = ClinicalClarityColors.OnSurfaceVariant
        )
    }
}

/** Estado UI para MisRegistros */
@Serializable
data class TmMisRegistrosUiState(
    val hayNuevos: Boolean = false
)

/** Filtros de registro */
enum class FiltroRegistro {
    Todos,
    EN_REVISION,
    VALIDADOS
}

/** Tarjeta de registro individual */
@Composable
fun RegistroTarjeta(
    examen: ExamenRegistro,
    onVerDetalle: (ExamenRegistro) -> Unit,
    onEliminar: (ExamenRegistro) -> Unit,
    colorFondo: Color,
    textColor: Color
) {
    val clicked = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable {
            onVerDetalle(examen)
        },
        elevation = 1.dp,
        color = colorFondo,
        shape = RoundedCornerShape(16.dp),
        interactionSource = clicked,
        onClick = { /* handled above */ }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Fila superior: info principal + estado
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Izquierda: info del examen
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Header: RUT y tipo examen
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = examen.rutPaciente,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Badge estado
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = when (examen.estado) {
                                EstadoExamen.PENDIENTE -> Color(0xFFFEF3C7)
                                EstadoExamen.EN_REVISION -> Color(0xFFE0F2FE)
                                EstadoExamen.VALIDADO -> Color(0xFFDCFCE7)
                                else -> Color(0xFFF8FAFC)
                            }
                        ) {
                            Text(
                                text = examen.estado.toString().first().toUpperCase() + examen.estado.toString().substring(1),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (examen.estado) {
                                    EstadoExamen.PENDIENTE -> ClinicalClarityColors.PendingText
                                    EstadoExamen.EN_REVISION -> ClinicalClarityColors.InReviewText
                                    EstadoExamen.VALIDADO -> ClinicalClarityColors.ValidatedText
                                    else -> ClinicalClarityColors.OnSurfaceVariant
                                }
                            )
                        }
                    }

                    // Fecha y hora
                    Text(
                        text = "${examen.fechaExamen} • ${examen.horaExamen}",
                        fontSize = 11.sp,
                        color = ClinicalClarityColors.OnSurfaceVariant
                    )

                    // Tipo examen
                    Text(
                        text = examen.tipoExamen,
                        fontSize = 12.sp,
                        color = ClinicalClarityColors.OnSurfaceVariant,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                // Derecha: flecha de navegación
                Icon(
                    Icons.Default.ArrowForward,
                    tint = ClinicalClarityColors.OnSurfaceVariant,
                    contentDescription = "Ver detalles",
                    modifier = Modifier.size(24.dp)
                )
            }

            // Divider
            Divider(modifier = Modifier.height(1.dp).color(Color(0xFFE2E8F0)))

            // Fila inferior: observaciones y botón acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Observaciones cortas
                Text(
                    text = examen.observaciones?.take(40)?.let { "• $it..." } ?: "Sin observaciones",
                    fontSize = 10.sp,
                    color = ClinicalClarityColors.OnSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                // Botón ver más (solo si hay detalle)
                if (examen.tieneDetalle) {
                    SmallButton(
                        texto = "Ver",
                        color = ClinicalClarityColors.Primary,
                        onClick = { onVerDetalle(examen) }
                    )
                }
            }
        }
    }
}

/** Estado vacío */
@Composable
fun EmptyStateIcon(
    required @DrawableRes icon: Int,
    @StringRes title: Int,
    @StringRes description: Int,
    color: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            tint = color,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = color,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            fontSize = 14.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center
        )
    }
}

/** Pequeño botón estilo ficha */
@Composable
fun SmallButton(
    texto: String,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(32.dp).padding(horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE2E8F0),
            contentColor = color
        )
    ) {
        Text(
            text = texto,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

/** Estado de examen de ejemplo para previews */
@Preview(showBackground = true)
@Composable
fun TmMisRegistrosScreenPreview() {
    val listaEjemplo = listOf(
        ExamenRegistro(
            id = "1",
            rutPaciente = "15.420.891-K",
            tipoExamen = "Campimetría Computarizada Humphrey",
            fechaExamen = "16 Oct 2024",
            horaExamen = "09:30",
            estado = EstadoExamen.VALIDADO,
            observaciones = "Paciente colaboradora, resultados dentro de rangos normales"
        ),
        ExamenRegistro(
            id = "2",
            rutPaciente = "18.321.456-K",
            tipoExamen = "Oftalmoscopia Directa",
            fechaExamen = "15 Oct 2024",
            horaExamen = "14:15",
            estado = EstadoExamen.EN_REVISION,
            observaciones = "En proceso de revisión médica"
        ),
        ExamenRegistro(
            id = "3",
            rutPaciente = "12.789.321-K",
            tipoExamen = "Perimetría Automatizada",
            fechaExamen = "14 Oct 2024",
            horaExamen = "11:00",
            estado = EstadoExamen.PENDIENTE,
            observaciones = "Requiere validación de equipo"
        )
    )

    TmMisRegistrosScreen(
        onNavigateBack = {},
        onVerDetalle = { examen -> println("Ver detalle: ${examen.id}") },
        onEliminar = { examen -> println("Eliminar: ${examen.id}") },
        listaExamenes = listaEjemplo
    )
}