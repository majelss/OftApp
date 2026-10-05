package com.example.oftapp.ui.screems.pacientes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oftapp.ui.screems.pacientes.components.PatientSummaryCard
import com.example.oftapp.ui.screems.pacientes.components.TimelineEndMarker
import com.example.oftapp.ui.screems.pacientes.components.TimelineFilterBar
import com.example.oftapp.ui.screems.pacientes.components.TimelineNodeCard
import com.example.oftapp.ui.theme.ClinicalBackground
import com.example.oftapp.ui.theme.OftAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientHistoryScreen(
    viewModel: PatientHistoryViewModel = viewModel(),
    onBack: () -> Unit = {},
    onExamenClick: (Long) -> Unit = {},
    onVerDocumento: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    PatientHistoryContent(
        uiState = uiState,
        onBack = onBack,
        onFiltroChange = { viewModel.onFiltroChange(it) },
        onExamenClick = onExamenClick,
        onVerDocumento = onVerDocumento
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PatientHistoryContent(
    uiState: PatientHistoryUiState,
    onBack: () -> Unit = {},
    onFiltroChange: (FiltroTimeline) -> Unit = {},
    onExamenClick: (Long) -> Unit = {},
    onVerDocumento: (Long) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Historial Clínico", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("Ficha Oftalmológica #8492", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = ClinicalBackground
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Error al cargar historial", style = MaterialTheme.typography.titleMedium)
                    Text(uiState.error, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    item {
                        uiState.paciente?.let { p ->
                            PatientSummaryCard(
                                nombre = p.nombre, rut = p.rut, edad = p.edad, fonasa = p.fonasa, activo = p.activo,
                                atenciones = uiState.timelineFiltrado.size,
                                validados = uiState.timeline.count { it.estadoLabel == "Validado" },
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }
                    }
                    item {
                        TimelineFilterBar(
                            registrosCount = uiState.timelineFiltrado.size,
                            filtroSeleccionado = uiState.filtro.label,
                            onFiltroChange = { opcion ->
                                val f = when (opcion) {
                                    "Exámenes" -> FiltroTimeline.EXAMENES
                                    "Observaciones" -> FiltroTimeline.OBSERVACIONES
                                    "Recetas" -> FiltroTimeline.RECETAS
                                    else -> FiltroTimeline.TODOS
                                }
                                onFiltroChange(f)
                            }
                        )
                    }
                    itemsIndexed(uiState.timelineFiltrado) { index, item ->
                        TimelineNodeCard(
                            fecha = item.fecha, etiquetaRelativa = item.etiquetaRelativa, titulo = item.titulo, subtitulo = item.subtitulo,
                            profesional = item.profesional, especialidad = item.especialidad, estadoLabel = item.estadoLabel,
                            tieneBotonAccion = item.tieneBotonAccion, textoBotonAccion = item.textoBotonAccion,
                            onBotonAccionClick = { if (item.tipo == TipoEvento.EXAMEN) onExamenClick(item.id) else onVerDocumento(item.id) },
                            mostrarLinea = index < uiState.timelineFiltrado.lastIndex,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                    item { TimelineEndMarker(modifier = Modifier.padding(top = 8.dp).padding(horizontal = 20.dp)) }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientHistoryScreenPreview() {
    OftAppTheme {
        val p = PacienteUi("Juan Pérez Villalobos", "12.345.678-K", "58", "Fonasa B", true)
        val t = listOf(
            EventoTimeline(1, "15 Sep 2026 • 09:30", "Hace 2 días", "Campimetría Computarizada", "Ojo Derecho (OD) • Glaucoma", "Dr. Mauricio Rojas", "Oftalmología", "Validado", TipoEvento.EXAMEN, true),
            EventoTimeline(2, "08 Sep 2026 • 14:15", "Hace 9 días", "Topografía Corneal", "Ambos Ojos (AO) • Astigmatismo", "Tec. Carmen Silva", "Tecnología Oftálmica", "Validado", TipoEvento.EXAMEN, true)
        )
        PatientHistoryContent(uiState = PatientHistoryUiState(isLoading = false, paciente = p, timeline = t, timelineFiltrado = t))
    }
}
