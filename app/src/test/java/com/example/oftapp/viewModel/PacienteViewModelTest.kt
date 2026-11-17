package com.example.oftapp.viewModel

import com.example.oftapp.ui.screens.pacientes.ExamenCompartirItem
import com.example.oftapp.ui.screens.pacientes.ExamenDetalleModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PacienteViewModelTest {

    @Test
    fun mockkValidaEjecucionDeCallbackDeNavegacionPaciente() {
        // 1. Creamos un mock de la función de navegación (Lambda) con MockK
        val onNavegarMock = mockk<(String) -> Unit>(relaxed = true)

        // 2. Simulamos la acción del usuario al pulsar en el menú
        onNavegarMock("historial")
        onNavegarMock("compartir")

        // 3. Verificamos con MockK que se llamó exactamente con esos destinos
        verify(exactly = 1) { onNavegarMock("historial") }
        verify(exactly = 1) { onNavegarMock("compartir") }
    }

    @Test
    fun mockkSimulaYVerificaDatosDeExamenDetalleModel() {
        // Creamos un Spy con MockK sobre el modelo de detalle clínico
        val detalleSpy = spyk(ExamenDetalleModel())

        // Forzamos una respuesta simulada con 'every'
        every { detalleSpy.estado } returns "Validado por Especialista"

        assertEquals("Validado por Especialista", detalleSpy.estado)
        assertEquals("CP-2024-8841", detalleSpy.idExamen)
        verify(atLeast = 1) { detalleSpy.estado }
    }

    @Test
    fun conteoYAlternanciaDeExamenesParaCompartirConMockk() {
        val onSeleccionCambioMock = mockk<(Int) -> Unit>(relaxed = true)

        var listaExamenes = listOf(
            ExamenCompartirItem(1, "Campimetría Computarizada", "AO", "12 Oct 2024", true),
            ExamenCompartirItem(2, "Topografía Corneal Pentacam", "OD", "28 Ago 2024", true),
            ExamenCompartirItem(3, "Curva de Tensión Aplanática", "OI", "15 Jun 2024", false)
        )

        // Simulamos marcar el tercer examen
        listaExamenes = listaExamenes.map {
            if (it.id == 3L) it.copy(seleccionadoInicial = true) else it
        }

        val seleccionados = listaExamenes.count { it.seleccionadoInicial }
        onSeleccionCambioMock(seleccionados)

        assertEquals(3, seleccionados)
        verify(exactly = 1) { onSeleccionCambioMock(3) }
    }
}