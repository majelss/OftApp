package com.example.oftapp.ui.screems.pacientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.oftapp.data.model.EventoClinico
import com.example.oftapp.data.model.TipoEventoClinico
import com.example.oftapp.data.repository.HistorialRepository
import com.example.oftapp.data.repository.HistorialRepositoryFake
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

// ─── UI Models ───────────────────────────────────────────────────────────────

data class PacienteUi(
    val nombre: String = "",
    val rut: String = "",
    val edad: String = "",
    val fonasa: String = "",
    val activo: Boolean = false,
    val codigoFicha: String = ""
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
    val consulta: String = "",
    val error: String? = null
)

// ─── ViewModel ───────────────────────────────────────────────────────────────

class PatientHistoryViewModel(
    private val pacienteId: Long,
    private val repository: HistorialRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientHistoryUiState())
    val uiState: StateFlow<PatientHistoryUiState> = _uiState.asStateFlow()

    init {
        cargar()
    }

    fun onFiltroChange(filtro: FiltroTimeline) {
        _uiState.update { state ->
            state.copy(
                filtro = filtro,
                timelineFiltrado = filtrar(state.timeline, filtro, state.consulta)
            )
        }
    }

    fun onBuscar(texto: String) {
        _uiState.update { state ->
            state.copy(
                consulta = texto,
                timelineFiltrado = filtrar(state.timeline, state.filtro, texto)
            )
        }
    }

    fun reintentar() = cargar()

    private fun cargar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val paciente = repository.obtenerPaciente(pacienteId)
                val eventos = repository.obtenerEventos(pacienteId)
                val timeline = eventos.map { mapearEvento(it) }
                val pacienteUi = paciente?.let {
                    PacienteUi(
                        nombre = it.nombre,
                        rut = it.rut,
                        edad = it.edad.toString(),
                        fonasa = it.prevision,
                        activo = it.activo,
                        codigoFicha = it.codigoFicha
                    )
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        paciente = pacienteUi,
                        timeline = timeline,
                        timelineFiltrado = filtrar(timeline, it.filtro, it.consulta),
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error desconocido al cargar el historial"
                    )
                }
            }
        }
    }

    private fun filtrar(
        lista: List<EventoTimeline>,
        filtro: FiltroTimeline,
        consulta: String
    ): List<EventoTimeline> {
        val porTipo = when (filtro) {
            FiltroTimeline.TODOS -> lista
            FiltroTimeline.EXAMENES -> lista.filter { it.tipo == TipoEvento.EXAMEN }
            FiltroTimeline.OBSERVACIONES -> lista.filter { it.tipo == TipoEvento.OBSERVACION }
            FiltroTimeline.RECETAS -> lista.filter { it.tipo == TipoEvento.RECETA }
        }
        return if (consulta.isBlank()) porTipo
        else {
            val q = consulta.lowercase()
            porTipo.filter { ev ->
                ev.titulo.lowercase().contains(q) ||
                    ev.subtitulo.lowercase().contains(q) ||
                    ev.profesional.lowercase().contains(q)
            }
        }
    }

    // ─── Mapeo EventoClinico → EventoTimeline ────────────────────────────────

    private fun mapearEvento(evento: EventoClinico): EventoTimeline {
        return EventoTimeline(
            id = evento.id,
            fecha = formatearFecha(evento.fechaIso),
            etiquetaRelativa = calcularEtiquetaRelativa(evento.fechaIso),
            titulo = evento.titulo,
            subtitulo = evento.subtitulo,
            profesional = evento.profesional,
            especialidad = evento.especialidad,
            estadoLabel = evento.estado,
            tipo = when (evento.tipo) {
                TipoEventoClinico.EXAMEN -> TipoEvento.EXAMEN
                TipoEventoClinico.OBSERVACION -> TipoEvento.OBSERVACION
                TipoEventoClinico.RECETA -> TipoEvento.RECETA
            },
            tieneBotonAccion = evento.tieneBotonAccion,
            textoBotonAccion = evento.textoBotonAccion
        )
    }

    /**
     * Formatea "2026-09-15T09:30" → "15 Sep 2026 • 09:30"
     * Compatible con minSdk 24 (sin java.time).
     */
    private fun formatearFecha(iso: String): String {
        return try {
            val parts = iso.split("T")
            val dateParts = parts[0].split("-")
            val year = dateParts[0]
            val month = dateParts[1].toInt()
            val day = dateParts[2].toInt()
            val time = if (parts.size > 1) parts[1] else ""
            val monthName = listOf(
                "Ene", "Feb", "Mar", "Abr", "May", "Jun",
                "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
            ).getOrElse(month - 1) { "?" }
            if (time.isNotEmpty()) "$day $monthName $year • $time"
            else "$day $monthName $year"
        } catch (e: Exception) {
            iso
        }
    }

    /**
     * Calcula etiqueta relativa desde fecha ISO "2026-09-15T09:30".
     * Compatible con minSdk 24.
     */
    private fun calcularEtiquetaRelativa(iso: String): String {
        return try {
            val datePart = iso.split("T")[0].split("-")
            val year = datePart[0].toInt()
            val month = datePart[1].toInt() - 1 // Calendar.MONTH es 0-based
            val day = datePart[2].toInt()

            val eventoTime = Calendar.getInstance().apply {
                set(year, month, day, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val hoy = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val diffMs = hoy - eventoTime
            val diffDays = (diffMs / (1000L * 60 * 60 * 24)).toInt()

            when {
                diffDays == 0 -> "Hoy"
                diffDays == 1 -> "Ayer"
                diffDays < 7 -> "Hace $diffDays días"
                diffDays < 14 -> "Hace 1 semana"
                diffDays < 30 -> "Hace ${diffDays / 7} semanas"
                diffDays < 60 -> "Hace 1 mes"
                diffDays < 365 -> "Hace ${diffDays / 30} meses"
                else -> "Hace ${diffDays / 365} año(s)"
            }
        } catch (e: Exception) {
            ""
        }
    }

    // ─── Factory ─────────────────────────────────────────────────────────────

    companion object {
        fun factory(pacienteId: Long, repository: HistorialRepository = HistorialRepositoryFake()): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PatientHistoryViewModel(pacienteId, repository) as T
                }
            }
        }
    }
}
