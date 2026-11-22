package com.example.oftapp.ui.screens.tm.registro

import java.time.LocalDate

/**
 * Estado del formulario para registrar un examen del Tecnólogo Médico.
 * En producción los valores están vacíos; la función [preview] devuelve un ejemplo
 * útil para los previews de Compose.
 */
data class ExamenFormState(
    val rut: String = "",
    val rutValido: Boolean = false,
    val pacienteVerificado: String? = null, // nombre del paciente verificado
    val tipoExamen: String = "",
    val ojoEvaluado: OjoEvaluado = OjoEvaluado.DERECHO,
    val sucursalBox: String = "",
    val fecha: String = "",
    val hora: String = "",
    val observaciones: String = "",
    val archivoAdjunto: String? = null, // URI del archivo
    val errorMensaje: String? = null
) {
    /** Número de campos críticos completados (para la barra "5 de 5 campos listos"). */
    val camposCompletados: Int
        get() {
            var count = 0
            if (rutValido && pacienteVerificado != null) count++
            if (tipoExamen.isNotBlank()) count++
            if (ojoEvaluado != OjoEvaluado.DESCONOCIDO) count++
            if (sucursalBox.isNotBlank()) count++
            if (archivoAdjunto != null) count++
            return count
        }

    val isReadyToSubmit: Boolean
        get() = camposCompletados == 5

    companion object {
        /** Example data for Compose preview – not used in production. */
        fun preview(): ExamenFormState = ExamenFormState(
            rut = "15.420.895-K",
            rutValido = true,
            pacienteVerificado = "Mariana González",
            tipoExamen = "Campimetría Computarizada Humphrey",
            ojoEvaluado = OjoEvaluado.DERECHO,
            sucursalBox = "Clínica Oftalmológica Andes - Box 4 Diagnóstico",
            fecha = LocalDate.now().toString(),
            hora = "09:30",
            observaciones = "Sin observaciones",
            archivoAdjunto = null,
            errorMensaje = null
        )
    }
}

enum class OjoEvaluado { DERECHO, IZQUIERDO, AMBOS, DESCONOCIDO }
