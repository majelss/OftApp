package com.example.oftapp.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import com.example.oftapp.data.ExamRepository
import com.example.oftapp.model.ExamenOftalmologico
import com.example.oftapp.model.OjoEvaluado
import com.example.oftapp.model.Paciente
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class ExamRegisterViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockApp: Application
    private lateinit var mockRepo: ExamRepository
    private lateinit var viewModel: ExamRegisterViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockApp = mockk(relaxed = true)
        mockRepo = mockk(relaxed = true)
        every { mockRepo.catalogoExamenes } returns listOf(
            "Campimetría Visual Computarizada 24-2" to "Protocolo estándar SITA-Fast activado",
            "Tomografía de Coherencia Óptica (OCT)" to "Protocolo Macular 3D"
        )
        every { mockRepo.catalogoSucursales } returns listOf(
            "Sucursal Providencia - Box 3 Oftalmo",
            "Sucursal Las Condes - Box 5 Diagnóstico"
        )
        viewModel = ExamRegisterViewModel(mockApp, mockRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `seleccion de ojo, tipo de examen, sucursal y fecha actualiza el estado`() {
        viewModel.onOjoSelected(OjoEvaluado.AMBOS)
        assertEquals(OjoEvaluado.AMBOS, viewModel.uiState.value.ojoEvaluado)

        viewModel.setTipoExamenDropdownOpen(true)
        assertTrue(viewModel.uiState.value.isTipoExamenDropdownOpen)

        viewModel.onTipoExamenSelected("Tomografía de Coherencia Óptica (OCT)", "Protocolo Macular 3D")
        assertEquals("Tomografía de Coherencia Óptica (OCT)", viewModel.uiState.value.tipoExamen)
        assertEquals("Protocolo Macular 3D", viewModel.uiState.value.protocoloExamen)
        assertFalse(viewModel.uiState.value.isTipoExamenDropdownOpen)

        viewModel.setSucursalDropdownOpen(true)
        assertTrue(viewModel.uiState.value.isSucursalDropdownOpen)

        viewModel.onSucursalSelected("Sucursal Las Condes - Box 5 Diagnóstico")
        assertEquals("Sucursal Las Condes - Box 5 Diagnóstico", viewModel.uiState.value.sucursalBox)
        assertFalse(viewModel.uiState.value.isSucursalDropdownOpen)

        viewModel.onFechaExamenSeleccionada("24/10/2026 11:00")
        assertEquals("24/10/2026 11:00", viewModel.uiState.value.fechaExamen)
    }

    @Test
    fun `observaciones respeta limite maximo de 300 caracteres`() {
        viewModel.onObservacionesChanged("Paciente colabora adecuadamente.")
        assertEquals("Paciente colabora adecuadamente.", viewModel.uiState.value.observaciones)

        val textoExcesivo = "A".repeat(350)
        viewModel.onObservacionesChanged(textoExcesivo)
        assertEquals("Paciente colabora adecuadamente.", viewModel.uiState.value.observaciones)
    }

    @Test
    fun `validaciones de guardarExamen detectan fecha, tipo y sucursal vacios`() {
        // 1. Sin fecha seleccionada
        viewModel.onGuardarExamen()
        assertNotNull(viewModel.uiState.value.mensajeError)
        assertTrue(viewModel.uiState.value.mensajeError!!.contains("fecha"))

        viewModel.onDismissError()
        assertNull(viewModel.uiState.value.mensajeError)

        // 2. Sin sucursal
        viewModel.onSucursalSelected("")
        viewModel.onGuardarExamen()
        assertTrue(viewModel.uiState.value.mensajeError!!.contains("sucursal"))

        // 3. Sin tipo de examen
        viewModel.onTipoExamenSelected("", "")
        viewModel.onGuardarExamen()
        assertTrue(viewModel.uiState.value.mensajeError!!.contains("tipo de examen"))
    }

    @Test
    fun `guardarExamen con datos validos persiste en repositorio y marca guardadoExitoso`() = runTest {
        val examenCreado = ExamenOftalmologico(
            id = "EXAM-TEST-01",
            paciente = Paciente("14.821.903-2", "Mariana González Riquelme", 42),
            tipoExamen = "Campimetría Visual Computarizada 24-2",
            protocolo = "Protocolo estándar SITA-Fast activado",
            sucursalBox = "Sucursal Providencia - Box 3 Oftalmo",
            ojoEvaluado = OjoEvaluado.DERECHO,
            documentoAdjunto = null,
            observaciones = "Campo visual conservado",
            fecha = "24/10/2026 10:30"
        )
        every {
            mockRepo.crearExamenNuevo(any(), any(), any(), any(), any(), any(), any(), any())
        } returns examenCreado
        coEvery { mockRepo.guardarExamen(any(), any()) } returns true

        viewModel.onFechaExamenSeleccionada("24/10/2026 10:30")
        viewModel.onObservacionesChanged("Campo visual conservado")
        viewModel.onGuardarExamen()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.guardadoExitoso)
        assertFalse(viewModel.uiState.value.isGuardando)
        coVerify(exactly = 1) { mockRepo.guardarExamen(examenCreado, any()) }

        viewModel.onDismissGuardadoExitoso()
        assertFalse(viewModel.uiState.value.guardadoExitoso)
    }

    @Test
    fun `adjuntar y eliminar archivo lee metadatos del ContentResolver`() {
        val mockUri = mockk<Uri>(relaxed = true)
        val mockContext = mockk<Context>(relaxed = true)
        val mockResolver = mockk<ContentResolver>(relaxed = true)
        val mockCursor = mockk<Cursor>(relaxed = true)

        every { mockUri.toString() } returns "content://oftapp/informe.pdf"
        every { mockContext.contentResolver } returns mockResolver
        every { mockResolver.query(mockUri, null, null, null, null) } returns mockCursor
        every { mockCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME) } returns 0
        every { mockCursor.getColumnIndex(OpenableColumns.SIZE) } returns 1
        every { mockCursor.moveToFirst() } returns true
        every { mockCursor.getString(0) } returns "campimetria_od.pdf"
        every { mockCursor.getLong(1) } returns 2L * 1024L * 1024L

        viewModel.onArchivoSeleccionado(mockUri, mockContext)
        val doc = viewModel.uiState.value.documentoAdjunto
        assertNotNull(doc)
        assertEquals("campimetria_od.pdf", doc!!.nombreArchivo)
        assertEquals("PDF", doc.formato)

        viewModel.onEliminarArchivo()
        assertNull(viewModel.uiState.value.documentoAdjunto)
    }

    @Test
    fun `flujo de descartar cambios abre, cancela y confirma reinicio`() {
        viewModel.onObservacionesChanged("Nota temporal")
        viewModel.onSolicitarDescartar()
        assertTrue(viewModel.uiState.value.showDescartarDialog)

        viewModel.onCancelarDescartar()
        assertFalse(viewModel.uiState.value.showDescartarDialog)

        viewModel.onConfirmarDescartar()
        assertEquals("", viewModel.uiState.value.observaciones)
    }
}
