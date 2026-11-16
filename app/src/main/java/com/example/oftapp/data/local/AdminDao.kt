package com.example.oftapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO para las operaciones de Administración (Usuarios, Auditoría y Configuración Global).
 */
@Dao
interface AdminDao {

    // --- Usuarios ---
    @Query("SELECT * FROM usuarios_admin ORDER BY id ASC")
    fun observarUsuarios(): Flow<List<UsuarioAdminEntity>>

    @Query("SELECT * FROM usuarios_admin WHERE id = :id LIMIT 1")
    suspend fun obtenerUsuarioPorId(id: String): UsuarioAdminEntity?

    @Query("SELECT COUNT(*) FROM usuarios_admin")
    suspend fun contarUsuarios(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarUsuario(usuario: UsuarioAdminEntity)

    // --- Auditoría ---
    @Query("SELECT * FROM audit_logs ORDER BY timestampMillis DESC")
    fun observarAuditLogs(): Flow<List<AuditLogEntity>>

    @Query("SELECT COUNT(*) FROM audit_logs")
    suspend fun contarAuditLogs(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAuditLog(log: AuditLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAuditLogs(logs: List<AuditLogEntity>)

    // --- Configuración del Sistema ---
    @Query("SELECT * FROM system_config WHERE id = 1 LIMIT 1")
    fun observarSystemConfig(): Flow<SystemConfigEntity?>

    @Query("SELECT * FROM system_config WHERE id = 1 LIMIT 1")
    suspend fun obtenerSystemConfig(): SystemConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarSystemConfig(config: SystemConfigEntity)
}
