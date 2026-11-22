package com.example.oftapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.oftapp.data.ExamRepository
import com.example.oftapp.data.remote.FhirReferenciaSintetica
import com.example.oftapp.ui.screens.examenes.ItemAtencion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de lista de atenciones y exámenes clínicos.
 * Consume los microservicios Spring Boot y la base de datos Room SQLite a través de ExamRepository,
 * además de exponer referencias clínicas de la API REST externa HAPI FHIR R4.
 */
class ExamListViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: ExamRepository =
        ExamRepository.getInstance(application.applicationContext)
) : AndroidViewModel(application) {

    val isOffline: StateFlow<Boolean> = repository.isOffline
    val pendientesCount: StateFlow<Int> = repository.pendientesCount
    val estadoSincronizacion: StateFlow<String> = repository.estadoSincronizacion

    val reportesFhirExternos: StateFlow<List<FhirReferenciaSintetica>> = repository.reportesFhirExternos
    val estadoFhirExterno: StateFlow<String> = repository.estadoFhirExterno

    /**
     * Flow reactivo de exámenes obtenidos de Room SQLite mapeados a ItemAtencion para la UI.
     */
    val atenciones: Flow<List<ItemAtencion>> = repository.examenes.map { lista ->
        lista.map { examen ->
            ItemAtencion(
                id = examen.id,
                paciente = examen.paciente.nombreCompleto,
                rut = examen.paciente.rut,
                examen = examen.tipoExamen,
                ojo = examen.ojoEvaluado.codigo,
                sucursal = examen.sucursalBox,
                fecha = examen.fecha,
                estado = examen.estado,
                pendienteSincronizacion = examen.pendienteSincronizacion
            )
        }
    }

    init {
        refrescarDatos()
    }

    fun refrescarDatos() {
        viewModelScope.launch {
            repository.refrescarDesdeBackend()
            repository.cargarReferenciasFhirExternas()
        }
    }

    fun sincronizarAhora() {
        viewModelScope.launch {
            repository.sincronizarPendientes()
        }
    }

    fun alternarModoOfflineDemo() {
        viewModelScope.launch {
            repository.alternarModoOfflineDemo()
        }
    }

    fun recargarFhirExterno() {
        viewModelScope.launch {
            repository.cargarReferenciasFhirExternas()
        }
    }
}
