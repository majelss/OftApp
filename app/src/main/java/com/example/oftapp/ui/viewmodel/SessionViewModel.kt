package com.example.oftapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oftapp.data.local.UserPreferencesRepository
import com.example.oftapp.model.SesionUsuario
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel that exposes the persisted user session stored in DataStore.
 * It provides a hot [StateFlow] that always contains the latest [SesionUsuario]
 * or null when no session is present (e.g., after logout).
 */
class SessionViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val session: StateFlow<SesionUsuario?> = userPreferencesRepository.sesionFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
}
