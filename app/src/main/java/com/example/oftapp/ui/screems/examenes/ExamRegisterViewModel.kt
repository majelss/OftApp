package com.example.oftapp.ui.screems.examenes

import androidx.lifecycle.ViewModel
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
    val ojoEvaluado: OjoEvaluado = OjoEvaluado.DERECHO,
    val documentoAdjunto: DocumentoAdjunto? = DocumentoAdjunto(
        nombreArchivo = "resultado_oct_scan_01.pdf",
        tamano = "2.4 MB",
        tiempoSubida = "Subido hace 2 min"
    ),
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
    private val repository: ExamRepository = ExamRepository.getInstance()
) : ViewModel() {

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

    fun onObservacionesChanged(texto: String) {
        if (texto.length <= _uiState.value.maxCaracteresObservaciones) {
            _uiState.update { it.copy(observaciones = texto) }
        }
    }

    fun onAdjuntarArchivoSimulado() {
        val nuevosArchivos = listOf(
            DocumentoAdjunto("campimetria_campo_visual.pdf", "1.8 MB", "Subido hace un momento"),
            DocumentoAdjunto("topografia_pentacam_hr.dcm", "6.2 MB", "Subido hace un momento", "DICOM"),
            DocumentoAdjunto("retinografia_od_color.jpg", "3.1 MB", "Subido hace un momento", "JPG")
        )
        val archivoSeleccionado = nuevosArchivos.random()
        _uiState.update { it.copy(documentoAdjunto = archivoSeleccionado) }
    }

    fun onEliminarArchivo() {
        _uiState.update { it.copy(documentoAdjunto = null) }
    }

    fun onGuardarExamen() {
        val state = _uiState.value
        // Validación RF05
        if (state.paciente.rut.isBlank()) {
            _uiState.update { it.copy(mensajeError = "El paciente no es válido") }
            return
        }
        if (state.tipoExamen.isBlank()) {
            _uiState.update { it.copy(mensajeError = "Seleccione el tipo de examen") }
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
                observaciones = state.observaciones
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
