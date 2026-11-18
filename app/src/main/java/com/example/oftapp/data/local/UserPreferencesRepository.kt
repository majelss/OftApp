package com.example.oftapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.oftapp.model.RolUsuario
import com.example.oftapp.model.SesionUsuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "oftapp_session")

/**
 * Persiste la sesión del usuario (rol, nombre, email, registro SIS) en DataStore.
 * Sobrevive al cierre del proceso y a rotaciones.
 *
 * Nota de seguridad: DataStore NO cifra. Para el token real de backend, usar
 * EncryptedSharedPreferences o cifrar el valor antes de escribirlo. Para el
 * rol y metadatos clínicos no sensibles, DataStore plano es suficiente.
 */
class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val ROL = stringPreferencesKey("rol")
        val EMAIL = stringPreferencesKey("email")
        val NOMBRE = stringPreferencesKey("nombre")
        val REGISTRO_SIS = stringPreferencesKey("registro_sis")
        val ESPECIALIDAD = stringPreferencesKey("especialidad")
        val TURNO_BOX = stringPreferencesKey("turno_box")
        val SUCURSAL = stringPreferencesKey("sucursal")
    }

    /** Flow reactivo con la sesión completa. null = no hay sesión activa. */
    val sesionFlow: Flow<SesionUsuario?> = context.dataStore.data.map { prefs ->
        val rolStr = prefs[Keys.ROL] ?: return@map null
        val rol = runCatching { RolUsuario.valueOf(rolStr) }.getOrNull() ?: return@map null
        SesionUsuario(
            nombreCompleto = prefs[Keys.NOMBRE] ?: "",
            rol = rol,
            especialidad = prefs[Keys.ESPECIALIDAD] ?: "",
            registroSIS = prefs[Keys.REGISTRO_SIS] ?: "",
            turnoBox = prefs[Keys.TURNO_BOX] ?: "",
            sucursal = prefs[Keys.SUCURSAL] ?: "",
            email = prefs[Keys.EMAIL] ?: ""
        )
    }

    /** Guarda la sesión completa. */
    suspend fun guardarSesion(sesion: SesionUsuario) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ROL] = sesion.rol.name
            prefs[Keys.EMAIL] = sesion.email
            prefs[Keys.NOMBRE] = sesion.nombreCompleto
            prefs[Keys.REGISTRO_SIS] = sesion.registroSIS
            prefs[Keys.ESPECIALIDAD] = sesion.especialidad
            prefs[Keys.TURNO_BOX] = sesion.turnoBox
            prefs[Keys.SUCURSAL] = sesion.sucursal
        }
    }

    /** Limpia la sesión (logout). */
    suspend fun cerrarSesion() {
        context.dataStore.edit { it.clear() }
    }

    /** Lectura puntual (para decisiones de ruteo sin observar). */
    suspend fun sesionActual(): SesionUsuario? = sesionFlow.first()
}

