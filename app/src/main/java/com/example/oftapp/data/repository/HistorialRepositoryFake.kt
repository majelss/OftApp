package com.example.oftapp.data.repository

import com.example.oftapp.data.model.EventoClinico
import com.example.oftapp.data.model.Paciente
import com.example.oftapp.data.model.TipoEventoClinico

/**
 * Implementación en memoria del repositorio de historial.
 * Todos los datos son ficticios (app académica Duoc UC, DSY1105).
 * No diagnostica ni interpreta clínicamente.
 */
class HistorialRepositoryFake : HistorialRepository {

    private val pacientes = listOf(
        Paciente(
            id = 1L,
            nombre = "Juan Pérez Villalobos",
            rut = "12.345.678-K",
            edad = 58,
            prevision = "Fonasa B",
            activo = true,
            codigoFicha = "OFT-2023-001"
        ),
        Paciente(
            id = 2L,
            nombre = "Valentina Soto Bravo",
            rut = "15.678.234-3",
            edad = 34,
            prevision = "Isapre Cruz Blanca",
            activo = true,
            codigoFicha = "OFT-2024-027"
        )
    )

    private val eventos = listOf(
        // Paciente 1
        EventoClinico(
            id = 1L, pacienteId = 1L,
            fechaIso = "2026-09-15T09:30",
            titulo = "Campimetría Computarizada",
            subtitulo = "Ojo Derecho (OD) • Control Periódico",
            profesional = "Dr. Mauricio Rojas",
            especialidad = "Oftalmología",
            estado = "Validado",
            tipo = TipoEventoClinico.EXAMEN,
            tieneBotonAccion = true,
            textoBotonAccion = "Ver Informe Examen"
        ),
        EventoClinico(
            id = 2L, pacienteId = 1L,
            fechaIso = "2026-09-08T14:15",
            titulo = "Topografía Corneal",
            subtitulo = "Ambos Ojos (AO) • Seguimiento",
            profesional = "Tec. Carmen Silva",
            especialidad = "Tecnología Oftálmica",
            estado = "Validado",
            tipo = TipoEventoClinico.EXAMEN,
            tieneBotonAccion = true,
            textoBotonAccion = "Ver Informe Examen"
        ),
        EventoClinico(
            id = 3L, pacienteId = 1L,
            fechaIso = "2026-09-01T11:00",
            titulo = "Control Oftalmológico — Receta",
            subtitulo = "Refracción • Prescripción de Lentes",
            profesional = "Dr. Mauricio Rojas",
            especialidad = "Oftalmología",
            estado = null,
            tipo = TipoEventoClinico.RECETA,
            tieneBotonAccion = true,
            textoBotonAccion = "Ver Receta Lentes"
        ),
        EventoClinico(
            id = 4L, pacienteId = 1L,
            fechaIso = "2026-08-15T10:20",
            titulo = "Evaluación Inicial",
            subtitulo = "Examen General Oftalmológico",
            profesional = "Enf. Patricia Muñoz",
            especialidad = "Enfermería Oftálmica",
            estado = null,
            tipo = TipoEventoClinico.OBSERVACION,
            tieneBotonAccion = false
        ),
        // Paciente 2
        EventoClinico(
            id = 5L, pacienteId = 2L,
            fechaIso = "2026-09-20T10:00",
            titulo = "Fondo de Ojo",
            subtitulo = "Ambos Ojos (AO) • Control Anual",
            profesional = "Dra. Lorena Fuentes",
            especialidad = "Oftalmología",
            estado = "Validado",
            tipo = TipoEventoClinico.EXAMEN,
            tieneBotonAccion = true,
            textoBotonAccion = "Ver Informe Examen"
        )
    )

    override suspend fun obtenerPaciente(id: Long): Paciente? =
        pacientes.firstOrNull { it.id == id }

    override suspend fun obtenerEventos(pacienteId: Long): List<EventoClinico> =
        eventos.filter { it.pacienteId == pacienteId }
            .sortedByDescending { it.fechaIso }
}
