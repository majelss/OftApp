package com.example.oftapp.viewmodel

import android.app.Application
import com.example.oftapp.data.ExamRepository
import com.example.oftapp.data.remote.FhirReferenciaSintetica
import com.example.oftapp.model.ExamenOftalmologico
import com.example.oftapp.model.OjoEvaluado
import com.example.oftapp.model.Paciente
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExamListAndDetailViewModelTest {

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
    fun `ExamListViewModel mapea examenes y ejecuta sincronizacion y FHIR`() = runTest {
        val mockApp = mockk<Application>(relaxed = true)
        val mockRepo = mockk<ExamRepository>(relaxed = true)
        val examenSample = ExamenOftalmologico(
            id = "EXAM-2026-001",
            paciente = Paciente("14.821.903-2", "Mariana González", 42),
            tipoExamen = "Campimetría 24-2",
            protocolo = "SITA-Fast",
            sucursalBox = "Providencia Box 3",
            ojoEvaluado = OjoEvaluado.DERECHO,
            documentoAdjunto = null,
            observaciones = "Normal",
            estado = "Validado",
            fecha = "24/10/2026 10:30",
            pendienteSincronizacion = true
        )

        every { mockRepo.examenes } returns flowOf(listOf(examenSample))
        every { mockRepo.isOffline } returns MutableStateFlow(false)
        every { mockRepo.pendientesCount } returns MutableStateFlow(1)
        every { mockRepo.estadoSincronizacion } returns MutableStateFlow("Sincronización pendiente (1)")
        every { mockRepo.reportesFhirExternos } returns MutableStateFlow(
            listOf(FhirReferenciaSintetica("FHIR-1", "LOINC 1", "OCT", "FINAL", "2026-10-24"))
        )
        every { mockRepo.estadoFhirExterno } returns MutableStateFlow("HAPI FHIR R4 Activo")
        coEvery { mockRepo.refrescarDesdeBackend(any()) } returns true
        coEvery { mockRepo.cargarReferenciasFhirExternas() } returns emptyList()
        coEvery { mockRepo.sincronizarPendientes(any()) } returns true
        coEvery { mockRepo.alternarModoOfflineDemo() } returns true

        val vm = ExamListViewModel(mockApp, mockRepo)
        advanceUntilIdle()

        val lista = vm.atenciones.first()
        assertEquals(1, lista.size)
        assertEquals("EXAM-2026-001", lista[0].id)
        assertEquals("OD", lista[0].ojo)
        assertTrue(lista[0].pendienteSincronizacion)

        vm.sincronizarAhora()
        vm.alternarModoOfflineDemo()
        vm.recargarFhirExterno()
        advanceUntilIdle()

        coVerify { mockRepo.sincronizarPendientes(any()) }
        coVerify { mockRepo.alternarModoOfflineDemo() }
        coVerify(atLeast = 2) { mockRepo.cargarReferenciasFhirExternas() }
    }

    @Test
    fun `ExamDetailViewModel gestiona zoom, pantalla completa, estados y exportacion`() {
        val vm = ExamDetailViewModel()

        assertEquals(100, vm.uiState.value.zoomPercentage)
        vm.onZoomIn()
        assertEquals(125, vm.uiState.value.zoomPercentage)
        repeat(6) { vm.onZoomIn() }
        assertEquals(200, vm.uiState.value.zoomPercentage)

        vm.onZoomOut()
        assertEquals(175, vm.uiState.value.zoomPercentage)
        repeat(10) { vm.onZoomOut() }
        assertEquals(50, vm.uiState.value.zoomPercentage)

        vm.onToggleFullscreen()
        assertTrue(vm.uiState.value.isFullscreenMap)
        vm.onToggleFullscreen()
        assertFalse(vm.uiState.value.isFullscreenMap)

        vm.onExportPdf()
        assertNotNull(vm.uiState.value.snackbarMessage)
        vm.onDismissSnackbar()
        assertNull(vm.uiState.value.snackbarMessage)

        vm.onExportDicom()
        assertNotNull(vm.uiState.value.snackbarMessage)
        vm.onOpenHistorico()
        assertNotNull(vm.uiState.value.snackbarMessage)

        vm.onOpenActualizarEstadoDialog()
        assertTrue(vm.uiState.value.showActualizarEstadoDialog)
        vm.onCloseActualizarEstadoDialog()
        assertFalse(vm.uiState.value.showActualizarEstadoDialog)

        vm.onSeleccionarNuevoEstado("Entregado")
        assertEquals("Entregado", vm.uiState.value.examen.estado)

        vm.onOpenReabrirDialog()
        assertTrue(vm.uiState.value.showReabrirDialog)
        vm.onCloseReabrirDialog()
        assertFalse(vm.uiState.value.showReabrirDialog)

        vm.onConfirmarReapertura()
        assertEquals("Observado", vm.uiState.value.examen.estado)
    }

    @Test
    fun `DashboardViewModel expone estadisticas y proximas atenciones iniciales`() {
        val vm = DashboardViewModel()
        assertEquals(3, vm.estadisticas.size)
        assertEquals(24, vm.estadisticas[0].cantidad)
        assertEquals(3, vm.proximasAtenciones.size)
        assertEquals("Mariana Ramírez", vm.proximasAtenciones[0].nombrePaciente)
    }
}
