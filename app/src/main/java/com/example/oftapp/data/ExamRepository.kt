package com.example.oftapp.data

import android.content.Context
import com.example.oftapp.data.local.ExamenDao
import com.example.oftapp.data.local.ExamenEntity
import com.example.oftapp.data.local.OftAppDatabase
import com.example.oftapp.model.DocumentoAdjunto
import com.example.oftapp.model.ExamenOftalmologico
import com.example.oftapp.model.OjoEvaluado
import com.example.oftapp.model.Paciente
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Repositorio de datos clínicos de OftApp.
 * Persiste los exámenes en Room (SQLite) para garantizar trazabilidad y persistencia de datos.
 * Cumple con los requerimientos académicos DSY1105: arquitectura MVVM + Room.
 */
class ExamRepository(private val dao: ExamenDao) {

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

    /**
     * Flow reactivo de exámenes almacenados en Room SQLite.
     * Al registrar un nuevo examen, Room emite la lista actualizada automáticamente.
     */
    val examenes: Flow<List<ExamenOftalmologico>> = dao.obtenerTodos().map { lista ->
        lista.map { it.toDomain() }
    }

    /**
     * Guarda el examen en la base de datos SQLite con Room.
     */
    suspend fun guardarExamen(examen: ExamenOftalmologico): Boolean {
        dao.insertarExamen(examen.toEntity())
        return true
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
            firmaElectronica = "Dr. C. Mendoza"
        )
    }

    // --- Mappers de Dominio a Room Entity ---

    private fun ExamenEntity.toDomain(): ExamenOftalmologico {
        val ojo = OjoEvaluado.entries.firstOrNull { it.codigo == ojoEvaluado }
            ?: OjoEvaluado.DERECHO
        val doc = if (documentoNombre != null) {
            DocumentoAdjunto(
                nombreArchivo = documentoNombre,
                tamano = documentoTamano ?: "0 KB",
                tiempoSubida = "Guardado localmente",
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
            firmaElectronica = firmaElectronica
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
        firmaElectronica = firmaElectronica
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
