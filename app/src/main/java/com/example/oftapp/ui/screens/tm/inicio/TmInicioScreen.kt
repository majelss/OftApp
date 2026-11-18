package com.example.oftapp.ui.screens.tm.inicio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
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

/** Pantalla Inicio - Tecnólogo Médico (Dashboard Mejorado) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TmInicioScreen(
    onNavigateBack: () -> Unit = {},
    onNuevoExamen: () -> Unit = {},
    onVerMisRegistros: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Column principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ClinicalClarityColors.Background)
            .verticalScroll(scrollState)
    ) {
        // --- HEADER SUPERIOR ---
        AppBarInicial(
            title = "Bienvenido",
            subtitle = "Tecnólogo Médico",
            onBack = onNavigateBack,
            color = ClinicalClarityColors.Primary,
            onNuevoExamen = onNuevoExamen,
            onVerMisRegistros = onVerMisRegistros
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN 1: RESUMEN DE JORNADA ---
        cardSection(
            title = "Resumen de Jornada",
            icon = Icons.Default.Dashboard,
            color = ClinicalClarityColors.Primary
       ) {
            JornadaResumenGrid()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN 2: PACIENTES EN COLA ---
        cardSection(
            title = "Pacientes en Espera",
            icon = Icons.Default.Person,
            color = ClinicalClarityColors.Secondary
        ) {
            PacientesColaList()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN 3: MÉTRICAS RÁPIDAS ---
        cardSection(
            title = "Métricas Clínicas",
            icon = Icons.Default.Chart,
            color = ClinicalClarityColors.SecondaryCyan
        ) {
            MetricasRapidasGrid()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN 4: ACCIONES RÁPIDAS ---
        cardSection(
            title = "Acciones Rápidas",
            icon = Icons.Default.FolderSpecial,
            color = ClinicalClarityColors.Secondary
        ) {
            AccionesRapidasGrid()
        }
    }
}

/** AppBar inicial con acciones */
@Composable
fun AppBarInicial(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    color: Color,
    onNuevoExamen: () -> Unit,
    onVerMisRegistros: () -> Unit
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

        // Acciones en la derecha
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            // Botón Mis Registros
            OutlinedButton(
                onClick = onVerMisRegistros,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, ClinicalClarityColors.Secondary),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFE0F2FE),
                    contentColor = ClinicalClarityColors.Secondary
                )
            ) {
                Text(
                    text = "Mis Registros",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = ClinicalClarityColors.Secondary
                )
            }

            // Separador
            Box(
                modifier = Modifier.width(8.dp).height(24.dp),
                backgroundColor = Color(0xFFE2E8F0)
            )

            // Botón Nuevo Examen (primario)
            Button(
                onClick = onNuevoExamen,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ClinicalClarityColors.Primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Nuevo Examen",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

/** Grid de resumen de jornada - 4 tarjetas */
@Composable
fun JornadaResumenGrid() {
    val metricas = listOf(
        MetricaItem(
            titulo = "Exámenes Hoy",
            valor = "12",
            subtitulo = "Total realizados",
            icono = Icons.Default.History,
            colorFondo = Color(0xFFE0F2FE),
            colorTexto = ClinicalClarityColors.Primary
        ),
        MetricaItem(
            titulo = "En Revisión",
            valor = "3",
            subtitulo = "Awaiting médico",
            icono = Icons.Default.Clipboard,
            colorFondo = Color(0xFFE0F2FE),
            colorTexto = ClinicalClarityColors.Secondary
        ),
        MetricaItem(
            titulo = "Pendientes",
            valor = "1",
            subtitulo = "Por validar",
            icono = Icons.Default.HourglassEmpty,
            colorFondo = Color(0xFFFEF3C7),
            colorTexto = ClinicalClarityColors.PendingText
        ),
        MetricaItem(
            titulo = "Validados",
            valor = "8",
            subtitulo = "Listos para informe",
            icono = Icons.Default.CheckCircle,
            colorFondo = Color(0xFFDCFCE7),
            colorTexto = ClinicalClarityColors.ValidatedText
        )
    )

    // Two columns grid
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        metricas.forEach { metric ->
            MetricaItemCard(metric = metric)
        }
    }
}

/** Item de métrica individual */
@Composable
fun MetricaItemCard(
    @StringRes titulo: Int,
    valor: String,
    subtitulo: String,
    icono: androidx.compose.material.icons.Icon,
    colorFondo: Color,
    colorTexto: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .height(100.dp)
            .background(colorFondo, RoundedCornerShape(16.dp))
            .clickable {}
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            // Ícono a la izquierda
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icono,
                    tint = colorTexto,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Título y valor
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "titulo placeholder", // placeholder por simplicidad
                        fontSize = 12.sp,
                        color = colorTexto.copy(alpha = 0.8f)
                    )
                    Text(
                        text = valor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorTexto
                    )
                }
            }

            // Subtítulo
            Text(
                text = subtitulo,
                fontSize = 11.sp,
                color = colorTexto.copy(alpha = 0.6f),
                textAlign = TextAlign.Start
            )
        }
    }
}

/** Lista de pacientes en cola */
@Composable
fun PacientesColaList() {
    // Datos de ejemplo
    val pacientes = listOf(
        PacienteCola("15.420.891-K", "Campimetría", "09:30", "EN REVISIÓN"),
        PacienteCola("18.321.456-K", "Oftalmoscopia", "10:15", "PENDIENTE"),
        PacienteCola("12.789.321-K", "Perimetría", "11:00", "VALIDADO")
    )

    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pacientes.size) { index ->
            PacienteColaItem(paciente = pacientes[index])
        }
    }
}

