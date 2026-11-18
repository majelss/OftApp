package com.example.oftapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room que persiste un examen oftalmológico en SQLite.
 * Mapea el modelo de dominio ExamenOftalmologico para almacenamiento local persistente.
 * (DSY1105 - OftApp)
 */
@Entity(tableName = "examenes")
data class ExamenEntity(
    @PrimaryKey val id: String,
    val pacienteRut: String,
    val pacienteNombre: String,
    val pacienteEdad: Int,
    val tipoExamen: String,
    val protocolo: String,
    val sucursalBox: String,
    val ojoEvaluado: String,          // OjoEvaluado.codigo (OD, OI, AO)
    val documentoNombre: String?,
    val documentoTamano: String?,
    val documentoFormato: String?,
    val documentoUri: String?,        // URI real del archivo seleccionado
    val observaciones: String,
    val estado: String,
    val fecha: String,
    val firmaElectronica: String
)
