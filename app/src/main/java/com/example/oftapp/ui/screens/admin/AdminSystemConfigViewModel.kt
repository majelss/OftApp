package com.example.oftapp.ui.screens.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.oftapp.data.AdminRepository
import com.example.oftapp.data.SucursalClinica
import com.example.oftapp.data.local.SystemConfigEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class AdminSystemConfigUiState(
    val vigenciaCodigos: String = "48h", // "24h", "48h", "7d"
    val umbralPio: Int = 21,
    val compresionDicom: Boolean = true,
    val sucursales: List<SucursalClinica> = emptyList(),
    val twoFaObligatorio: Boolean = true,
    val bloqueoInactividad: Boolean = true,
    val cifradoExtremo: Boolean = true,
    val ultimoRespaldoFecha: String = "Hoy, 04:00 AM (Chile Continental)",
    val ultimoRespaldoTamano: String = "Tamaño: 4.82 GB • Almacenamiento Cifrado AWS S3 Santiago",
    val respaldoEnProgreso: Boolean = false,
    val respaldoCompletadoRecien: Boolean = false,
    val sucursalEnEdicion: SucursalClinica? = null,
    val mostrarDialogoSucursal: Boolean = false,
    val toastVisible: Boolean = false,
    val toastTitulo: String = "",
    val toastSubtitulo: String = ""
)

