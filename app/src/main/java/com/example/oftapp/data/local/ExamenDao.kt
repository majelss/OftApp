package com.example.oftapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones CRUD y reactivas sobre la tabla de exámenes oftalmológicos.
 */
@Dao
interface ExamenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarExamen(examen: ExamenEntity): Long

    @Query("SELECT * FROM examenes ORDER BY fecha DESC")
    fun obtenerTodos(): Flow<List<ExamenEntity>>

    @Query("SELECT * FROM examenes WHERE id = :id")
    suspend fun obtenerPorId(id: String): ExamenEntity?

    @Query("SELECT * FROM examenes WHERE pacienteRut = :rut ORDER BY fecha DESC")
    fun obtenerPorPaciente(rut: String): Flow<List<ExamenEntity>>

    @Query("DELETE FROM examenes WHERE id = :id")
    suspend fun eliminarPorId(id: String): Int
}
