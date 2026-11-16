package com.example.oftapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para Usuarios gestionados desde el módulo Administrador.
 */
@Entity(tableName = "usuarios_admin")
data class UsuarioAdminEntity(
    @PrimaryKey
    val id: String,
    val rol: String, // "medico", "paciente", "tecnologo", "administrativo", "administrador"
    val nombreCompleto: String,
    val rut: String,
    val email: String,
    val telefono: String,
    val exigirCambioClave: Boolean,
    val registroSis: String,
    val especialidad: String,
    val boxHabitual: String,
    val mencionTecnologo: String = "Oftalmología y Optometría",
    val equiposAutorizados: String = "Pentacam HD,OCT Macular,Campímetro Humphrey",
    val sucursalAdministrativo: String = "Casa Central - Av. Providencia",
    val ventanillaAdministrativo: String = "Recepción Piso 2 - Exámenes",
    val fichaPaciente: String = "#OF-8941",
    val previsionPaciente: String = "FONASA Tramo B/C/D",
    val modoBajaVision: Boolean = true,
    val cuentaActiva: Boolean,
    val permisoHistoricos: Boolean,
    val permisoExportacionMasiva: Boolean,
    val permisoAccesoRemoto: Boolean,
    val ultimaModificacion: String
)
