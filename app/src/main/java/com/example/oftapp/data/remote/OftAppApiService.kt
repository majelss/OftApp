package com.example.oftapp.data.remote

import com.example.oftapp.data.local.AuditLogEntity
import com.example.oftapp.data.local.UsuarioAdminEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class LoginApiRequest(
    val email: String,
    val password: String,
    val rol: String
)

data class LoginApiResponse(
    val autenticado: Boolean,
    val usuarioId: Long?,
    val nombre: String?,
    val email: String?,
    val rol: String?,
    val token: String?,
    val mensaje: String?
)

data class PacienteDto(
    val id: Long? = null,
    val rut: String,
    val nombreCompleto: String,
    val edad: Int,
    val prevision: String,
    val activo: Boolean,
    val codigoFicha: String
)

data class ExamenRemoteDto(
    val id: String,
    val pacienteRut: String,
    val pacienteNombre: String,
    val pacienteEdad: Int,
    val tipoExamen: String,
    val protocolo: String?,
    val sucursalBox: String,
    val ojoEvaluado: String,
    val documentoNombre: String?,
    val documentoTamano: String?,
    val documentoFormato: String?,
    val observaciones: String?,
    val estado: String,
    val fecha: String,
    val firmaElectronica: String?
)

data class EstadoUpdateDto(
    val nuevoEstado: String,
    val observaciones: String? = null,
    val firmaElectronica: String? = null
)

/**
 * Interfaz Retrofit que consume los microservicios propios Spring Boot (/api/v1).
 */
interface OftAppApiService {

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginApiRequest): LoginApiResponse

    @GET("api/v1/pacientes")
    suspend fun listarPacientes(
        @Header("X-User-Role") rol: String = "MEDICO"
    ): List<PacienteDto>

    @GET("api/v1/pacientes/{id}")
    suspend fun obtenerPacientePorId(
        @Path("id") id: Long
    ): PacienteDto

    @POST("api/v1/pacientes")
    suspend fun crearPaciente(
        @Header("X-User-Role") rol: String = "MEDICO",
        @Body paciente: PacienteDto
    ): PacienteDto

    @GET("api/v1/examenes")
    suspend fun listarExamenes(
        @Header("X-User-Role") rol: String = "MEDICO",
        @Query("pacienteRut") pacienteRut: String? = null,
        @Query("estado") estado: String? = null
    ): List<ExamenRemoteDto>

    @POST("api/v1/examenes")
    suspend fun crearExamen(
        @Header("X-User-Role") rol: String = "MEDICO",
        @Body examen: ExamenRemoteDto
    ): ExamenRemoteDto

    @PUT("api/v1/examenes/{id}")
    suspend fun actualizarExamen(
        @Header("X-User-Role") rol: String = "MEDICO",
        @Path("id") id: String,
        @Body examen: ExamenRemoteDto
    ): ExamenRemoteDto

    @PATCH("api/v1/examenes/{id}/estado")
    suspend fun actualizarEstadoExamen(
        @Header("X-User-Role") rol: String = "MEDICO",
        @Path("id") id: String,
        @Body request: EstadoUpdateDto
    ): ExamenRemoteDto

    @DELETE("api/v1/examenes/{id}")
    suspend fun eliminarExamen(
        @Header("X-User-Role") rol: String = "ADMINISTRADOR",
        @Path("id") id: String
    )

    @GET("api/v1/admin/usuarios")
    suspend fun listarUsuariosAdmin(
        @Header("X-User-Role") rol: String = "ADMINISTRADOR"
    ): List<UsuarioAdminEntity>

    @POST("api/v1/admin/usuarios")
    suspend fun guardarUsuarioAdmin(
        @Header("X-User-Role") rol: String = "ADMINISTRADOR",
        @Body usuario: UsuarioAdminEntity
    ): UsuarioAdminEntity

    @GET("api/v1/admin/auditoria")
    suspend fun listarAuditoria(): List<AuditLogEntity>

    @POST("api/v1/admin/auditoria")
    suspend fun registrarAuditoria(@Body log: AuditLogEntity): AuditLogEntity
}
