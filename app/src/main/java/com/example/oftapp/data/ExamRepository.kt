package com.example.oftapp.data

import android.content.Context
import com.example.oftapp.data.local.ExamenDao
import com.example.oftapp.data.local.ExamenEntity
import com.example.oftapp.data.local.OftAppDatabase
import com.example.oftapp.data.remote.EstadoUpdateDto
import com.example.oftapp.data.remote.ExamenRemoteDto
import com.example.oftapp.data.remote.FhirExternalApiService
import com.example.oftapp.data.remote.FhirReferenciaSintetica
import com.example.oftapp.data.remote.OftAppApiService
import com.example.oftapp.data.remote.RetrofitClient
import com.example.oftapp.model.DocumentoAdjunto
import com.example.oftapp.model.ExamenOftalmologico
import com.example.oftapp.model.OjoEvaluado
import com.example.oftapp.model.Paciente
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Repositorio de datos clínicos de OftApp con arquitectura Offline-First:
 * - Consume microservicios propios Spring Boot mediante Retrofit (OftAppApiService).
 * - Persiste en Room (SQLite) como fuente de verdad local.
 * - Si el servidor falla, activa «Modo offline» y marca cambios con «Sincronización pendiente».
 * - Consume la API REST externa pública HAPI FHIR R4 (FhirExternalApiService) con datos sintéticos.
 */
