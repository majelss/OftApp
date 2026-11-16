package com.example.oftapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para la Bitácora Criptográfica Inmutable (Auditoría del Sistema).
 */
@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey
    val id: String,               // Ej: "#LOG-882190"
    val titulo: String,           // Ej: "Modificación de Permisos"
    val categoria: String,        // "usuarios", "parametros", "examenes", "fichas"
    val estado: String,           // "Autorizado", "Creado", "Configuración", "Desactivado"
    val operador: String,         // Ej: "Prof. Dr. R. Valenzuela"
    val afectadoEtiqueta: String, // "Afectado:" o "Entidad:"
    val afectadoValor: String,    // Ej: "Claudia Vidal (Adm.)" o "Umbrales Tonometría"
    val detalleTitulo: String,    // Ej: "Acción y Privilegio Concedido"
    val detalleTexto: String,     // Descripción del cambio
    val valorPrevio: String? = null,
    val valorNuevo: String? = null,
    val fechaGrupo: String,       // "HOY, 24 MAYO 2024" o "AYER, 23 MAYO 2024"
    val hora: String,             // "11:42 hrs"
    val hashSha256: String,
    val timestampMillis: Long
)
