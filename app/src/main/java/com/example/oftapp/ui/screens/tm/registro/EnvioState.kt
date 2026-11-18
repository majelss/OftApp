package com.example.oftapp.ui.screens.tm.registro

/**
 * Representa los diferentes estados del proceso de envío del examen
 * desde la pantalla de registro.
 */
sealed interface EnvioState {
    data object Idle : EnvioState
    data object Enviando : EnvioState
    data object Enviado : EnvioState
    data class Error(val mensaje: String) : EnvioState
}
