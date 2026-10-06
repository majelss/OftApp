package com.example.oftapp.ui.screems.pacientes

import com.example.oftapp.data.model.EventoClinico
import com.example.oftapp.data.model.Paciente
import com.example.oftapp.data.model.TipoEventoClinico
import com.example.oftapp.data.repository.HistorialRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HistorialRepositoryTestFake(
    private val shouldFail: Boolean = false
) : HistorialRepository {
    override suspend fun obtenerPaciente(id: Long): Paciente? {
        if (shouldFail) throw Exception("Error de red simulado")
        return Paciente(1L, "Juan", "111", 50, "Fonasa", true, "F-123")
    }

    override suspend fun obtenerEventos(pacienteId: Long): List<EventoClinico> {
        if (shouldFail) throw Exception("Error de red simulado")
        return listOf(
            EventoClinico(1L, 1L, "2026-01-01T10:00", "Examen 1", "Sub 1", "Dr. A", "Esp 1", tipo = TipoEventoClinico.EXAMEN),
            EventoClinico(2L, 1L, "2026-01-02T10:00", "Obs 1", "Sub 2", "Dr. B", "Esp 2", tipo = TipoEventoClinico.OBSERVACION),
            EventoClinico(3L, 1L, "2026-01-03T10:00", "Receta 1", "Sub 3", "Dr. A", "Esp 1", tipo = TipoEventoClinico.RECETA),
            EventoClinico(4L, 1L, "2026-01-04T10:00", "Examen 2", "Sub 4", "Dr. C", "Esp 3", tipo = TipoEventoClinico.EXAMEN)
        )
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class PatientHistoryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `filtro EXAMENES deja solo eventos de tipo examen`() = runTest {
        val viewModel = PatientHistoryViewModel(1L, HistorialRepositoryTestFake())
        advanceUntilIdle() // Espera a que termine cargar()

        viewModel.onFiltroChange(FiltroTimeline.EXAMENES)

        val state = viewModel.uiState.value
        assertEquals(2, state.timelineFiltrado.size)
        assertTrue(state.timelineFiltrado.all { it.tipo == TipoEvento.EXAMEN })
    }

    @Test
    fun `busqueda por texto reduce la lista correctamente`() = runTest {
        val viewModel = PatientHistoryViewModel(1L, HistorialRepositoryTestFake())
        advanceUntilIdle()

        // Buscar por profesional "Dr. A"
        viewModel.onBuscar("dr. a")

        val state = viewModel.uiState.value
        // Hay 2 eventos con Dr. A
        assertEquals(2, state.timelineFiltrado.size)
        assertTrue(state.timelineFiltrado.all { it.profesional == "Dr. A" })
    }

    @Test
    fun `si repositorio lanza excepcion, error no es nulo`() = runTest {
        val viewModel = PatientHistoryViewModel(1L, HistorialRepositoryTestFake(shouldFail = true))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.error)
        assertEquals("Error de red simulado", state.error)
        assertTrue(state.timeline.isEmpty())
    }
}
