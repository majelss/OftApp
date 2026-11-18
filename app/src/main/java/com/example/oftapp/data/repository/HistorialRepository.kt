package com.example.oftapp.data.repository

import com.example.oftapp.data.model.EventoClinico
import com.example.oftapp.data.model.Paciente

interface HistorialRepository {
    suspend fun obtenerPaciente(id: Long): Paciente?
    suspend fun obtenerEventos(pacienteId: Long): List<EventoClinico>
}
