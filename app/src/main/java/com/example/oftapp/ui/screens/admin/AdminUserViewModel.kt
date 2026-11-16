package com.example.oftapp.ui.screens.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.oftapp.data.AdminRepository
import com.example.oftapp.data.local.UsuarioAdminEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class AdminUserUiState(
    val modoEdicion: Boolean = true,
    val usuarioId: String = "USR-84920",
    val rolSeleccionado: String = "medico", // "paciente", "medico", "tecnologo", "administrativo", "administrador"
    val nombreCompleto: String = "Dra. Sofía Valenzuela Morales",
    val rut: String = "15.842.109-K",
    val email: String = "svalenzuela@oftalmolaser.cl",
    val telefono: String = "+56 9 8765 4321",
    val exigirCambioClave: Boolean = true,
    val registroSis: String = "SIS-78942-CL",
    val especialidad: String = "Polo Anterior y Catarata",
    val boxHabitual: String = "Box 04 - Torre Quirúrgica Oftalmo",
    val mencionTecnologo: String = "Oftalmología y Optometría",
    val equiposAutorizados: Set<String> = setOf("Pentacam HD", "OCT Macular", "Campímetro Humphrey"),
    val sucursalAdministrativo: String = "Casa Central - Av. Providencia",
    val ventanillaAdministrativo: String = "Recepción Piso 2 - Exámenes",
    val fichaPaciente: String = "#OF-8941",
    val previsionPaciente: String = "FONASA Tramo B/C/D",
    val modoBajaVision: Boolean = true,
    val cuentaActiva: Boolean = true,
    val permisoHistoricos: Boolean = true,
    val permisoExportacionMasiva: Boolean = false,
    val permisoAccesoRemoto: Boolean = true,
    val ultimaModificacion: String = "Modificado hace 2h",
    val usuariosRegistrados: List<UsuarioAdminEntity> = emptyList(),
    val mensajeError: String? = null,
    val toastVisible: Boolean = false,
    val toastMensaje: String = ""
)

