package com.example.oftapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones CRUD, reactivas y de sincronización offline/online sobre la tabla de exámenes.
 */
@Dao
interface ExamenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarExamen(examen: ExamenEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarExamenes(examenes: List<ExamenEntity>)

    @Query("SELECT * FROM examenes ORDER BY fecha DESC")
    fun obtenerTodos(): Flow<List<ExamenEntity>>

    @Query("SELECT * FROM examenes WHERE id = :id")
    suspend fun obtenerPorId(id: String): ExamenEntity?

    @Query("SELECT * FROM examenes WHERE pacienteRut = :rut ORDER BY fecha DESC")
    fun obtenerPorPaciente(rut: String): Flow<List<ExamenEntity>>

    @Query("SELECT * FROM examenes WHERE pendienteSincronizacion = 1")
    suspend fun obtenerPendientesSincronizacion(): List<ExamenEntity>

    @Query("UPDATE examenes SET pendienteSincronizacion = 0 WHERE id = :id")
    suspend fun marcarComoSincronizado(id: String)

    @Query("DELETE FROM examenes WHERE id = :id")
    suspend fun eliminarPorId(id: String): Int
}
