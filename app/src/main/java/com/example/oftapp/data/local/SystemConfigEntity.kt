package com.example.oftapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para la Configuración Global del Sistema (Módulo Clínico Central).
 */
@Entity(tableName = "system_config")
data class SystemConfigEntity(
    @PrimaryKey
    val id: Int = 1,
    val vigenciaCodigos: String,      // "24h", "48h", "7d"
    val umbralPio: Int,               // Ej: 21 (mmHg)
    val compresionDicom: Boolean,     // true
    val sucursalesRaw: String,        // Lista serializada separada por '||' y campos por '|'
    val twoFaObligatorio: Boolean,    // true
    val bloqueoInactividad: Boolean,  // true
    val cifradoExtremo: Boolean,      // true
    val ultimoRespaldoFecha: String,  // "Hoy, 04:00 AM (Chile Continental)"
    val ultimoRespaldoTamano: String  // "Tamaño: 4.82 GB • Almacenamiento Cifrado AWS S3 Santiago"
)
