package com.example.oftapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.oftapp.data.AdminRepository
import com.example.oftapp.data.local.AuditLogEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminAuditUiState(
    val todosLosLogs: List<AuditLogEntity> = emptyList(),
    val logsFiltrados: List<AuditLogEntity> = emptyList(),
    val busqueda: String = "",
    val categoriaSeleccionada: String = "all", // "all", "usuarios", "parametros", "examenes", "fichas"
    val logSeleccionadoParaHash: AuditLogEntity? = null,
    val mostrarExportSheet: Boolean = false,
    val toastVisible: Boolean = false,
    val toastTitulo: String = "",
    val toastDescripcion: String = ""
) {
    val eventosHoyCount: Int
        get() = 28 + todosLosLogs.size
}

class AdminAuditViewModel(
    private val repository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminAuditUiState())
    val uiState: StateFlow<AdminAuditUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.sincronizarConBackend()
        }
        viewModelScope.launch {
            repository.auditLogsFlow.collect { lista ->
                _uiState.update { state ->
                    val filtrados = filtrarLogs(lista, state.categoriaSeleccionada, state.busqueda)
                    state.copy(
                        todosLosLogs = lista,
                        logsFiltrados = filtrados
                    )
                }
            }
        }
    }

    fun onBusquedaChange(query: String) {
        _uiState.update { state ->
            val filtrados = filtrarLogs(state.todosLosLogs, state.categoriaSeleccionada, query)
            state.copy(busqueda = query, logsFiltrados = filtrados)
        }
    }

    fun onCategoriaChange(categoria: String) {
        _uiState.update { state ->
            val filtrados = filtrarLogs(state.todosLosLogs, categoria, state.busqueda)
            state.copy(categoriaSeleccionada = categoria, logsFiltrados = filtrados)
        }
    }

    fun restablecerFiltros() {
        _uiState.update { state ->
            state.copy(
                busqueda = "",
                categoriaSeleccionada = "all",
                logsFiltrados = state.todosLosLogs
            )
        }
    }

    fun mostrarDetalleHash(log: AuditLogEntity?) {
        _uiState.update { it.copy(logSeleccionadoParaHash = log) }
    }

    fun setMostrarExportSheet(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarExportSheet = mostrar) }
    }

    fun confirmarExportacion(formato: String) {
        _uiState.update { it.copy(mostrarExportSheet = false) }
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    toastVisible = true,
                    toastTitulo = "$formato generado",
                    toastDescripcion = "Descargando informe forense con huella verificada."
                )
            }
            delay(3500)
            _uiState.update { it.copy(toastVisible = false) }
        }
    }

    private fun filtrarLogs(
        logs: List<AuditLogEntity>,
        categoria: String,
        query: String
    ): List<AuditLogEntity> {
        val q = query.trim().lowercase()
        return logs.filter { log ->
            val matchCat = (categoria == "all") || (log.categoria.equals(categoria, ignoreCase = true))
            val matchQuery = q.isEmpty() ||
                log.id.lowercase().contains(q) ||
                log.titulo.lowercase().contains(q) ||
                log.operador.lowercase().contains(q) ||
                log.afectadoValor.lowercase().contains(q) ||
                log.detalleTexto.lowercase().contains(q)
            matchCat && matchQuery
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AdminAuditViewModel(AdminRepository.getInstance(context)) as T
                }
            }
    }
}
