package com.example.oftapp.viewmodel

import com.example.oftapp.data.AdminRepository
import com.example.oftapp.data.local.AuditLogEntity
import com.example.oftapp.data.local.SystemConfigEntity
import com.example.oftapp.data.local.UsuarioAdminEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class AdminViewModelsTest {

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
    fun `AdminUserViewModel valida campos, cambia modo y persiste usuario`() = runTest {
        val mockRepo = mockk<AdminRepository>(relaxed = true)
        val usuarioSemilla = UsuarioAdminEntity(
            id = "USR-84920",
            rol = "medico",
            nombreCompleto = "Dra. Sofía Valenzuela Morales",
            rut = "15.842.109-K",
            email = "svalenzuela@oftalmolaser.cl",
            telefono = "+56 9 8765 4321",
            exigirCambioClave = true,
            registroSis = "SIS-78942-CL",
            especialidad = "Polo Anterior y Catarata",
            boxHabitual = "Box 04",
            cuentaActiva = true,
            permisoHistoricos = true,
            permisoExportacionMasiva = false,
            permisoAccesoRemoto = true,
            ultimaModificacion = "Modificado hace 2h"
        )
        every { mockRepo.usuariosFlow } returns flowOf(listOf(usuarioSemilla))

        val vm = AdminUserViewModel(mockRepo)
        advanceUntilIdle()

        assertEquals("Dra. Sofía Valenzuela Morales", vm.uiState.value.nombreCompleto)
        assertTrue(vm.especialidadesCatalogo.isNotEmpty())
        assertTrue(vm.sucursalesAdminCatalogo.isNotEmpty())
        assertTrue(vm.previsionesCatalogo.isNotEmpty())
        assertTrue(vm.equiposCatalogo.isNotEmpty())

        // Cambiar a modo creación (formulario en blanco)
        vm.alternarModoCreacionEdicion()
        assertFalse(vm.uiState.value.modoEdicion)
        assertEquals("", vm.uiState.value.nombreCompleto)

        // Intentar suspender usuario vacío -> error
        vm.suspenderUsuario()
        assertNotNull(vm.uiState.value.mensajeError)

        // Intentar guardar sin nombre -> error
        vm.guardarUsuario()
        assertTrue(vm.uiState.value.mensajeError!!.contains("Nombre"))

        // Nombre válido pero RUT inválido -> error
        vm.onNombreChange("Dr. Camilo Arancibia")
        vm.onRutChange("12345")
        vm.guardarUsuario()
        assertTrue(vm.uiState.value.mensajeError!!.contains("RUT"))

        // RUT válido pero email inválido -> error
        vm.onRutChange("15.842.109-K")
        vm.onEmailChange("correo-invalido")
        vm.guardarUsuario()
        assertTrue(vm.uiState.value.mensajeError!!.contains("Correo"))

        // Email válido pero rol médico sin SIS -> error
        vm.onEmailChange("carancibia@oftalmolaser.cl")
        vm.onRolChange("medico")
        vm.onRegistroSisChange("")
        vm.guardarUsuario()
        assertTrue(vm.uiState.value.mensajeError!!.contains("Superintendencia"))

        // Completar SIS y demás atributos y guardar con éxito
        vm.onRegistroSisChange("SIS-49821")
        vm.onTelefonoChange("+56 9 1122 3344")
        vm.onExigirCambioClaveChange(false)
        vm.onEspecialidadChange("Glaucoma y Presión Intraocular")
        vm.onBoxHabitualChange("Box 02")
        vm.onToggleEquipoTecnologo("Angiógrafo Láser")
        vm.onToggleEquipoTecnologo("Pentacam HD")
        vm.onSucursalAdministrativoChange("Clínica Red Poniente")
        vm.onVentanillaChange("Ventanilla 1")
        vm.onPrevisionChange("Isapre Colmena")
        vm.onModoBajaVisionChange(false)
        vm.onCuentaActivaChange(true)
        vm.onPermisoHistoricosChange(false)
        vm.onPermisoExportacionChange(true)
        vm.onPermisoAccesoRemotoChange(false)

        vm.guardarUsuario()
        advanceUntilIdle()

        assertNull(vm.uiState.value.mensajeError)
        assertTrue(vm.uiState.value.modoEdicion)
        coVerify { mockRepo.guardarUsuario(any(), esNuevo = true) }

        // Restablecer clave y suspender usuario
        vm.restablecerClave()
        vm.suspenderUsuario()
        vm.seleccionarUsuarioExistente(usuarioSemilla)
        vm.alternarModoCreacionEdicion()
        vm.alternarModoCreacionEdicion()
        advanceUntilIdle()

        coVerify { mockRepo.registrarRestablecimientoClave(any(), any()) }
        coVerify { mockRepo.suspenderUsuario(any()) }
    }

    @Test
    fun `AdminAuditViewModel filtra por categoria, texto, muestra hash y exporta`() = runTest {
        val mockRepo = mockk<AdminRepository>(relaxed = true)
        val logs = listOf(
            AuditLogEntity(
                id = "#LOG-882190",
                titulo = "Modificación de Permisos",
                categoria = "usuarios",
                estado = "Autorizado",
                operador = "Prof. Dr. R. Valenzuela",
                afectadoEtiqueta = "Afectado:",
                afectadoValor = "Claudia Vidal",
                detalleTitulo = "Acción",
                detalleTexto = "Permiso concedido",
                fechaGrupo = "HOY",
                hora = "11:42 hrs",
                hashSha256 = "hash123",
                timestampMillis = 1000L
            ),
            AuditLogEntity(
                id = "#LOG-881903",
                titulo = "Modificación de Parámetro",
                categoria = "parametros",
                estado = "Configuración",
                operador = "Prof. Dr. R. Valenzuela",
                afectadoEtiqueta = "Entidad:",
                afectadoValor = "Umbrales Tonometría",
                detalleTitulo = "Comparativa",
                detalleTexto = "Ajuste PIO",
                valorPrevio = "22 mmHg",
                valorNuevo = "21 mmHg",
                fechaGrupo = "HOY",
                hora = "09:30 hrs",
                hashSha256 = "hash456",
                timestampMillis = 900L
            )
        )
        every { mockRepo.auditLogsFlow } returns flowOf(logs)

        val vm = AdminAuditViewModel(mockRepo)
        advanceUntilIdle()

        assertEquals(2, vm.uiState.value.logsFiltrados.size)
        assertEquals(30, vm.uiState.value.eventosHoyCount)

        vm.onCategoriaChange("parametros")
        assertEquals(1, vm.uiState.value.logsFiltrados.size)

        vm.onBusquedaChange("claudia")
        assertEquals(0, vm.uiState.value.logsFiltrados.size)

        vm.restablecerFiltros()
        assertEquals(2, vm.uiState.value.logsFiltrados.size)

        vm.mostrarDetalleHash(logs[0])
        assertEquals(logs[0], vm.uiState.value.logSeleccionadoParaHash)
        vm.mostrarDetalleHash(null)
        assertNull(vm.uiState.value.logSeleccionadoParaHash)

        vm.setMostrarExportSheet(true)
        assertTrue(vm.uiState.value.mostrarExportSheet)
        vm.confirmarExportacion("PDF Timbrado")
        assertFalse(vm.uiState.value.mostrarExportSheet)
        advanceUntilIdle()
    }

    @Test
    fun `AdminSystemConfigViewModel actualiza parametros, sucursales y respaldos`() = runTest {
        val mockRepo = mockk<AdminRepository>(relaxed = true)
        val configBase = AdminRepository.crearConfigPredeterminada()
        every { mockRepo.systemConfigFlow } returns flowOf(configBase)
        coEvery { mockRepo.generarRespaldoManual() } returns configBase.copy(ultimoRespaldoFecha = "Hoy, 12:00 hrs")
        coEvery { mockRepo.restablecerValoresPredeterminados() } returns configBase

        val vm = AdminSystemConfigViewModel(mockRepo)
        advanceUntilIdle()

        assertEquals("48h", vm.uiState.value.vigenciaCodigos)
        assertEquals(21, vm.uiState.value.umbralPio)
        assertEquals(2, vm.uiState.value.sucursales.size)

        vm.onVigenciaChange("24h")
        assertEquals("24h", vm.uiState.value.vigenciaCodigos)

        vm.onAumentarPio()
        assertEquals(22, vm.uiState.value.umbralPio)
        vm.onDisminuirPio()
        assertEquals(21, vm.uiState.value.umbralPio)

        vm.onCompresionDicomChange(false)
        vm.onTwoFaChange(false)
        vm.onBloqueoInactividadChange(false)
        vm.onCifradoExtremoChange(false)
        assertFalse(vm.uiState.value.compresionDicom)

        // Diálogo nueva sucursal
        vm.abrirDialogoNuevaSucursal()
        assertTrue(vm.uiState.value.mostrarDialogoSucursal)
        vm.guardarSucursalDialogo("Sucursal Sur", "Anexo", "Av. Sur 100", 5, "OCT")
        assertEquals(3, vm.uiState.value.sucursales.size)

        // Editar sucursal existente
        val primera = vm.uiState.value.sucursales.first()
        vm.abrirDialogoEditarSucursal(primera)
        vm.guardarSucursalDialogo("Sucursal Central Editada", "Principal", "Av. Central", 8, "Láser")
        assertEquals("Sucursal Central Editada", vm.uiState.value.sucursales.first().nombre)

        vm.abrirDialogoNuevaSucursal()
        vm.cerrarDialogoSucursal()
        assertFalse(vm.uiState.value.mostrarDialogoSucursal)

        vm.generarRespaldoManual()
        vm.restablecerValoresPredeterminados()
        vm.guardarTodosLosCambios()
        advanceUntilIdle()

        coVerify { mockRepo.generarRespaldoManual() }
        coVerify { mockRepo.restablecerValoresPredeterminados() }
        coVerify { mockRepo.guardarConfiguracionSistema(any()) }
    }
}