class ExamRepository(
    private val dao: ExamenDao,
    private val api: OftAppApiService = RetrofitClient.oftAppApi,
    private val fhirApi: FhirExternalApiService = RetrofitClient.fhirExternalApi
) {

    // Catálogo de tipos de exámenes oftalmológicos
    val catalogoExamenes = listOf(
        "Campimetría Visual Computarizada 24-2" to "Protocolo estándar SITA-Fast activado",
        "Campimetría Visual Computarizada 30-2" to "Protocolo SITA-Standard para Glaucoma",
        "Tomografía de Coherencia Óptica (OCT)" to "Protocolo Macular 3D de alta resolución",
        "Topografía Corneal Pentacam" to "Mapeo de elevación anterior y posterior",
        "Tonometría de Aplanación (Goldmann)" to "Medición PIO con corrección paquimétrica",
        "Agudeza Visual y Refracción Subjetiva" to "Cartilla Snellen digital calibrada"
    )

    // Catálogo de sucursales y boxes de atención
    val catalogoSucursales = listOf(
        "Sucursal Providencia - Box 3 Oftalmo",
        "Sucursal Puerto Montt - Box 1 Consulta",
        "Sucursal Las Condes - Box 5 Diagnóstico",
        "Sucursal Viña del Mar - Box 2 Especialidades"
    )

    // Paciente por defecto para la ficha
    val pacienteMariana = Paciente(
        rut = "14.821.903-2",
        nombreCompleto = "Mariana González Riquelme",
        edad = 42,
        isValidado = true
    )

    // Estados reactivos de conectividad y sincronización (Anexo C)
    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    private val _pendientesCount = MutableStateFlow(0)
    val pendientesCount: StateFlow<Int> = _pendientesCount.asStateFlow()

    private val _estadoSincronizacion = MutableStateFlow("Sincronizado")
    val estadoSincronizacion: StateFlow<String> = _estadoSincronizacion.asStateFlow()

    // Estados reactivos para la API REST Externa (HAPI FHIR R4)
    private val _reportesFhirExternos = MutableStateFlow<List<FhirReferenciaSintetica>>(emptyList())
    val reportesFhirExternos: StateFlow<List<FhirReferenciaSintetica>> = _reportesFhirExternos.asStateFlow()

    private val _estadoFhirExterno = MutableStateFlow("Conectado a HAPI FHIR R4")
    val estadoFhirExterno: StateFlow<String> = _estadoFhirExterno.asStateFlow()

    /**
     * Flow reactivo de exámenes almacenados en Room SQLite.
     */
    val examenes: Flow<List<ExamenOftalmologico>> = dao.obtenerTodos().map { lista ->
        val pendientes = lista.count { it.pendienteSincronizacion }
        _pendientesCount.value = pendientes
        actualizarEtiquetaEstado(_isOffline.value, pendientes)
        lista.map { it.toDomain() }
    }

    /**
     * Sincroniza la lista de exámenes desde el microservicio Spring Boot hacia Room.
     * Si el backend no responde, activa «Modo offline» y mantiene los datos locales de Room.
     */
    suspend fun refrescarDesdeBackend(rol: String = "MEDICO"): Boolean {
        return try {
            val remotos = api.listarExamenes(rol = rol)
            val entidades = remotos.map { it.toEntity(pendienteSync = false) }
            if (entidades.isNotEmpty()) {
                dao.insertarExamenes(entidades)
            }
            val pendientesRestantes = dao.obtenerPendientesSincronizacion().size
            _isOffline.value = false
            _pendientesCount.value = pendientesRestantes
            actualizarEtiquetaEstado(offline = false, pendientes = pendientesRestantes)
            true
        } catch (_: Exception) {
            val pendientesLocales = dao.obtenerPendientesSincronizacion().size
            _isOffline.value = true
            _pendientesCount.value = pendientesLocales
            actualizarEtiquetaEstado(offline = true, pendientes = pendientesLocales)
            false
        }
    }

    /**
     * Guarda el examen intentando enviarlo primero al microservicio Spring Boot.
     * Si el servidor falla, lo persiste en Room con `pendienteSincronizacion = true`
     * y cambia el estado visual a «Modo offline» + «Sincronización pendiente».
     */
    suspend fun guardarExamen(examen: ExamenOftalmologico, rol: String = "MEDICO"): Boolean {
        return try {
            val enviado = api.crearExamen(rol = rol, examen = examen.toRemoteDto())
            dao.insertarExamen(enviado.toEntity(pendienteSync = false, localUri = examen.documentoAdjunto?.uri))
            val pendientes = dao.obtenerPendientesSincronizacion().size
            _isOffline.value = false
            _pendientesCount.value = pendientes
            actualizarEtiquetaEstado(offline = false, pendientes = pendientes)
            true
        } catch (_: Exception) {
            // Fallback Offline en Room con marca de sincronización pendiente
            dao.insertarExamen(examen.copy(pendienteSincronizacion = true).toEntity())
            val pendientes = dao.obtenerPendientesSincronizacion().size
            _isOffline.value = true
            _pendientesCount.value = pendientes
            actualizarEtiquetaEstado(offline = true, pendientes = pendientes)
            true
        }
    }

    /**
     * Actualiza el estado clínico de un examen en el backend y en Room (con fallback offline).
     */
    suspend fun actualizarEstadoExamen(
        id: String,
        nuevoEstado: String,
        observaciones: String? = null,
        firma: String? = null,
        rol: String = "MEDICO"
    ): Boolean {
        return try {
            val actualizado = api.actualizarEstadoExamen(
                rol = rol,
                id = id,
                request = EstadoUpdateDto(
                    nuevoEstado = nuevoEstado,
                    observaciones = observaciones,
                    firmaElectronica = firma
                )
            )
            dao.insertarExamen(actualizado.toEntity(pendienteSync = false))
            _isOffline.value = false
            actualizarEtiquetaEstado(offline = false, pendientes = dao.obtenerPendientesSincronizacion().size)
            true
        } catch (_: Exception) {
            val local = dao.obtenerPorId(id)
            if (local != null) {
                dao.insertarExamen(
                    local.copy(
                        estado = nuevoEstado,
                        observaciones = observaciones ?: local.observaciones,
                        firmaElectronica = firma ?: local.firmaElectronica,
                        pendienteSincronizacion = true
                    )
                )
            }
            val pendientes = dao.obtenerPendientesSincronizacion().size
            _isOffline.value = true
            _pendientesCount.value = pendientes
            actualizarEtiquetaEstado(offline = true, pendientes = pendientes)
            false
        }
    }

    /**
     * Envía al backend Spring Boot todos los exámenes guardados localmente mientras estaba en «Modo offline».
     */
    suspend fun sincronizarPendientes(rol: String = "MEDICO"): Boolean {
        _estadoSincronizacion.value = "Sincronizando..."
        return try {
            val pendientes = dao.obtenerPendientesSincronizacion()
            for (entidad in pendientes) {
                api.crearExamen(rol = rol, examen = entidad.toRemoteDto())
                dao.marcarComoSincronizado(entidad.id)
            }
            val remotos = api.listarExamenes(rol = rol)
            if (remotos.isNotEmpty()) {
                dao.insertarExamenes(remotos.map { it.toEntity(pendienteSync = false) })
            }
            _isOffline.value = false
            _pendientesCount.value = 0
            _estadoSincronizacion.value = "Sincronizado"
            true
        } catch (_: Exception) {
            val pendientes = dao.obtenerPendientesSincronizacion().size
            _isOffline.value = true
            _pendientesCount.value = pendientes
            actualizarEtiquetaEstado(offline = true, pendientes = pendientes)
            false
        }
    }

    /**
     * Simula o fuerza la desconexión/reconexión para demostrar el «Modo offline» en vivo durante la defensa.
     */
    suspend fun alternarModoOfflineDemo(): Boolean {
        if (!_isOffline.value) {
            _isOffline.value = true
            val pendientes = dao.obtenerPendientesSincronizacion().size
            _pendientesCount.value = pendientes
            actualizarEtiquetaEstado(offline = true, pendientes = pendientes)
            return true
        } else {
            return sincronizarPendientes()
        }
    }

    /**
     * Consume la API REST externa pública HAPI FHIR R4 (https://hapi.fhir.org/baseR4/DiagnosticReport)
     * mapeando sus entradas a referencias oftalmológicas sintéticas sin interferir con el backend propio.
     */
    suspend fun cargarReferenciasFhirExternas(): List<FhirReferenciaSintetica> {
        return try {
            val response = fhirApi.consultarReportesDiagnosticosFhir(count = 4)
            val catalogoSintetico = listOf(
                "LOINC 29274-8" to "OCT Macular Bilateral (FHIR R4)",
                "LOINC 79893-4" to "Campimetría Visual Automatizada 24-2",
                "LOINC 56844-4" to "Tonometría Intraocular Goldmann",
                "LOINC 79892-6" to "Topografía Corneal Scheimpflug"
            )
            val mapeados = response.entry.orEmpty().mapIndexed { idx, entry ->
                val res = entry.resource
                val parFallback = catalogoSintetico[idx % catalogoSintetico.size]
                val coding = res?.code?.coding?.firstOrNull()
                val codigo = coding?.code?.let { "LOINC $it" } ?: parFallback.first
                val display = res?.code?.text
                    ?: coding?.display
                    ?: parFallback.second
                FhirReferenciaSintetica(
                    fhirId = "FHIR-${res?.id ?: (1040 + idx)}",
                    codigoEstandar = codigo,
                    nombreEstudio = display,
                    estadoFhir = (res?.status ?: "final").uppercase(),
                    fechaReferencia = (res?.effectiveDateTime ?: res?.issued ?: "2026-10-24").take(10)
                )
            }.ifEmpty {
                generarReferenciasFhirSinteticasRespaldo()
            }
            _reportesFhirExternos.value = mapeados
            _estadoFhirExterno.value = "HAPI FHIR R4 Activo (${mapeados.size} recursos)"
            mapeados
        } catch (_: Exception) {
            val respaldo = generarReferenciasFhirSinteticasRespaldo()
            _reportesFhirExternos.value = respaldo
            _estadoFhirExterno.value = "HAPI FHIR R4 (Caché Sintético Local)"
            respaldo
        }
    }

    private fun generarReferenciasFhirSinteticasRespaldo(): List<FhirReferenciaSintetica> = listOf(
        FhirReferenciaSintetica(
            fhirId = "FHIR-48291",
            codigoEstandar = "LOINC 29274-8",
            nombreEstudio = "OCT Macular de Alta Resolución (HL7 FHIR)",
            estadoFhir = "FINAL",
            fechaReferencia = "2026-10-24"
        ),
        FhirReferenciaSintetica(
            fhirId = "FHIR-48292",
            codigoEstandar = "LOINC 79893-4",
            nombreEstudio = "Campimetría Computarizada 24-2 SITA",
            estadoFhir = "FINAL",
            fechaReferencia = "2026-10-23"
        ),
        FhirReferenciaSintetica(
            fhirId = "FHIR-48293",
            codigoEstandar = "LOINC 56844-4",
            nombreEstudio = "Curva Tensional Paquimétrica (PIO)",
            estadoFhir = "REGISTERED",
            fechaReferencia = "2026-10-22"
        )
    )

    private fun actualizarEtiquetaEstado(offline: Boolean, pendientes: Int) {
        _estadoSincronizacion.value = when {
            offline && pendientes > 0 -> "Modo offline • Sincronización pendiente ($pendientes)"
            offline -> "Modo offline"
            pendientes > 0 -> "Sincronización pendiente ($pendientes)"
            else -> "Sincronizado"
        }
    }

    fun crearExamenNuevo(
        paciente: Paciente,
        tipoExamen: String,
        protocolo: String,
        sucursal: String,
        ojo: OjoEvaluado,
        documento: DocumentoAdjunto?,
        observaciones: String,
        fecha: String? = null
    ): ExamenOftalmologico {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fechaFinal = if (!fecha.isNullOrBlank()) fecha else sdf.format(Date())
        return ExamenOftalmologico(
            id = "EXAM-" + UUID.randomUUID().toString().take(8).uppercase(),
            paciente = paciente,
            tipoExamen = tipoExamen,
            protocolo = protocolo,
            sucursalBox = sucursal,
            ojoEvaluado = ojo,
            documentoAdjunto = documento,
            observaciones = observaciones,
            estado = "Pendiente",
            fecha = fechaFinal,
            firmaElectronica = "Dr. C. Mendoza",
            pendienteSincronizacion = false
        )
    }

    // --- Mappers de Dominio, Room Entity y Remote DTO ---

    private fun ExamenEntity.toDomain(): ExamenOftalmologico {
        val ojo = OjoEvaluado.entries.firstOrNull { it.codigo == ojoEvaluado }
            ?: OjoEvaluado.DERECHO
        val doc = if (documentoNombre != null) {
            DocumentoAdjunto(
                nombreArchivo = documentoNombre,
                tamano = documentoTamano ?: "0 KB",
                tiempoSubida = if (pendienteSincronizacion) "Sincronización pendiente" else "Sincronizado",
                formato = documentoFormato ?: "PDF",
                uri = documentoUri
            )
        } else null

        return ExamenOftalmologico(
            id = id,
            paciente = Paciente(
                rut = pacienteRut,
                nombreCompleto = pacienteNombre,
                edad = pacienteEdad,
                isValidado = true
            ),
            tipoExamen = tipoExamen,
            protocolo = protocolo,
            sucursalBox = sucursalBox,
            ojoEvaluado = ojo,
            documentoAdjunto = doc,
            observaciones = observaciones,
            estado = estado,
            fecha = fecha,
            firmaElectronica = firmaElectronica,
            pendienteSincronizacion = pendienteSincronizacion
        )
    }

    private fun ExamenOftalmologico.toEntity(): ExamenEntity = ExamenEntity(
        id = id,
        pacienteRut = paciente.rut,
        pacienteNombre = paciente.nombreCompleto,
        pacienteEdad = paciente.edad,
        tipoExamen = tipoExamen,
        protocolo = protocolo,
        sucursalBox = sucursalBox,
        ojoEvaluado = ojoEvaluado.codigo,
        documentoNombre = documentoAdjunto?.nombreArchivo,
        documentoTamano = documentoAdjunto?.tamano,
        documentoFormato = documentoAdjunto?.formato,
        documentoUri = documentoAdjunto?.uri,
        observaciones = observaciones,
        estado = estado,
        fecha = fecha,
        firmaElectronica = firmaElectronica,
        pendienteSincronizacion = pendienteSincronizacion
    )

    private fun ExamenOftalmologico.toRemoteDto(): ExamenRemoteDto = ExamenRemoteDto(
        id = id,
        pacienteRut = paciente.rut,
        pacienteNombre = paciente.nombreCompleto,
        pacienteEdad = paciente.edad,
        tipoExamen = tipoExamen,
        protocolo = protocolo,
        sucursalBox = sucursalBox,
        ojoEvaluado = ojoEvaluado.codigo,
        documentoNombre = documentoAdjunto?.nombreArchivo,
        documentoTamano = documentoAdjunto?.tamano,
        documentoFormato = documentoAdjunto?.formato,
        observaciones = observaciones,
        estado = estado,
        fecha = fecha,
        firmaElectronica = firmaElectronica
    )

    private fun ExamenEntity.toRemoteDto(): ExamenRemoteDto = ExamenRemoteDto(
        id = id,
        pacienteRut = pacienteRut,
        pacienteNombre = pacienteNombre,
        pacienteEdad = pacienteEdad,
        tipoExamen = tipoExamen,
        protocolo = protocolo,
        sucursalBox = sucursalBox,
        ojoEvaluado = ojoEvaluado,
        documentoNombre = documentoNombre,
        documentoTamano = documentoTamano,
        documentoFormato = documentoFormato,
        observaciones = observaciones,
        estado = estado,
        fecha = fecha,
        firmaElectronica = firmaElectronica
    )

    private fun ExamenRemoteDto.toEntity(
        pendienteSync: Boolean,
        localUri: String? = null
    ): ExamenEntity = ExamenEntity(
        id = id,
        pacienteRut = pacienteRut,
        pacienteNombre = pacienteNombre,
        pacienteEdad = pacienteEdad,
        tipoExamen = tipoExamen,
        protocolo = protocolo ?: "Protocolo clínico estándar",
        sucursalBox = sucursalBox,
        ojoEvaluado = ojoEvaluado,
        documentoNombre = documentoNombre,
        documentoTamano = documentoTamano,
        documentoFormato = documentoFormato,
        documentoUri = localUri,
        observaciones = observaciones ?: "",
        estado = estado,
        fecha = fecha,
        firmaElectronica = firmaElectronica ?: "Dr. C. Mendoza",
        pendienteSincronizacion = pendienteSync
    )

    companion object {
        @Volatile
        private var instance: ExamRepository? = null

        fun getInstance(context: Context): ExamRepository {
            return instance ?: synchronized(this) {
                instance ?: ExamRepository(
                    OftAppDatabase.getInstance(context).examenDao()
                ).also { instance = it }
            }
        }
    }
}
