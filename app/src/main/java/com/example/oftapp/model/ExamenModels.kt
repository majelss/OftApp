package com.example.oftapp.model

/**
 * Modelo para datos ficticios del paciente (según requerimientos académicos DSY1105).
 */
data class Paciente(
    val rut: String,
    val nombreCompleto: String,
    val edad: Int,
    val isValidado: Boolean = true
)

/**
 * Lateralidad oftalmológica para el examen.
 */
enum class OjoEvaluado(
    val codigo: String,
    val titulo: String,
    val detalle: String
) {
    DERECHO("OD", "Ojo Derecho (OD)", "Agudeza 20/25 • PIO 14 mmHg"),
    IZQUIERDO("OI", "Ojo Izquierdo (OI)", "Sin exploración reciente"),
    AMBOS("AO", "Ambos Ojos (AO)", "Protocolo bilateral completo")
}

/**
 * Documento clínico simulado (PDF, DICOM o JPG).
 */
data class DocumentoAdjunto(
    val nombreArchivo: String,
    val tamano: String,
    val tiempoSubida: String,
    val formato: String = "PDF"
)

/**
 * Registro completo de examen oftalmológico para trazabilidad clínica.
 */
data class ExamenOftalmologico(
    val id: String,
    val paciente: Paciente,
    val tipoExamen: String,
    val protocolo: String,
    val sucursalBox: String,
    val ojoEvaluado: OjoEvaluado,
    val documentoAdjunto: DocumentoAdjunto?,
    val observaciones: String,
    val estado: String = "Pendiente",
    val fecha: String,
    val firmaElectronica: String = "Dr. C. Mendoza"
)

/**
 * Modelo detallado para la pantalla de visualización e interpretación
 * de campimetría y exámenes diagnósticos (RF06, RF08, RF09).
 */
data class ExamenVisualDetalle(
    val idExamen: String = "EXAMEN #CP-2024-8841",
    val paciente: Paciente = Paciente(
        rut = "14.821.903-2",
        nombreCompleto = "Mariana González R.",
        edad = 46,
        isValidado = true
    ),
    val estado: String = "Validado",
    val tipoExamen: String = "Campimetría Computarizada Humphrey 30-2 (OD - Ojo Derecho)",
    val fechaHora: String = "24 Oct 2024 • 10:30 hrs",
    val medico: String = "Dr. Carlos Morales",
    val md: String = "-2.14 dB",
    val mdProb: String = "p < 5%",
    val psd: String = "+1.85 dB",
    val psdProb: String = "p < 2%",
    val vfi: String = "97%",
    val vfiStatus: String = "Confiable",
    val lateralidadCampo: String = "OD (Ojo Derecho) - 30° Humphrey Field",
    val fijacion: String = "FIJACIÓN: 0/14",
    val fijacionDetalle: String = "PÉRDIDAS",
    val observaciones: String = "Escotoma relativo paracentral superonasal leve en ojo derecho compatible con evolución tensional. Nervio óptico con excavación fisiológica 0.4. Se sugiere control de PIO en 3 meses y mantener lubricación ocular.",
    val especialidad: String = "Oftalmología General",
    val validadorNombre: String = "Dr. C. Morales",
    val validadorIniciales: String = "CM",
    val validadorRegistro: String = "Reg. Med. 44921 • Especialista en Glaucoma"
)

