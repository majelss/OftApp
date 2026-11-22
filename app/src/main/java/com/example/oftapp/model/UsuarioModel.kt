package com.example.oftapp.model

object Roles {
    const val PACIENTE = "PACIENTE"
    const val MEDICO = "MEDICO"
    const val ADMINISTRADOR = "ADMINISTRADOR"
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

fun EstadoSesion.esAdmin(): Boolean =
    rol.equals(Roles.ADMINISTRADOR, ignoreCase = true) ||
        rol.equals(Roles.ADMINISTRATIVO, ignoreCase = true)

fun EstadoSesion.esTecnico(): Boolean =
    rol.equals(Roles.TECNICO, ignoreCase = true) ||
        rol.equals("TECNOLOGO_MEDICO", ignoreCase = true)

fun EstadoSesion.esMedico(): Boolean =
    rol.equals(Roles.MEDICO, ignoreCase = true) ||
        (!esPaciente() && !esAdmin() && !esTecnico())