class AdminSystemConfigViewModel(
    private val repository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminSystemConfigUiState())
    val uiState: StateFlow<AdminSystemConfigUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.systemConfigFlow.collect { config ->
                if (config != null) {
                    aplicarEntidad(config)
                } else {
                    val inicial = repository.obtenerConfigActual()
                    aplicarEntidad(inicial)
                }
            }
        }
    }

    private fun aplicarEntidad(config: SystemConfigEntity) {
        val listaSucursales = AdminRepository.parsearSucursales(config.sucursalesRaw)
        _uiState.update {
            it.copy(
                vigenciaCodigos = config.vigenciaCodigos,
                umbralPio = config.umbralPio,
                compresionDicom = config.compresionDicom,
                sucursales = listaSucursales,
                twoFaObligatorio = config.twoFaObligatorio,
                bloqueoInactividad = config.bloqueoInactividad,
                cifradoExtremo = config.cifradoExtremo,
                ultimoRespaldoFecha = config.ultimoRespaldoFecha,
                ultimoRespaldoTamano = config.ultimoRespaldoTamano
            )
        }
    }

    fun onVigenciaChange(valor: String) {
        _uiState.update { it.copy(vigenciaCodigos = valor) }
    }

    fun onDisminuirPio() {
        _uiState.update { state ->
            val nuevo = (state.umbralPio - 1).coerceAtLeast(10)
            state.copy(umbralPio = nuevo)
        }
    }

    fun onAumentarPio() {
        _uiState.update { state ->
            val nuevo = (state.umbralPio + 1).coerceAtMost(45)
            state.copy(umbralPio = nuevo)
        }
    }

    fun onCompresionDicomChange(valor: Boolean) {
        _uiState.update { it.copy(compresionDicom = valor) }
    }

    fun onTwoFaChange(valor: Boolean) {
        _uiState.update { it.copy(twoFaObligatorio = valor) }
    }

    fun onBloqueoInactividadChange(valor: Boolean) {
        _uiState.update { it.copy(bloqueoInactividad = valor) }
    }

    fun onCifradoExtremoChange(valor: Boolean) {
        _uiState.update { it.copy(cifradoExtremo = valor) }
    }

    fun abrirDialogoNuevaSucursal() {
        _uiState.update {
            it.copy(
                sucursalEnEdicion = null,
                mostrarDialogoSucursal = true
            )
        }
    }

    fun abrirDialogoEditarSucursal(sucursal: SucursalClinica) {
        _uiState.update {
            it.copy(
                sucursalEnEdicion = sucursal,
                mostrarDialogoSucursal = true
            )
        }
    }

    fun cerrarDialogoSucursal() {
        _uiState.update {
            it.copy(
                sucursalEnEdicion = null,
                mostrarDialogoSucursal = false
            )
        }
    }

    fun guardarSucursalDialogo(
        nombre: String,
        tipoBadge: String,
        direccion: String,
        boxesActivos: Int,
        equipamiento: String
    ) {
        val actual = _uiState.value
        val existente = actual.sucursalEnEdicion
        val nuevaSucursal = SucursalClinica(
            id = existente?.id ?: ("SUC-" + Random.nextInt(100, 999)),
            nombre = nombre.trim().ifEmpty { "Sucursal Clínica" },
            tipoBadge = tipoBadge.trim().ifEmpty { "Anexo" },
            direccion = direccion.trim().ifEmpty { "Dirección por confirmar" },
            boxesActivos = boxesActivos.coerceIn(1, 30),
            equipamiento = equipamiento.trim().ifEmpty { "Tonometría + Refracción" }
        )

        val nuevaLista = if (existente != null) {
            actual.sucursales.map { if (it.id == existente.id) nuevaSucursal else it }
        } else {
            actual.sucursales + nuevaSucursal
        }

        _uiState.update {
            it.copy(
                sucursales = nuevaLista,
                sucursalEnEdicion = null,
                mostrarDialogoSucursal = false
            )
        }
        mostrarToast(
            titulo = if (existente != null) "Sucursal Actualizada" else "Nueva Sucursal Añadida",
            subtitulo = "${nuevaSucursal.nombre} (${nuevaSucursal.boxesActivos} boxes activos)"
        )
    }

    fun generarRespaldoManual() {
        if (_uiState.value.respaldoEnProgreso) return
        viewModelScope.launch {
            _uiState.update { it.copy(respaldoEnProgreso = true, respaldoCompletadoRecien = false) }
            delay(1400)
            val actualizada = repository.generarRespaldoManual()
            aplicarEntidad(actualizada)
            _uiState.update {
                it.copy(
                    respaldoEnProgreso = false,
                    respaldoCompletadoRecien = true
                )
            }
            mostrarToast(
                titulo = "¡Respaldo Exitoso! (4.83 GB)",
                subtitulo = "Volcado seguro sincronizado en AWS S3 Santiago."
            )
            delay(2500)
            _uiState.update { it.copy(respaldoCompletadoRecien = false) }
        }
    }

    fun restablecerValoresPredeterminados() {
        viewModelScope.launch {
            val porDefecto = repository.restablecerValoresPredeterminados()
            aplicarEntidad(porDefecto)
            mostrarToast(
                titulo = "Valores Predeterminados Restaurados",
                subtitulo = "Parámetros clínicos restablecidos según normativa."
            )
        }
    }

    fun guardarTodosLosCambios() {
        val s = _uiState.value
        val entidad = SystemConfigEntity(
            id = 1,
            vigenciaCodigos = s.vigenciaCodigos,
            umbralPio = s.umbralPio,
            compresionDicom = s.compresionDicom,
            sucursalesRaw = AdminRepository.serializarSucursales(s.sucursales),
            twoFaObligatorio = s.twoFaObligatorio,
            bloqueoInactividad = s.bloqueoInactividad,
            cifradoExtremo = s.cifradoExtremo,
            ultimoRespaldoFecha = s.ultimoRespaldoFecha,
            ultimoRespaldoTamano = s.ultimoRespaldoTamano
        )
        viewModelScope.launch {
            repository.guardarConfiguracionSistema(entidad)
            mostrarToast(
                titulo = "Configuración Actualizada",
                subtitulo = "Los parámetros clínicos han sido sincronizados con éxito."
            )
        }
    }

    private fun mostrarToast(titulo: String, subtitulo: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    toastVisible = true,
                    toastTitulo = titulo,
                    toastSubtitulo = subtitulo
                )
            }
            delay(3200)
            _uiState.update { it.copy(toastVisible = false) }
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AdminSystemConfigViewModel(AdminRepository.getInstance(context)) as T
                }
            }
    }
}
