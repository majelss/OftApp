package com.example.oftapp.model

/**
 * Estados posibles de un examen en el flujo clínico del Tecnólogo Médico.
 */
enum class TmEstadoExamen(
    val etiqueta: String,
    val esEditablePorTm: Boolean
) {
    BORRADOR("Borrador", true),
    PENDIENTE("Pendiente", true),
    EN_REVISION_MEDICA("En Revisión Médica", false),
    VALIDADO("Validado", false)
}

/**
 * Datos del paciente para el Tecnólogo Médico.
 */
data class TmPaciente(
    val rut: String,
    val nombreCompleto: String,
    val edad: Int = 42,
    val sexo: String = "Femenino",
    val fichaClinica: String = "FC-89211",
    val telefono: String = "+56 9 8765 4321",
    val prevision: String = "Fonasa B"
)

/**
 * Archivo adjunto clínico cargado por el tecnólogo.
 */
data class TmArchivo(
    val id: String,
    val nombre: String,
    val tamano: String,
    val tipo: String, // "PDF", "CSV", "DICOM", "IMG"
    val isValidadoTecnicamente: Boolean = true
)

/**
 * Métricas de telemetría y confiabilidad visual de la prueba.
 */
data class TmTelemetriaVisual(
    val perdidaFijacion: String = "7% (1/14)",
    val falsosPositivos: String = "2%",
    val falsosNegativos: String = "0%"
)

/**
 * Paciente asignado en la agenda del día para el tecnólogo.
 */
data class TmAsignadoHoy(
    val id: String,
    val paciente: TmPaciente,
    val tipoExamen: String,
    val ojo: String, // "OD", "OI", "AO"
    val hora: String, // "10:30 hrs"
    val box: String = "Box 4 Diagnóstico",
    val estado: String = "Pendiente"
)

/**
 * Registro completo de examen del Tecnólogo Médico.
 */
data class TmRegistroExamen(
    val id: String,
    val codigoRegistro: String = "#REG-TM-2024-8841",
    val paciente: TmPaciente,
    val tipoExamen: String,
    val protocolo: String = "SITA-Standard 24-2 con estímulo III Blanco",
    val equipoBox: String = "Humphrey Field Analyzer 3 • Box 4 Diagnóstico",
    val ojo: String = "AO", // "OD", "OI", "AO"
    val fechaHora: String = "16 de Octubre de 2024, 10:30 hrs",
    val estado: TmEstadoExamen = TmEstadoExamen.PENDIENTE,
    val accionPendienteAlerta: String? = null,
    val observacionesTecnicas: String = "Paciente colaboradora en todo el proceso. Pérdidas de fijación 1/14 (7%), falsos positivos 2%, falsos negativos 0%. Confiabilidad alta. Se observa defecto escotomatoso arcuato incipiente en cuadrante nasal superior de OI.",
    val telemetria: TmTelemetriaVisual = TmTelemetriaVisual(),
    val archivos: List<TmArchivo> = listOf(
        TmArchivo("1", "Campimetria_24_2_MarianaGonzalez.pdf", "2.4 MB", "PDF", true),
        TmArchivo("2", "Mapa_Sensibilidad_Raw.csv", "142 KB", "CSV", false)
    ),
    val tecnologoResponsable: String = "T.M. Felipe Araya M.",
    val registroSIS: String = "SIS: 48201",
    val medicoAsignado: String? = null
)

