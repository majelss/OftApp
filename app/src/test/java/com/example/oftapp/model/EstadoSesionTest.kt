package com.example.oftapp.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EstadoSesionTest {

    @Test
    fun estadoInicialPorDefectoDebeEstarNoAutenticado() {
        val sesion = EstadoSesion()

        assertFalse(sesion.estaAutenticado)
        assertNull(sesion.usuarioId)
        assertFalse(sesion.esPaciente())
    }

    @Test
    fun sesionConRolPacienteDebeSerReconocidaPorEsPaciente() {
        val sesionPaciente = EstadoSesion(
            usuarioId = 1L,
            rol = Roles.PACIENTE,
            estaAutenticado = true
        )

        assertTrue(sesionPaciente.estaAutenticado)
        assertTrue(sesionPaciente.esPaciente())
        assertEquals(1L, sesionPaciente.usuarioId)
    }

    @Test
    fun sesionConRolMedicoNoDebeEntrarAlFlujoDePaciente() {
        val sesionMedico = EstadoSesion(
            usuarioId = 99L,
            rol = Roles.MEDICO,
            estaAutenticado = true
        )

        assertTrue(sesionMedico.estaAutenticado)
        assertFalse(sesionMedico.esPaciente())
    }

    @Test
    fun cerrarSesionDebeLimpiarCredencialesYEstadoAutenticado() {
        var sesion = EstadoSesion(
            usuarioId = 1L,
            rol = Roles.PACIENTE,
            estaAutenticado = true
        )

        sesion = EstadoSesion(
            usuarioId = null,
            rol = null,
            estaAutenticado = false
        )

        assertFalse(sesion.estaAutenticado)
        assertNull(sesion.usuarioId)
        assertFalse(sesion.esPaciente())
    }
}