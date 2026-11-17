package com.example.oftapp.navigation


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class AppNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appIniciaEnPantallaDeLogin() {
        composeTestRule.setContent {
            AppNavigation()
        }

        // Verifica que la pantalla inicial sea el Login
        composeTestRule.onNodeWithText("Iniciar Sesión", substring = true, ignoreCase = true)
            .assertIsDisplayed()
    }

    @Test
    fun loginComoPacienteNavegaAlDashboardPaciente() {
        // 1. Verificamos que estamos en la pantalla de Login
        composeTestRule.onNodeWithText("OftApp").assertIsDisplayed()

        // 2. Abrimos el selector de rol (por defecto dice "Médico Oftalmólogo")
        composeTestRule.onNodeWithText("Médico Oftalmólogo")
            .performScrollTo()
            .performClick()

        // 3. Seleccionamos "Paciente" en el menú desplegable
        composeTestRule.onNodeWithText("Paciente")
            .performClick()

        // 4. Pulsamos el botón "Iniciar Sesión"
        composeTestRule.onNodeWithText("Iniciar Sesión")
            .performScrollTo()
            .performClick()

        // 5. Verificamos que navegó a la pantalla de inicio del Paciente
        composeTestRule.onNodeWithText("Mis Recetas", substring = true, ignoreCase = true)
            .assertIsDisplayed()
    }
}