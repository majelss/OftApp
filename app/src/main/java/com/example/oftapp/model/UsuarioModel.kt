package com.example.oftapp.model

object Roles {
    const val PACIENTE = "PACIENTE"
    const val MEDICO = "MEDICO"
    const val ADMINISTRATIVO = "ADMINISTRATIVO"
    const val TECNICO = "TECNICO"
    const val INVITADO = "INVITADO"
}

data class EstadoSesion(
    val usuarioId: Long? = null,
    val rol: String? = Roles.INVITADO,
    val estaAutenticado: Boolean = false
)

fun EstadoSesion.esPaciente(): Boolean = rol.equals(Roles.PACIENTE, ignoreCase = true)