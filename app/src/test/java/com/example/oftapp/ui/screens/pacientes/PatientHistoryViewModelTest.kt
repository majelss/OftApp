package com.example.oftapp.ui.screens.pacientes

import com.example.oftapp.data.model.EventoClinico
import com.example.oftapp.data.model.Paciente
import com.example.oftapp.data.model.TipoEventoClinico
import com.example.oftapp.data.repository.HistorialRepository
import com.example.oftapp.viewmodel.FiltroTimeline
import com.example.oftapp.viewmodel.PatientHistoryViewModel
import com.example.oftapp.viewmodel.TipoEvento
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HistorialRepositoryTestFake(
    private val shouldFail: Boolean = false
) : HistorialRepository {
    override suspend fun obtenerPaciente(id: Long): Paciente? {
        if (shouldFail) throw Exception("Error de red simulado")
        return Paciente(1L, "Juan", "111", 50, "Fonasa", true, "F-123")
    }

    override suspend fun obtenerEventos(pacienteId: Long): List<EventoClinico> {
        if (shouldFail) throw Exception("Error de red simulado")
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val hoy = sdf.format(Calendar.getInstance().time)
        val ayer = sdf.format(Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.time)
        val hace4Dias = sdf.format(Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -4) }.time)
        val hace10Dias = sdf.format(Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -10) }.time)
        val hace20Dias = sdf.format(Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -20) }.time)
        val hace45Dias = sdf.format(Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -45) }.time)
        val hace120Dias = sdf.format(Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -120) }.time)
        val hace400Dias = sdf.format(Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -400) }.time)

        return listOf(
            EventoClinico(1L, 1L, hoy, "Examen 1", "Sub 1", "Dr. A", "Esp 1", tipo = TipoEventoClinico.EXAMEN),
            EventoClinico(2L, 1L, ayer, "Obs 1", "Sub 2", "Dr. B", "Esp 2", tipo = TipoEventoClinico.OBSERVACION),
            EventoClinico(3L, 1L, hace4Dias, "Receta 1", "Sub 3", "Dr. A", "Esp 1", tipo = TipoEventoClinico.RECETA),
            EventoClinico(4L, 1L, hace10Dias, "Examen 2", "Sub 4", "Dr. C", "Esp 3", tipo = TipoEventoClinico.EXAMEN),
            EventoClinico(5L, 1L, hace20Dias, "Obs 2", "Sub 5", "Dr. D", "Esp 2", tipo = TipoEventoClinico.OBSERVACION),
            EventoClinico(6L, 1L, hace45Dias, "Receta 2", "Sub 6", "Dr. E", "Esp 1", tipo = TipoEventoClinico.RECETA),
            EventoClinico(7L, 1L, hace120Dias, "Examen 3", "Sub 7", "Dr. F", "Esp 3", tipo = TipoEventoClinico.EXAMEN),
            EventoClinico(8L, 1L, hace400Dias, "Examen 4", "Sub 8", "Dr. G", "Esp 1", tipo = TipoEventoClinico.EXAMEN)
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
    fun `todos los filtros de timeline y reintentar funcionan correctamente`() = runTest {
        val viewModel = PatientHistoryViewModel(1L, HistorialRepositoryTestFake())
        advanceUntilIdle()

        viewModel.onFiltroChange(FiltroTimeline.EXAMENES)
        assertTrue(viewModel.uiState.value.timelineFiltrado.all { it.tipo == TipoEvento.EXAMEN })

        viewModel.onFiltroChange(FiltroTimeline.OBSERVACIONES)
        assertTrue(viewModel.uiState.value.timelineFiltrado.all { it.tipo == TipoEvento.OBSERVACION })

        viewModel.onFiltroChange(FiltroTimeline.RECETAS)
        assertTrue(viewModel.uiState.value.timelineFiltrado.all { it.tipo == TipoEvento.RECETA })

        viewModel.onFiltroChange(FiltroTimeline.TODOS)
        assertEquals(8, viewModel.uiState.value.timelineFiltrado.size)

        viewModel.reintentar()
        advanceUntilIdle()
        assertNotNull(viewModel.uiState.value.paciente)
    }

    @Test
    fun `busqueda por texto reduce la lista correctamente`() = runTest {
        val viewModel = PatientHistoryViewModel(1L, HistorialRepositoryTestFake())
        advanceUntilIdle()

        viewModel.onBuscar("dr. a")
        val state = viewModel.uiState.value
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
