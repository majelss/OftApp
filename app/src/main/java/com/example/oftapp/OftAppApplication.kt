package com.example.oftapp

import android.app.Application
import com.example.oftapp.data.local.OftAppDatabase

/**
 * Clase Application de OftApp.
 * Inicializa la base de datos Room al arrancar la aplicación.
 */
class OftAppApplication : Application() {
    val database: OftAppDatabase by lazy {
        OftAppDatabase.getInstance(this)
    }
}
