package com.example.oftapp.data

import com.example.oftapp.data.local.AdminDao
import com.example.oftapp.data.local.ExamenDao
import com.example.oftapp.data.local.ExamenEntity
import com.example.oftapp.data.local.UsuarioAdminEntity
import com.example.oftapp.data.remote.ExamenRemoteDto
import com.example.oftapp.data.remote.FhirBundleEntry
import com.example.oftapp.data.remote.FhirBundleResponse
import com.example.oftapp.data.remote.FhirCodeableConcept
import com.example.oftapp.data.remote.FhirCoding
import com.example.oftapp.data.remote.FhirDiagnosticResource
import com.example.oftapp.data.remote.FhirExternalApiService
import com.example.oftapp.data.remote.OftAppApiService
import com.example.oftapp.data.repository.HistorialRepositoryFake
import com.example.oftapp.model.DocumentoAdjunto
import com.example.oftapp.model.EstadoSesion
import com.example.oftapp.model.ExamenVisualDetalle
import com.example.oftapp.model.OjoEvaluado
import com.example.oftapp.model.Paciente
import com.example.oftapp.model.Roles
import com.example.oftapp.model.esAdmin
import com.example.oftapp.model.esPaciente
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExamAndAdminRepositoryTest {

    @Test
    fun `ExamRepository sincroniza online y aplica fallback Room en Modo Offline`() = runTest {
        val mockDao = mockk<ExamenDao>(relaxed = true)
        val mockApi = mockk<OftAppApiService>(relaxed = true)
        val mockFhir = mockk<FhirExternalApiService>(relaxed = true)

        val entityLocal = ExamenEntity(
            id = "EXAM-2026-001",
            pacienteRut = "14.821.903-2",
            pacienteNombre = "Mariana González",
            pacienteEdad = 42,
            tipoExamen = "Campimetría 24-2",
            protocolo = "SITA-Fast",
            sucursalBox = "Providencia Box 3",
            ojoEvaluado = "OD",
            documentoNombre = "informe.pdf",
            documentoTamano = "2.1 MB",
            documentoFormato = "PDF",
            documentoUri = "content://test/1",
            observaciones = "Normal",
            estado = "Validado",
            fecha = "24/10/2026 10:30",
            firmaElectronica = "Dr. C. Mendoza",
            pendienteSincronizacion = true
        )
        every { mockDao.obtenerTodos() } returns flowOf(listOf(entityLocal))
        coEvery { mockDao.obtenerPorId("EXAM-2026-001") } returns entityLocal
        coEvery { mockDao.obtenerPendientesSincronizacion() } returns listOf(entityLocal)

        val repo = ExamRepository(mockDao, mockApi, mockFhir)

        // 1. Verificar emisión del Flow local
        val listaDominio = repo.examenes.first()
        assertEquals(1, listaDominio.size)
        assertEquals("EXAM-2026-001", listaDominio[0].id)
        assertNotNull(listaDominio[0].documentoAdjunto)

        // 2. Refrescar desde backend con éxito
        val dtoRemoto = ExamenRemoteDto(
            id = "EXAM-2026-001",
            pacienteRut = "14.821.903-2",
            pacienteNombre = "Mariana González",
            pacienteEdad = 42,
            tipoExamen = "Campimetría 24-2",
            protocolo = "SITA-Fast",
            sucursalBox = "Providencia Box 3",
            ojoEvaluado = "OD",
            documentoNombre = "informe.pdf",
            documentoTamano = "2.1 MB",
            documentoFormato = "PDF",
            observaciones = "Normal",
            estado = "Validado",
            fecha = "24/10/2026 10:30",
            firmaElectronica = "Dr. C. Mendoza"
        )
        coEvery { mockApi.listarExamenes(any(), any(), any()) } returns listOf(dtoRemoto)
        coEvery { mockDao.obtenerPendientesSincronizacion() } returns emptyList()

        val okRefresh = repo.refrescarDesdeBackend("MEDICO")
        assertTrue(okRefresh)
        assertFalse(repo.isOffline.value)
        assertEquals("Sincronizado", repo.estadoSincronizacion.value)

        // 3. Fallo del backend -> activa «Modo offline»
        coEvery { mockApi.listarExamenes(any(), any(), any()) } throws RuntimeException("Backend caído")
        coEvery { mockDao.obtenerPendientesSincronizacion() } returns listOf(entityLocal)

        val failRefresh = repo.refrescarDesdeBackend("MEDICO")
        assertFalse(failRefresh)
        assertTrue(repo.isOffline.value)
        assertTrue(repo.estadoSincronizacion.value.contains("Modo offline"))

        // 4. Crear y guardar examen cuando el backend falla (fallback Room con pendienteSincronizacion = true)
        val nuevoExamen = repo.crearExamenNuevo(
            paciente = repo.pacienteMariana,
            tipoExamen = repo.catalogoExamenes[0].first,
            protocolo = repo.catalogoExamenes[0].second,
            sucursal = repo.catalogoSucursales[0],
            ojo = OjoEvaluado.AMBOS,
            documento = DocumentoAdjunto("oct.pdf", "1.2 MB", "Ahora", "PDF"),
            observaciones = "Sin hallazgos"
        )
        coEvery { mockApi.crearExamen(any(), any()) } throws RuntimeException("Sin conexión")
        val guardadoOffline = repo.guardarExamen(nuevoExamen)
        assertTrue(guardadoOffline)
        assertTrue(repo.isOffline.value)

        // 5. Guardar examen cuando el backend responde OK
        coEvery { mockApi.crearExamen(any(), any()) } returns dtoRemoto
        coEvery { mockDao.obtenerPendientesSincronizacion() } returns emptyList()
        val guardadoOnline = repo.guardarExamen(nuevoExamen)
        assertTrue(guardadoOnline)
        assertFalse(repo.isOffline.value)

        // 6. Actualizar estado de examen (online y offline)
        coEvery { mockApi.actualizarEstadoExamen(any(), any(), any()) } returns dtoRemoto.copy(estado = "Entregado")
        assertTrue(repo.actualizarEstadoExamen("EXAM-2026-001", "Entregado", "OK", "Dr. X"))

        coEvery { mockApi.actualizarEstadoExamen(any(), any(), any()) } throws RuntimeException("Timeout")
        assertFalse(repo.actualizarEstadoExamen("EXAM-2026-001", "Observado", "Revisión", "Dr. X"))

        // 7. Sincronizar pendientes al reconectar
        coEvery { mockDao.obtenerPendientesSincronizacion() } returns listOf(entityLocal)
        coEvery { mockApi.crearExamen(any(), any()) } returns dtoRemoto
        coEvery { mockApi.listarExamenes(any(), any(), any()) } returns listOf(dtoRemoto)
        assertTrue(repo.sincronizarPendientes())
        assertEquals("Sincronizado", repo.estadoSincronizacion.value)

        // 8. Alternar modo offline demo
        assertTrue(repo.alternarModoOfflineDemo())
        assertTrue(repo.isOffline.value)
        repo.alternarModoOfflineDemo()

        // 9. Consumo de API Externa HAPI FHIR (éxito y fallback)
        coEvery { mockFhir.consultarReportesDiagnosticosFhir(any(), any()) } returns FhirBundleResponse(
            resourceType = "Bundle",
            total = 1,
            entry = listOf(
                FhirBundleEntry(
                    resource = FhirDiagnosticResource(
                        id = "9901",
                        status = "final",
                        effectiveDateTime = "2026-10-24T10:00:00Z",
                        code = FhirCodeableConcept(
                            text = "OCT Macular HL7",
                            coding = listOf(FhirCoding(code = "29274-8", display = "OCT"))
                        )
                    )
                )
            )
        )
        val fhirList = repo.cargarReferenciasFhirExternas()
        assertEquals(1, fhirList.size)
        assertEquals("FHIR-9901", fhirList[0].fhirId)

        coEvery { mockFhir.consultarReportesDiagnosticosFhir(any(), any()) } throws RuntimeException("Sin internet")
        val fhirFallback = repo.cargarReferenciasFhirExternas()
        assertEquals(3, fhirFallback.size)
    }

    @Test
    fun `AdminRepository persiste usuarios, configuracion y genera logs con SHA-256`() = runTest {
        val mockAdminDao = mockk<AdminDao>(relaxed = true)
        val mockApi = mockk<OftAppApiService>(relaxed = true)
        val configInicial = AdminRepository.crearConfigPredeterminada()
        coEvery { mockAdminDao.obtenerSystemConfig() } returns configInicial

        val adminRepo = AdminRepository(mockAdminDao, mockApi)

        coEvery { mockApi.listarUsuariosAdmin(any()) } returns emptyList()
        coEvery { mockApi.listarAuditoria() } returns emptyList()
        assertTrue(adminRepo.sincronizarConBackend())

        coEvery { mockApi.listarUsuariosAdmin(any()) } throws RuntimeException("Offline")
        assertFalse(adminRepo.sincronizarConBackend())

        val usuario = UsuarioAdminEntity(
            id = "USR-900",
            rol = "medico",
            nombreCompleto = "Dra. Sofía Valenzuela",
            rut = "15.842.109-K",
            email = "svalenzuela@oftalmolaser.cl",
            telefono = "+56911112222",
            exigirCambioClave = true,
            registroSis = "SIS-78942",
            especialidad = "Glaucoma",
            boxHabitual = "Box 1",
            cuentaActiva = true,
            permisoHistoricos = true,
            permisoExportacionMasiva = true,
            permisoAccesoRemoto = true,
            ultimaModificacion = "Ahora"
        )

        adminRepo.guardarUsuario(usuario, esNuevo = true)
        adminRepo.guardarUsuario(usuario.copy(rol = "tecnologo"), esNuevo = false)
        adminRepo.guardarUsuario(usuario.copy(rol = "administrativo"), esNuevo = false)
        adminRepo.guardarUsuario(usuario.copy(rol = "administrador"), esNuevo = false)
        adminRepo.guardarUsuario(usuario.copy(rol = "paciente"), esNuevo = false)
        adminRepo.suspenderUsuario(usuario)
        adminRepo.registrarRestablecimientoClave("Dra. Sofía", "15.842.109-K")

        // Cambio de umbral PIO genera log comparativo antes/después
        adminRepo.guardarConfiguracionSistema(configInicial.copy(umbralPio = 19))
        // Cambio de políticas generales sin cambiar PIO
        adminRepo.guardarConfiguracionSistema(configInicial.copy(twoFaObligatorio = false))

        val respaldo = adminRepo.generarRespaldoManual()
        assertTrue(respaldo.ultimoRespaldoFecha.contains("Respaldo Manual"))

        val restaurada = adminRepo.restablecerValoresPredeterminados()
        assertEquals(21, restaurada.umbralPio)

        val sucursales = AdminRepository.parsearSucursales(configInicial.sucursalesRaw)
        assertEquals(2, sucursales.size)
        val raw = AdminRepository.serializarSucursales(sucursales)
        assertTrue(raw.contains("Sucursal Central Andes"))

        coVerify(atLeast = 8) { mockAdminDao.insertarAuditLog(any()) }
    }

    @Test
    fun `HistorialRepositoryFake y Modelos de Sesion operan correctamente`() = runTest {
        val fakeRepo = HistorialRepositoryFake(api = null)
        val paciente = fakeRepo.obtenerPaciente(1L)
        assertNotNull(paciente)
        val eventos = fakeRepo.obtenerEventos(1L)
        assertTrue(eventos.isNotEmpty())

        val sesionPaciente = EstadoSesion(1L, Roles.PACIENTE, true)
        assertTrue(sesionPaciente.esPaciente())
        assertFalse(sesionPaciente.esAdmin())

        val sesionAdmin = EstadoSesion(1L, Roles.ADMINISTRADOR, true)
        assertTrue(sesionAdmin.esAdmin())
        assertFalse(sesionAdmin.esPaciente())

        val detalle = ExamenVisualDetalle()
        assertEquals("Validado", detalle.estado)
    }
}
