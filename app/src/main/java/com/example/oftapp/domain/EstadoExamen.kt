package com.example.oftapp.domain

/**
 * Máquina de estados clínicos centralizada para el flujo de exámenes.
 * Regula la editabilidad por parte del Tecnólogo Médico y las acciones urgentes.
 */
enum class EstadoExamen(
    val etiqueta: String,
    val esEditablePorTm: Boolean,
    val requiereAccionInmediata: Boolean = false
) {
    BORRADOR("Borrador", esEditablePorTm = true),
    PENDIENTE("Pendiente", esEditablePorTm = true, requiereAccionInmediata = true),
    EN_REVISION("En Revisión Médica", esEditablePorTm = false),
    VALIDADO("Validado", esEditablePorTm = false),
    OBSERVADO("Observado por Médico", esEditablePorTm = true, requiereAccionInmediata = true),
    ENTREGADO("Entregado a Paciente", esEditablePorTm = false);

    companion object {
        /** Mapea un string libre (de Room) a un EstadoExamen seguro. */
        fun desde(texto: String?): EstadoExamen {
            return entries.firstOrNull {
                it.etiqueta.equals(texto, ignoreCase = true) || it.name.equals(texto, ignoreCase = true)
            } ?: BORRADOR
        }
    }
}

