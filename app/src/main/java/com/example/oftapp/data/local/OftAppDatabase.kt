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
 * Persiste los exámenes oftalmológicos en el dispositivo.
 * (DSY1105 - OftApp)
 */
@Database(
    entities = [ExamenEntity::class],
    version = 1,
    exportSchema = false
)
abstract class OftAppDatabase : RoomDatabase() {

    abstract fun examenDao(): ExamenDao

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
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Prepoblar con exámenes ficticios iniciales del caso clínico
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = database.examenDao()
                        dao.insertarExamen(
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
                        dao.insertarExamen(
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
                        dao.insertarExamen(
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
        }
    }
}
