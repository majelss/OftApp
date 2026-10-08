package com.example.oftapp.ui.screems.examenes

import androidx.lifecycle.ViewModel
import com.example.oftapp.model.ExamenVisualDetalle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ExamDetailUiState(
    val examen: ExamenVisualDetalle = ExamenVisualDetalle(),
    val zoomPercentage: Int = 100,
    val isFullscreenMap: Boolean = false,
    val showActualizarEstadoDialog: Boolean = false,
    val showReabrirDialog: Boolean = false,
    val snackbarMessage: String? = null
)

class ExamDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExamDetailUiState())
    val uiState: StateFlow<ExamDetailUiState> = _uiState.asStateFlow()

    fun onZoomIn() {
        _uiState.update {
            val next = (it.zoomPercentage + 25).coerceAtMost(200)
            it.copy(zoomPercentage = next)
        }
    }

    fun onZoomOut() {
        _uiState.update {
            val prev = (it.zoomPercentage - 25).coerceAtLeast(50)
            it.copy(zoomPercentage = prev)
        }
    }

    fun onToggleFullscreen() {
        _uiState.update { it.copy(isFullscreenMap = !it.isFullscreenMap) }
    }

    fun onExportPdf() {
        _uiState.update {
            it.copy(snackbarMessage = "Reporte PDF descargado: resultado_campimetria_${it.examen.idExamen.takeLast(4)}.pdf")
        }
    }

    fun onExportDicom() {
        _uiState.update {
            it.copy(snackbarMessage = "Cargando serie tomográfica DICOM (SOP Class: Ophthalmic Tomography)")
        }
    }

    fun onOpenHistorico() {
        _uiState.update {
            it.copy(snackbarMessage = "Cargando comparativa histórica de 3 evaluaciones previas")
        }
    }

    fun onOpenActualizarEstadoDialog() {
        _uiState.update { it.copy(showActualizarEstadoDialog = true) }
    }

    fun onCloseActualizarEstadoDialog() {
        _uiState.update { it.copy(showActualizarEstadoDialog = false) }
    }

    fun onSeleccionarNuevoEstado(nuevoEstado: String) {
        _uiState.update {
            it.copy(
                examen = it.examen.copy(estado = nuevoEstado),
                showActualizarEstadoDialog = false,
                snackbarMessage = "Estado actualizado a: $nuevoEstado con firma electrónica"
            )
        }
    }

    fun onOpenReabrirDialog() {
        _uiState.update { it.copy(showReabrirDialog = true) }
    }

    fun onCloseReabrirDialog() {
        _uiState.update { it.copy(showReabrirDialog = false) }
    }

    fun onConfirmarReapertura() {
        _uiState.update {
            it.copy(
                examen = it.examen.copy(estado = "Observado"),
                showReabrirDialog = false,
                snackbarMessage = "Examen reabierto para corrección por especialista"
            )
        }
    }

    fun onDismissSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
