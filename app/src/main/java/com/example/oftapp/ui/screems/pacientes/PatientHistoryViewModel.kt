package com.example.oftapp.ui.screems.pacientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PacienteUi(
    val nombre: String = "",
    val rut: String = "",
    val edad: String = "",
    val fonasa: String = "",
    val activo: Boolean = false
)

enum class TipoEvento {
    EXAMEN,
    OBSERVACION,
    RECETA
}

data class EventoTimeline(
    val id: Long,
    val fecha: String,
    val etiquetaRelativa: String,
    val titulo: String,
    val subtitulo: String,
    val profesional: String,
    val especialidad: String,
    val estadoLabel: String? = null,
    val tipo: TipoEvento = TipoEvento.EXAMEN,
    val tieneBotonAccion: Boolean = false,
    val textoBotonAccion: String = "Ver Informe Examen"
)

enum class FiltroTimeline(val label: String) {
    TODOS("Todos los eventos"),
    EXAMENES("Exámenes"),
    OBSERVACIONES("Observaciones"),
    RECETAS("Recetas")
}

data class PatientHistoryUiState(
    val isLoading: Boolean = true,
    val paciente: PacienteUi? = null,
    val timeline: List<EventoTimeline> = emptyList(),
    val timelineFiltrado: List<EventoTimeline> = emptyList(),
    val filtro: FiltroTimeline = FiltroTimeline.TODOS,
    val error: String? = null
)

class PatientHistoryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PatientHistoryUiState())
    val uiState: StateFlow<PatientHistoryUiState> = _uiState.asStateFlow()

    init { cargarMock() }

    fun onFiltroChange(filtro: FiltroTimeline) {
        _uiState.update { state ->
            val filtrado = when (filtro) {
                FiltroTimeline.TODOS -> state.timeline
                FiltroTimeline.EXAMENES -> state.timeline.filter { it.tipo == TipoEvento.EXAMEN }
                FiltroTimeline.OBSERVACIONES -> state.timeline.filter { it.tipo == TipoEvento.OBSERVACION }
                FiltroTimeline.RECETAS -> state.timeline.filter { it.tipo == TipoEvento.RECETA }
            }
            state.copy(filtro = filtro, timelineFiltrado = filtrado)
        }
    }

    private fun cargarMock() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val paciente = PacienteUi("Juan Pérez Villalobos", "12.345.678-K", "58", "Fonasa B", true)
            val timeline = listOf(
                EventoTimeline(1, "15 Sep 2026 • 09:30", "Hace 2 días", "Campimetría Computarizada", "Ojo Derecho (OD) • Glaucoma", "Dr. Mauricio Rojas", "Oftalmología", "Validado", TipoEvento.EXAMEN, true, "Ver Informe Examen"),
                EventoTimeline(2, "08 Sep 2026 • 14:15", "Hace 9 días", "Topografía Corneal", "Ambos Ojos (AO) • Astigmatismo", "Tec. Carmen Silva", "Tecnología Oftálmica", "Validado", TipoEvento.EXAMEN, true, "Ver Informe Examen"),
                EventoTimeline(3, "01 Sep 2026 • 11:00", "Hace 16 días", "Control Oftalmológico - Receta", "Refracción • Prescripción de Lentes", "Dr. Mauricio Rojas", "Oftalmología", tipo = TipoEvento.RECETA, tieneBotonAccion = true, textoBotonAccion = "Ver Receta Lentes"),
                EventoTimeline(4, "15 Ago 2026 • 10:20", "Hace 1 mes", "Evaluación Inicial", "Examen General Oftalmológico", "Enf. Patricia Muñoz", "Enfermería Oftálmica", tipo = TipoEvento.OBSERVACION, tieneBotonAccion = false)
            )
            _uiState.update { it.copy(isLoading = false, paciente = paciente, timeline = timeline, timelineFiltrado = timeline, error = null) }
        }
    }
}