/** Item de paciente en cola */
@Composable
fun PacienteColaItem(
    rut: String,
    tipoExamen: String,
    hora: String,
    estado: String
) {
    val clicked = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp).clickable {},
        elevation = 1.dp,
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(12.dp),
        interactionSource = clicked
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            // Fila superior: RUT y tipo examen
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rut,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = ClinicalClarityColors.OnSurface
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Badge estado
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = when (estado) {
                        "EN REVISIÓN" -> Color(0xFFE0F2FE)
                        "PENDIENTE" -> Color(0xFFFEF3C7)
                        "VALIDADO" -> Color(0xFFDCFCE7)
                        else -> Color(0xFFF8FAFC)
                    }
                ) {
                    Text(
                        text = estado,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (estado) {
                            "EN REVISIÓN" -> ClinicalClarityColors.InReviewText
                            "PENDIENTE" -> ClinicalClarityColors.PendingText
                            "VALIDADO" -> ClinicalClarityColors.ValidatedText
                            else -> ClinicalClarityColors.OnSurfaceVariant
                        }
                    )
                }
            }

            // Fila inferior: hora y tipo examen
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = hora,
                    fontSize = 11.sp,
                    color = ClinicalClarityColors.OnSurfaceVariant
                )

                Text(
                    text = tipoExamen,
                    fontSize = 11.sp,
                    color = ClinicalClarityColors.OnSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}

/** Grid de métricas rápidas */
@Composable
fun MetricasRapidasGrid() {
    val metricas = listOf(
        MetricaRapida(
            titulo = "Precisión",
            valor = "98%",
            subtitulo = "Exactitud del equipo",
            icono = Icons.Default.TrendingUp,
            colorFondo = Color(0xFFDCFCE7),
            colorTexto = ClinicalClarityColors.ValidatedText
        ),
        MetricaRapida(
            titulo = "Tiempo Promedio",
            valor = "15 min",
            subtitulo = "Por examen",
            icono = Icons.Default.Clock,
            colorFondo = Color(0xFFE0F2FE),
            colorTexto = ClinicalClarityColors.Secondary
        ),
        MetricaRapida(
            titulo = "Colaboración",
            valor = "95%",
            subtitulo = "Pacientes colaboradores",
            icono = Icons.Default.Favorite,
            colorFondo = Color(0xFFFEF3C7),
            colorTexto = ClinicalClarityColors.PendingText
        ),
        MetricaRapida(
            titulo = "Equipo",
            valor = "5 unidades",
            subtitulo = "Disponibles",
            icono = Icons.Default.DevicesOther,
            colorFondo = Color(0xFFF8FAFC),
            colorTexto = ClinicalClarityColors.OnSurface
        )
    )

    // Two columns grid
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        metricas.forEach { meta ->
            MetricaRapidaCard(metric = meta)
        }
    }
}

/** Item de métrica rápida */
@Composable
fun MetricaRapidaCard(
    @StringRes titulo: Int,
    valor: String,
    subtitulo: String,
    icono: androidx.compose.material.icons.Icon,
    colorFondo: Color,
    colorTexto: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(colorFondo, RoundedCornerShape(12.dp))
            .padding(horizontal = 4.dp)
            .clickable {}
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // Ícono
            Icon(
                icono,
                tint = colorTexto,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Título y valor principales
            Text(
                text = "titulo placeholder",
                fontSize = 12.sp,
                color = colorTexto.copy(alpha = 0.8f)
            )
            Text(
                text = valor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )

            // Subtítulo pequeño
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitulo,
                fontSize = 10.sp,
                color = colorTexto.copy(alpha = 0.6f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

/** Grid de acciones rápidas */
@Composable
fun AccionesRapidasGrid() {
    val acciones = listOf(
        AccionRapida(
            titulo = "Nuevo Examen",
            icono = Icons.Default.Add,
            colorFondo = Color(0xFF0F80AA),
            onClick = {}
        ),
        AccionRapida(
            titulo = "Ver Mis Registros",
            icono = Icons.Default.FolderSpecial,
            colorFondo = Color(0xFFE0F2FE),
            onClick = {}
        ),
        AccionRapida(
            titulo = "Configuración",
            icono = Icons.Default.Settings,
            colorFondo = Color(0xFFF8FAFC),
            onClick = {}
        ),
        AccionRapida(
            titulo = "Ayuda",
            icono = Icons.Default.Help,
            colorFondo = Color(0xFFE2E8F0),
            onClick = {}
        )
    )

    // Two columns grid
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        acciones.forEach { accion ->
            AccionRapidaCard(accion = accion)
        }
    }
}

/** Item de acción rápida */
@Composable
fun AccionRapidaCard(
    @StringRes titulo: Int,
    icono: androidx.compose.material.icons.Icon,
    colorFondo: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(colorFondo, RoundedCornerShape(16.dp))
            .padding(horizontal = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // Ícono grande
            Icon(
                icono,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Título
            Text(
                text = "titulo placeholder",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

/** Datos de ejemplo para la cola */
private data class PacienteCola(
    val rut: String,
    val tipoExamen: String,
    val hora: String,
    val estado: String
)

/** Datos de ejemplo para métrica rápida */
private data class MetricaRapida(
    val titulo: String,
    val valor: String,
    val subtitulo: String,
    val icono: androidx.compose.material.icons.Icon,
    val colorFondo: Color,
    val colorTexto: Color
)

/** Datos de ejemplo para acción rápida */
private data class AccionRapida(
    val titulo: String,
    val icono: androidx.compose.material.icons.Icon,
    val colorFondo: Color,
    val onClick: () -> Unit
)

/** Preview de la pantalla */
@Preview(showBackground = true)
@Composable
fun TmInicioScreenPreview() {
    TmInicioScreen(
        onNavigateBack = {},
        onNuevoExamen = {},
        onVerMisRegistros = {}
    )
}