package com.example.oftapp.model

/**
 * Roles disponibles en el sistema oftálmico OftApp.
 */
enum class RolUsuario(
    val titulo: String,
    val descripcion: String,
    val emailDemo: String,
    val passwordDemo: String
) {
    TECNOLOGO_MEDICO(
        titulo = "Tecnólogo Médico",
        descripcion = "Captura técnica, digitalización y envío a revisión",
        emailDemo = "tm.araya@clinicaofta.cl",
        passwordDemo = "tm123456"
    ),
    MEDICO_OFTALMOLOGO(
        titulo = "Médico Oftalmólogo",
        descripcion = "Acceso diagnóstico completo (OD / OI) y validación",
        emailDemo = "dr.valenzuela@clinicaofta.cl",
        passwordDemo = "dr123456"
    ),
    ADMINISTRATIVO(
        titulo = "Administrativo",
        descripcion = "Gestión de citas, agenda y admisión de pacientes",
        emailDemo = "admin@clinicaofta.cl",
        passwordDemo = "admin123456"
    ),
    PACIENTE(
        titulo = "Paciente",
        descripcion = "Acceso a su historial, exámenes y resultados",
        emailDemo = "paciente@clinicaofta.cl",
        passwordDemo = "pac123456"
    );

    companion object {
        fun desdeEmail(email: String): RolUsuario {
            val e = email.trim().lowercase()
            return when {
                e.startsWith("tm.") || e.contains("tecnologo") || e.contains("araya") -> TECNOLOGO_MEDICO
                e.startsWith("dr.") || e.contains("medico") || e.contains("valenzuela") || e.contains("morales") -> MEDICO_OFTALMOLOGO
                e.startsWith("admin") -> ADMINISTRATIVO
                e.startsWith("paciente") || e.contains("pac") -> PACIENTE
                else -> TECNOLOGO_MEDICO
            }
        }
    }
}

/**
 * Estado de la sesión del usuario actual en la aplicación.
 */
data class SesionUsuario(
    val nombreCompleto: String = "T.M. Felipe Araya M.",
    val rol: RolUsuario = RolUsuario.TECNOLOGO_MEDICO,
    val especialidad: String = "Área de Oftalmología Diagnóstica",
    val registroSIS: String = "48201",
    val turnoBox: String = "Turno Mañana • Box 4 Diagnóstico",
    val sucursal: String = "Clínica Oftalmológica Andes",
    val email: String = "tm.araya@clinicaofta.cl"
)

/**
 * Singleton de sesión en memoria para OftApp.
 */
object SesionActual {
    var usuario: SesionUsuario = SesionUsuario()
        private set

    fun iniciarSesion(rol: RolUsuario, email: String = "") {
        usuario = when (rol) {
            RolUsuario.TECNOLOGO_MEDICO -> SesionUsuario(
                nombreCompleto = "T.M. Felipe Araya M.",
                rol = RolUsuario.TECNOLOGO_MEDICO,
                especialidad = "Área de Oftalmología Diagnóstica",
                registroSIS = "48201",
                turnoBox = "Turno Mañana • Box 4 Diagnóstico",
                sucursal = "Clínica Oftalmológica Andes",
                email = if (email.isNotBlank()) email else "tm.araya@clinicaofta.cl"
            )
            RolUsuario.MEDICO_OFTALMOLOGO -> SesionUsuario(
                nombreCompleto = "Dr. Carlos Morales",
                rol = RolUsuario.MEDICO_OFTALMOLOGO,
                especialidad = "Médico Oftalmólogo • Especialista en Glaucoma",
                registroSIS = "44921",
                turnoBox = "Consulta General • Box 1",
                sucursal = "Clínica Oftalmológica Andes",
                email = if (email.isNotBlank()) email else "dr.valenzuela@clinicaofta.cl"
            )
            RolUsuario.ADMINISTRATIVO -> SesionUsuario(
                nombreCompleto = "Adm. Patricia Salgado",
                rol = RolUsuario.ADMINISTRATIVO,
                especialidad = "Admisión y Coordinación Quirúrgica",
                registroSIS = "ADM-102",
                turnoBox = "Mostrador Central",
                sucursal = "Clínica Oftalmológica Andes",
                email = if (email.isNotBlank()) email else "admin@clinicaofta.cl"
            )
            RolUsuario.PACIENTE -> SesionUsuario(
                nombreCompleto = "Mariana González R.",
                rol = RolUsuario.PACIENTE,
                especialidad = "",
                registroSIS = "",
                turnoBox = "",
                sucursal = "Clínica Oftalmológica Andes",
                email = if (email.isNotBlank()) email else "paciente@clinicaofta.cl"
            )
        }
    }

    fun cerrarSesion() {
        usuario = SesionUsuario()
    }
}
