package com.example.oftapp.ui.screens.examenes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.oftapp.data.ExamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * ViewModel para la pantalla de lista de atenciones y exámenes clínicos.
 * Consume la base de datos Room SQLite a través de ExamRepository.
 * Garantiza persistencia y reactividad en tiempo real (DSY1105 - OftApp).
 */
class ExamListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExamRepository =
        ExamRepository.getInstance(application.applicationContext)

    /**
     * Flow reactivo de exámenes obtenidos de Room SQLite mapeados a ItemAtencion para la UI.
     * Cualquier nuevo registro guardado en ExamRegisterScreen se reflejará aquí automáticamente.
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
                estado = examen.estado
            )
        }
    }
}
