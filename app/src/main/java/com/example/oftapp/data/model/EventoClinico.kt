package com.example.oftapp.data.model

/**
 * Tipo de evento clínico registrado en el historial.
 */
enum class TipoEventoClinico {
    EXAMEN,
    OBSERVACION,
    RECETA
}

/**
 * Evento clínico almacenado en el repositorio.
 * La fecha se guarda en formato ISO-8601 (yyyy-MM-ddTHH:mm) para poder
 * calcular etiquetas relativas en el ViewModel sin depender de java.time (API 26+).
 */
data class EventoClinico(
    val id: Long,
    val pacienteId: Long,
    val fechaIso: String,          // ej. "2026-09-15T09:30"
    val titulo: String,
    val subtitulo: String,
    val profesional: String,
    val especialidad: String,
    val estado: String? = null,    // "Validado", "Pendiente", etc.
    val tipo: TipoEventoClinico = TipoEventoClinico.EXAMEN,
    val tieneBotonAccion: Boolean = false,
    val textoBotonAccion: String = "Ver Informe Examen"
)