class AdminUserViewModel(
    private val repository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUserUiState())
    val uiState: StateFlow<AdminUserUiState> = _uiState.asStateFlow()

    val especialidadesCatalogo = listOf(
        "Polo Anterior y Catarata",
        "Glaucoma y Presión Intraocular",
        "Retina y Vítreo",
        "Córnea y Cirugía Refractiva",
        "Oftalmología Pediátrica"
    )

    val sucursalesAdminCatalogo = listOf(
        "Casa Central - Av. Providencia",
        "Clínica Red Poniente",
        "Centro Ambulatorio Norte"
    )

    val previsionesCatalogo = listOf(
        "FONASA Tramo B/C/D",
        "Isapre Colmena",
        "Isapre Banmédica",
        "Isapre Consalud",
        "Particular / Convenio"
    )

    val equiposCatalogo = listOf(
        "Pentacam HD",
        "OCT Macular",
        "Campímetro Humphrey",
        "Angiógrafo Láser"
    )

    init {
        viewModelScope.launch {
            repository.usuariosFlow.collect { lista ->
                _uiState.update { it.copy(usuariosRegistrados = lista) }
                // Cargar primer usuario si estamos en modo edición inicial
                val actual = lista.firstOrNull { u -> u.id == _uiState.value.usuarioId } ?: lista.firstOrNull()
                if (actual != null && _uiState.value.modoEdicion) {
                    aplicarEntidadAEstado(actual, modoEdicion = true)
                }
            }
        }
    }

    private fun aplicarEntidadAEstado(u: UsuarioAdminEntity, modoEdicion: Boolean) {
        val equiposSet = u.equiposAutorizados
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()

        _uiState.update {
            it.copy(
                modoEdicion = modoEdicion,
                usuarioId = u.id,
                rolSeleccionado = u.rol,
                nombreCompleto = u.nombreCompleto,
                rut = u.rut,
                email = u.email,
                telefono = u.telefono,
                exigirCambioClave = u.exigirCambioClave,
                registroSis = u.registroSis,
                especialidad = u.especialidad,
                boxHabitual = u.boxHabitual,
                mencionTecnologo = u.mencionTecnologo,
                equiposAutorizados = equiposSet,
                sucursalAdministrativo = u.sucursalAdministrativo,
                ventanillaAdministrativo = u.ventanillaAdministrativo,
                fichaPaciente = u.fichaPaciente,
                previsionPaciente = u.previsionPaciente,
                modoBajaVision = u.modoBajaVision,
                cuentaActiva = u.cuentaActiva,
                permisoHistoricos = u.permisoHistoricos,
                permisoExportacionMasiva = u.permisoExportacionMasiva,
                permisoAccesoRemoto = u.permisoAccesoRemoto,
                ultimaModificacion = u.ultimaModificacion,
                mensajeError = null
            )
        }
    }

    fun seleccionarUsuarioExistente(usuario: UsuarioAdminEntity) {
        aplicarEntidadAEstado(usuario, modoEdicion = true)
        mostrarToast("Usuario ${usuario.nombreCompleto} cargado")
    }

    fun alternarModoCreacionEdicion() {
        val pasarAModoEdicion = !_uiState.value.modoEdicion
        if (pasarAModoEdicion) {
            val existente = _uiState.value.usuariosRegistrados.firstOrNull()
            if (existente != null) {
                aplicarEntidadAEstado(existente, modoEdicion = true)
            } else {
                _uiState.update { it.copy(modoEdicion = true, mensajeError = null) }
            }
            mostrarToast("Modo edición cargado")
        } else {
            val nuevoId = "USR-" + Random.nextInt(85000, 99999)
            _uiState.update {
                it.copy(
                    modoEdicion = false,
                    usuarioId = nuevoId,
                    nombreCompleto = "",
                    rut = "",
                    email = "",
                    telefono = "",
                    registroSis = "",
                    boxHabitual = "Box 01 - Consulta Oftalmo",
                    fichaPaciente = "#OF-" + Random.nextInt(1000, 9999),
                    cuentaActiva = true,
                    exigirCambioClave = true,
                    ultimaModificacion = "Registro en creación",
                    mensajeError = null
                )
            }
            mostrarToast("Formulario en blanco para nuevo usuario")
        }
    }

    fun onRolChange(nuevoRol: String) {
        _uiState.update { it.copy(rolSeleccionado = nuevoRol, mensajeError = null) }
    }

    fun onNombreChange(valor: String) {
        _uiState.update { it.copy(nombreCompleto = valor, mensajeError = null) }
    }

    fun onRutChange(valor: String) {
        _uiState.update { it.copy(rut = valor, mensajeError = null) }
    }

    fun onEmailChange(valor: String) {
        _uiState.update { it.copy(email = valor, mensajeError = null) }
    }

    fun onTelefonoChange(valor: String) {
        _uiState.update { it.copy(telefono = valor, mensajeError = null) }
    }

    fun onExigirCambioClaveChange(valor: Boolean) {
        _uiState.update { it.copy(exigirCambioClave = valor) }
    }

    fun onRegistroSisChange(valor: String) {
        _uiState.update { it.copy(registroSis = valor, mensajeError = null) }
    }

    fun onEspecialidadChange(valor: String) {
        _uiState.update { it.copy(especialidad = valor) }
    }

    fun onBoxHabitualChange(valor: String) {
        _uiState.update { it.copy(boxHabitual = valor) }
    }

    fun onToggleEquipoTecnologo(equipo: String) {
        _uiState.update { state ->
            val actual = state.equiposAutorizados.toMutableSet()
            if (actual.contains(equipo)) actual.remove(equipo) else actual.add(equipo)
            state.copy(equiposAutorizados = actual)
        }
    }

    fun onSucursalAdministrativoChange(valor: String) {
        _uiState.update { it.copy(sucursalAdministrativo = valor) }
    }

    fun onVentanillaChange(valor: String) {
        _uiState.update { it.copy(ventanillaAdministrativo = valor) }
    }

    fun onPrevisionChange(valor: String) {
        _uiState.update { it.copy(previsionPaciente = valor) }
    }

    fun onModoBajaVisionChange(valor: Boolean) {
        _uiState.update { it.copy(modoBajaVision = valor) }
    }

    fun onCuentaActivaChange(valor: Boolean) {
        _uiState.update { it.copy(cuentaActiva = valor) }
    }

    fun onPermisoHistoricosChange(valor: Boolean) {
        _uiState.update { it.copy(permisoHistoricos = valor) }
    }

    fun onPermisoExportacionChange(valor: Boolean) {
        _uiState.update { it.copy(permisoExportacionMasiva = valor) }
    }

    fun onPermisoAccesoRemotoChange(valor: Boolean) {
        _uiState.update { it.copy(permisoAccesoRemoto = valor) }
    }

    fun restablecerClave() {
        val s = _uiState.value
        val nombre = s.nombreCompleto.ifBlank { "Usuario ${s.usuarioId}" }
        viewModelScope.launch {
            repository.registrarRestablecimientoClave(nombre, s.rut.ifBlank { "S/R" })
            _uiState.update { it.copy(exigirCambioClave = true) }
            mostrarToast("Clave temporal enviada a ${s.email.ifBlank { "correo institucional" }}")
        }
    }

    fun suspenderUsuario() {
        val s = _uiState.value
        if (s.nombreCompleto.isBlank()) {
            _uiState.update { it.copy(mensajeError = "Ingrese los datos del usuario antes de suspender la cuenta.") }
            return
        }
        viewModelScope.launch {
            val entidad = construirEntidadDesdeEstado(s).copy(
                cuentaActiva = false,
                ultimaModificacion = "Suspendido recién"
            )
            repository.suspenderUsuario(entidad)
            _uiState.update {
                it.copy(
                    cuentaActiva = false,
                    ultimaModificacion = "Suspendido recién",
                    mensajeError = null
                )
            }
            mostrarToast("La cuenta ha sido suspendida preventivamente")
        }
    }

    fun guardarUsuario() {
        val s = _uiState.value

        // Validaciones obligatorias
        if (s.nombreCompleto.trim().length < 3) {
            _uiState.update { it.copy(mensajeError = "El campo 'Nombre y Apellidos' es obligatorio.") }
            return
        }
        if (!validarFormatoRutChileno(s.rut)) {
            _uiState.update {
                it.copy(mensajeError = "Ingrese un RUT chileno válido con puntos y guion (Ej: 15.842.109-K).")
            }
            return
        }
        if (s.email.isBlank() || !s.email.contains("@") || !s.email.contains(".")) {
            _uiState.update {
                it.copy(mensajeError = "Ingrese un Correo Electrónico Institucional válido.")
            }
            return
        }
        if (s.rolSeleccionado == "medico" && s.registroSis.isBlank()) {
            _uiState.update {
                it.copy(mensajeError = "El N° Registro Superintendencia de Salud / Colmed es obligatorio para Médicos.")
            }
            return
        }

        val esNuevo = !s.modoEdicion
        val entidad = construirEntidadDesdeEstado(s).copy(
            ultimaModificacion = "Sincronizado recién"
        )

        viewModelScope.launch {
            repository.guardarUsuario(entidad, esNuevo = esNuevo)
            _uiState.update {
                it.copy(
                    modoEdicion = true,
                    ultimaModificacion = "Sincronizado recién",
                    mensajeError = null
                )
            }
            mostrarToast(
                if (esNuevo) "Nuevo usuario creado y registrado en Auditoría"
                else "Permisos clínicos y usuario guardados correctamente"
            )
        }
    }

    private fun construirEntidadDesdeEstado(s: AdminUserUiState): UsuarioAdminEntity {
        return UsuarioAdminEntity(
            id = s.usuarioId,
            rol = s.rolSeleccionado,
            nombreCompleto = s.nombreCompleto.trim(),
            rut = s.rut.trim().uppercase(),
            email = s.email.trim(),
            telefono = s.telefono.trim(),
            exigirCambioClave = s.exigirCambioClave,
            registroSis = s.registroSis.trim(),
            especialidad = s.especialidad,
            boxHabitual = s.boxHabitual.trim(),
            mencionTecnologo = s.mencionTecnologo,
            equiposAutorizados = s.equiposAutorizados.joinToString(","),
            sucursalAdministrativo = s.sucursalAdministrativo,
            ventanillaAdministrativo = s.ventanillaAdministrativo,
            fichaPaciente = s.fichaPaciente,
            previsionPaciente = s.previsionPaciente,
            modoBajaVision = s.modoBajaVision,
            cuentaActiva = s.cuentaActiva,
            permisoHistoricos = s.permisoHistoricos,
            permisoExportacionMasiva = s.permisoExportacionMasiva,
            permisoAccesoRemoto = s.permisoAccesoRemoto,
            ultimaModificacion = s.ultimaModificacion
        )
    }

    private fun mostrarToast(mensaje: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(toastVisible = true, toastMensaje = mensaje) }
            delay(3000)
            _uiState.update { it.copy(toastVisible = false) }
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AdminUserViewModel(AdminRepository.getInstance(context)) as T
                }
            }
    }
}
