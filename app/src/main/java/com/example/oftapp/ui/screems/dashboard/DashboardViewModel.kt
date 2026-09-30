package com.example.oftapp.ui.screems.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class EstadisticaDashboard(
    val titulo: String,
    val cantidad: Int,
    val detalle: String,
    val tipo: String
)

data class AtencionResumen(
    val iniciales: String,
    val nombrePaciente: String,
    val hora: String,
    val examen: String,
    val estado: String
)
//Datos referenciales a las imagenes de la app

class DashboardViewModel : ViewModel() {

    var estadisticas by mutableStateOf(
        listOf(
            EstadisticaDashboard(
                titulo = "Atenciones hoy",
                cantidad = 24,
                detalle = "↑ 15%",
                tipo = "atenciones"
            ),
            EstadisticaDashboard(
                titulo = "Exámenes pend.",
                cantidad = 7,
                detalle = "3 urg.",
                tipo = "pendientes"
            ),
            EstadisticaDashboard(
                titulo = "Completados",
                cantidad = 17,
                detalle = "Hoy",
                tipo = "completados"
            )
        )
    )
        private set

    var proximasAtenciones by mutableStateOf(
        listOf(
            AtencionResumen(
                iniciales = "MR",
                nombrePaciente = "Mariana Ramírez",
                hora = "09:45 AM",
                examen = "Tonometría de Aplanación",
                estado = "Urgente"
            ),
            AtencionResumen(
                iniciales = "CL",
                nombrePaciente = "Carlos Lavín",
                hora = "10:00 AM",
                examen = "Campo Visual Computarizado",
                estado = "En sala"
            ),
            AtencionResumen(
                iniciales = "ES",
                nombrePaciente = "Elena Sandoval",
                hora = "10:20 AM",
                examen = "Dilatación Pupilar (OD/OI)",
                estado = "Preparación"
            )
        )
    )
        private set
}