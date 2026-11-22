package com.example.oftapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Base de datos Room de OftApp (SQLite).
 * Persiste los exámenes oftalmológicos y los módulos de Administración (Usuarios, Auditoría y Sistema).
 * (DSY1105 - OftApp)
 */
@Database(
    entities = [
        ExamenEntity::class,
        UsuarioAdminEntity::class,
        AuditLogEntity::class,
        SystemConfigEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class OftAppDatabase : RoomDatabase() {

    abstract fun examenDao(): ExamenDao
    abstract fun adminDao(): AdminDao

    companion object {
        @Volatile
        private var INSTANCE: OftAppDatabase? = null

        fun getInstance(context: Context): OftAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OftAppDatabase::class.java,
                    "oftapp_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedInitialDataIfNeeded(database)
                    }
                }
            }
        }

        suspend fun seedInitialDataIfNeeded(database: OftAppDatabase) {
            val examenDao = database.examenDao()
            val adminDao = database.adminDao()

            // 1. Semilla de usuarios de administración
            if (adminDao.contarUsuarios() == 0) {
                adminDao.guardarUsuario(
                    UsuarioAdminEntity(
                        id = "USR-84920",
                        rol = "medico",
                        nombreCompleto = "Dra. Sofía Valenzuela Morales",
                        rut = "15.842.109-K",
                        email = "svalenzuela@oftalmolaser.cl",
                        telefono = "+56 9 8765 4321",
                        exigirCambioClave = true,
                        registroSis = "SIS-78942-CL",
                        especialidad = "Polo Anterior y Catarata",
                        boxHabitual = "Box 04 - Torre Quirúrgica Oftalmo",
                        cuentaActiva = true,
                        permisoHistoricos = true,
                        permisoExportacionMasiva = false,
                        permisoAccesoRemoto = true,
                        ultimaModificacion = "Modificado hace 2h"
                    )
                )
            }

            // 2. Semilla de bitácora criptográfica de auditoría
            if (adminDao.contarAuditLogs() == 0) {
                val baseTime = 1716550000000L
                adminDao.insertarAuditLogs(
                    listOf(
                        AuditLogEntity(
                            id = "#LOG-882190",
                            titulo = "Modificación de Permisos",
                            categoria = "usuarios",
                            estado = "Autorizado",
                            operador = "Prof. Dr. R. Valenzuela",
                            afectadoEtiqueta = "Afectado:",
                            afectadoValor = "Claudia Vidal (Adm.)",
                            detalleTitulo = "Acción y Privilegio Concedido",
                            detalleTexto = "Permiso concedido: Reemisión directa de duplicados clínicos con firma delegada.",
                            valorPrevio = null,
                            valorNuevo = null,
                            fechaGrupo = "HOY, 24 MAYO 2024",
                            hora = "11:42 hrs",
                            hashSha256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                            timestampMillis = baseTime + 4000L
                        ),
                        AuditLogEntity(
                            id = "#LOG-882144",
                            titulo = "Creación de Usuario Médico",
                            categoria = "usuarios",
                            estado = "Creado",
                            operador = "Prof. Dr. R. Valenzuela",
                            afectadoEtiqueta = "Afectado:",
                            afectadoValor = "Dr. Camilo Arancibia",
                            detalleTitulo = "Especialidad & Certificación",
                            detalleTexto = "Médico Oftalmólogo (Glaucoma). Registro SIS #49821 validado; firma electrónica avanzada vinculada.",
                            valorPrevio = null,
                            valorNuevo = null,
                            fechaGrupo = "HOY, 24 MAYO 2024",
                            hora = "10:15 hrs",
                            hashSha256 = "8f434346648f6b96df89dda901c5176b10a6d83961dd3c1ac88b59b2dc327aa4",
                            timestampMillis = baseTime + 3000L
                        ),
                        AuditLogEntity(
                            id = "#LOG-881903",
                            titulo = "Modificación de Parámetro",
                            categoria = "parametros",
                            estado = "Configuración",
                            operador = "Prof. Dr. R. Valenzuela",
                            afectadoEtiqueta = "Entidad:",
                            afectadoValor = "Umbrales Tonometría",
                            detalleTitulo = "Comparativa Forense",
                            detalleTexto = "Ajuste de umbral crítico de presión intraocular.",
                            valorPrevio = "22 mmHg",
                            valorNuevo = "21 mmHg",
                            fechaGrupo = "HOY, 24 MAYO 2024",
                            hora = "09:30 hrs",
                            hashSha256 = "2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae",
                            timestampMillis = baseTime + 2000L
                        ),
                        AuditLogEntity(
                            id = "#LOG-881200",
                            titulo = "Desactivación Temporal",
                            categoria = "usuarios",
                            estado = "Desactivado",
                            operador = "Prof. Dr. R. Valenzuela",
                            afectadoEtiqueta = "Afectado:",
                            afectadoValor = "T.M. Lucas Morales",
                            detalleTitulo = "Motivo Administrativo",
                            detalleTexto = "Suspensión cautelar de credenciales por período de reposo médico (Licencia #4102-B).",
                            valorPrevio = null,
                            valorNuevo = null,
                            fechaGrupo = "AYER, 23 MAYO 2024",
                            hora = "Ayer 17:00 hrs",
                            hashSha256 = "4e07408562bedb8b60ce05c1decfe3ad16b72230967de01f640b7e4729b49fce",
                            timestampMillis = baseTime + 1000L
                        )
                    )
                )
            }

            // 3. Semilla de configuración global del sistema
            if (adminDao.obtenerSystemConfig() == null) {
                val sucursalesDefault = listOf(
                    "SUC-1|Sucursal Central Andes|Principal|Av. Las Condes 10420, Las Condes|6|OCT + Campimetría + Láser",
                    "SUC-2|Sucursal Providencia|Anexo|Av. Pedro de Valdivia 145, Of. 602|4|Tonometría + Refracción"
                ).joinToString("||")

                adminDao.guardarSystemConfig(
                    SystemConfigEntity(
                        id = 1,
                        vigenciaCodigos = "48h",
                        umbralPio = 21,
                        compresionDicom = true,
                        sucursalesRaw = sucursalesDefault,
                        twoFaObligatorio = true,
                        bloqueoInactividad = true,
                        cifradoExtremo = true,
                        ultimoRespaldoFecha = "Hoy, 04:00 AM (Chile Continental)",
                        ultimoRespaldoTamano = "Tamaño: 4.82 GB • Almacenamiento Cifrado AWS S3 Santiago"
                    )
                )
            }

            // 4. Semilla de exámenes clínicos (si la base fue recreada)
            examenDao.insertarExamen(
                ExamenEntity(
                    id = "EXAM-2026-001",
                    pacienteRut = "14.821.903-2",
                    pacienteNombre = "Mariana González Riquelme",
                    pacienteEdad = 42,
                    tipoExamen = "Campimetría Visual Computarizada 24-2",
                    protocolo = "Protocolo estándar SITA-Fast activado",
                    sucursalBox = "Sucursal Providencia - Box 3 Oftalmo",
                    ojoEvaluado = "OD",
                    documentoNombre = "resultado_oct_scan_01.pdf",
                    documentoTamano = "2.4 MB",
                    documentoFormato = "PDF",
                    documentoUri = null,
                    observaciones = "Sin hallazgos patológicos en cuadrante nasal superior.",
                    estado = "Validado",
                    fecha = "24/10/2026 10:30",
                    firmaElectronica = "Dr. C. Mendoza"
                )
            )
            examenDao.insertarExamen(
                ExamenEntity(
                    id = "EXAM-2026-002",
                    pacienteRut = "18.332.194-K",
                    pacienteNombre = "Carlos Venegas Soto",
                    pacienteEdad = 38,
                    tipoExamen = "Tomografía de Coherencia Óptica (OCT)",
                    protocolo = "Protocolo Macular 3D de alta resolución",
                    sucursalBox = "Sucursal Puerto Montt - Box 1 Consulta",
                    ojoEvaluado = "AO",
                    documentoNombre = "oct_macular_hd.pdf",
                    documentoTamano = "3.8 MB",
                    documentoFormato = "PDF",
                    documentoUri = null,
                    observaciones = "Grosor foveal central dentro de límites normales bilaterales.",
                    estado = "Pendiente",
                    fecha = "24/10/2026 09:15",
                    firmaElectronica = "Dr. C. Mendoza"
                )
            )
            examenDao.insertarExamen(
                ExamenEntity(
                    id = "EXAM-2026-003",
                    pacienteRut = "12.784.551-8",
                    pacienteNombre = "Beatriz Morales Lara",
                    pacienteEdad = 51,
                    tipoExamen = "Topografía Corneal Pentacam",
                    protocolo = "Mapeo de elevación anterior y posterior",
                    sucursalBox = "Sucursal Las Condes - Box 5 Diagnóstico",
                    ojoEvaluado = "OI",
                    documentoNombre = "topografia_pentacam.pdf",
                    documentoTamano = "4.1 MB",
                    documentoFormato = "PDF",
                    documentoUri = null,
                    observaciones = "Astigmatismo miópico regular con la regla en ojo izquierdo.",
                    estado = "Observado",
                    fecha = "23/10/2026 16:45",
                    firmaElectronica = "Dr. C. Mendoza"
                )
            )
        }
    }
}
