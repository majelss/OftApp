package com.example.oftapp.ui.screens.examenes

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.oftapp.data.ExamRepository
import com.example.oftapp.model.DocumentoAdjunto
import com.example.oftapp.model.OjoEvaluado
import com.example.oftapp.model.Paciente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExamRegisterUiState(
    val paciente: Paciente = Paciente(
        rut = "14.821.903-2",
        nombreCompleto = "Mariana González Riquelme",
        edad = 42,
        isValidado = true
    ),
    val pacienteQuery: String = "14.821.903-2 - Mariana González",
    val tipoExamen: String = "Campimetría Visual Computarizada 24-2",
    val protocoloExamen: String = "Protocolo estándar SITA-Fast activado",
    val sucursalBox: String = "Sucursal Providencia - Box 3 Oftalmo",
    val fechaExamen: String = "",
    val ojoEvaluado: OjoEvaluado = OjoEvaluado.DERECHO,
    val documentoAdjunto: DocumentoAdjunto? = null,
    val observaciones: String = "",
    val maxCaracteresObservaciones: Int = 300,
    val firmaProfesional: String = "Dr. C. Mendoza",
    val isTipoExamenDropdownOpen: Boolean = false,
    val isSucursalDropdownOpen: Boolean = false,
    val isGuardando: Boolean = false,
    val guardadoExitoso: Boolean = false,
    val showDescartarDialog: Boolean = false,
    val mensajeError: String? = null
)

class ExamRegisterViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: ExamRepository =
        ExamRepository.getInstance(application.applicationContext)

    private val _uiState = MutableStateFlow(ExamRegisterUiState())
    val uiState: StateFlow<ExamRegisterUiState> = _uiState.asStateFlow()

    val catalogoExamenes = repository.catalogoExamenes
    val catalogoSucursales = repository.catalogoSucursales

    fun onOjoSelected(ojo: OjoEvaluado) {
        _uiState.update { it.copy(ojoEvaluado = ojo) }
    }

    fun onTipoExamenSelected(tipo: String, protocolo: String) {
        _uiState.update {
            it.copy(
                tipoExamen = tipo,
                protocoloExamen = protocolo,
                isTipoExamenDropdownOpen = false
            )
        }
    }

    fun setTipoExamenDropdownOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isTipoExamenDropdownOpen = isOpen) }
    }

    fun onSucursalSelected(sucursal: String) {
        _uiState.update {
            it.copy(
                sucursalBox = sucursal,
                isSucursalDropdownOpen = false
            )
        }
    }

    fun setSucursalDropdownOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isSucursalDropdownOpen = isOpen) }
    }

    fun onFechaExamenSeleccionada(fecha: String) {
        _uiState.update { it.copy(fechaExamen = fecha) }
    }

    fun onObservacionesChanged(texto: String) {
        if (texto.length <= _uiState.value.maxCaracteresObservaciones) {
            _uiState.update { it.copy(observaciones = texto) }
        }
    }

    /**
     * Integración con recurso del dispositivo: Acceso a documentos e imágenes
     * mediante Storage / Photo Picker / File Provider de Android.
     * Lee metadatos reales (nombre de archivo y tamaño) del URI.
     */
    fun onArchivoSeleccionado(uri: Uri, context: Context) {
        val contentResolver = context.contentResolver
        var nombreArchivo = "documento_examen_${System.currentTimeMillis()}"
        var tamanoArchivo = ""

        try {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (cursor.moveToFirst()) {
                    if (nameIndex >= 0) {
                        nombreArchivo = cursor.getString(nameIndex) ?: nombreArchivo
                    }
                    if (sizeIndex >= 0) {
                        val sizeBytes = cursor.getLong(sizeIndex)
                        tamanoArchivo = when {
                            sizeBytes >= 1024 * 1024 -> "%.1f MB".format(sizeBytes / (1024.0 * 1024.0))
                            sizeBytes >= 1024 -> "${sizeBytes / 1024} KB"
                            else -> "$sizeBytes B"
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Manejo de excepción seguro
        }

        val extension = nombreArchivo.substringAfterLast('.', "PDF").uppercase()

        _uiState.update {
            it.copy(
                documentoAdjunto = DocumentoAdjunto(
                    nombreArchivo = nombreArchivo,
                    tamano = if (tamanoArchivo.isNotBlank()) tamanoArchivo else "Archivo local",
                    tiempoSubida = "Seleccionado desde dispositivo",
                    formato = extension,
                    uri = uri.toString()
                )
            )
        }
    }

    fun onEliminarArchivo() {
        _uiState.update { it.copy(documentoAdjunto = null) }
    }

    fun onGuardarExamen() {
        val state = _uiState.value

        // Validación de campos obligatorios con mensajes claros
        if (state.paciente.rut.isBlank()) {
            _uiState.update { it.copy(mensajeError = "⚠️ Debe indicar un RUT de paciente válido.") }
            return
        }
        if (state.tipoExamen.isBlank()) {
            _uiState.update { it.copy(mensajeError = "⚠️ Seleccione el tipo de examen a realizar.") }
            return
        }
        if (state.sucursalBox.isBlank()) {
            _uiState.update { it.copy(mensajeError = "⚠️ Indique la sucursal y box clínico asignado.") }
            return
        }
        if (state.fechaExamen.isBlank()) {
            _uiState.update { it.copy(mensajeError = "⚠️ Seleccione la fecha y hora del examen con el calendario.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isGuardando = true, mensajeError = null) }
            val nuevo = repository.crearExamenNuevo(
                paciente = state.paciente,
                tipoExamen = state.tipoExamen,
                protocolo = state.protocoloExamen,
                sucursal = state.sucursalBox,
                ojo = state.ojoEvaluado,
                documento = state.documentoAdjunto,
                observaciones = state.observaciones,
                fecha = state.fechaExamen
            )
            repository.guardarExamen(nuevo)
            _uiState.update { it.copy(isGuardando = false, guardadoExitoso = true) }
        }
    }

    fun onSolicitarDescartar() {
        _uiState.update { it.copy(showDescartarDialog = true) }
    }

    fun onConfirmarDescartar() {
        _uiState.update { ExamRegisterUiState() }
    }

    fun onCancelarDescartar() {
        _uiState.update { it.copy(showDescartarDialog = false) }
    }

    fun onDismissGuardadoExitoso() {
        _uiState.update { it.copy(guardadoExitoso = false) }
    }

    fun onDismissError() {
        _uiState.update { it.copy(mensajeError = null) }
    }
}
