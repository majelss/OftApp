package com.example.oftapp.data.model

data class Paciente(
    val id: Long,
    val nombre: String,
    val rut: String,
    val edad: Int,
    val prevision: String,
    val activo: Boolean,
    val codigoFicha: String
)
