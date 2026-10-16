package com.example.oftapp.data

import com.example.oftapp.model.DocumentoAdjunto
import com.example.oftapp.model.ExamenOftalmologico
import com.example.oftapp.model.OjoEvaluado
import com.example.oftapp.model.Paciente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Repositorio de datos clínicos simulados para OftApp.
 * Cumple con los requerimientos académicos DSY1105 de desacoplamiento MVVM y datos ficticios.
 */
class ExamRepository {

    // Paciente por defecto de la pantalla
    val pacienteMariana = Paciente(
        rut = "14.821.903-2",
        nombreCompleto = "Mariana González Riquelme",
        edad = 42,
        isValidado = true
    )

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

    // Lista en memoria de exámenes registrados
    private val _examenes = MutableStateFlow<List<ExamenOftalmologico>>(
        listOf(
            ExamenOftalmologico(
                id = "EXAM-2026-001",
                paciente = pacienteMariana,
                tipoExamen = "Campimetría Visual Computarizada 24-2",
                protocolo = "Protocolo estándar SITA-Fast activado",
                sucursalBox = "Sucursal Providencia - Box 3 Oftalmo",
                ojoEvaluado = OjoEvaluado.DERECHO,
                documentoAdjunto = DocumentoAdjunto(
                    nombreArchivo = "resultado_oct_scan_01.pdf",
                    tamano = "2.4 MB",
                    tiempoSubida = "Subido hace 2 min"
                ),
                observaciones = "Sin hallazgos patológicos en cuadrante nasal superior.",
                estado = "Validado",
                fecha = "05/10/2026 09:41",
                firmaElectronica = "Dr. C. Mendoza"
            )
        )
    )
    val examenes: StateFlow<List<ExamenOftalmologico>> = _examenes.asStateFlow()

    fun guardarExamen(examen: ExamenOftalmologico): Boolean {
        _examenes.value = listOf(examen) + _examenes.value
        return true
    }

    fun crearExamenNuevo(
        paciente: Paciente,
        tipoExamen: String,
        protocolo: String,
        sucursal: String,
        ojo: OjoEvaluado,
        documento: DocumentoAdjunto?,
        observaciones: String
    ): ExamenOftalmologico {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
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
            fecha = sdf.format(Date()),
            firmaElectronica = "Dr. C. Mendoza"
        )
    }

    companion object {
        @Volatile
        private var instance: ExamRepository? = null

        fun getInstance(): ExamRepository {
            return instance ?: synchronized(this) {
                instance ?: ExamRepository().also { instance = it }
            }
        }
    }
}
