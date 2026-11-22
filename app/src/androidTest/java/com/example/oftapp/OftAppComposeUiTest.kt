package com.example.oftapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.oftapp.ui.screens.auth.LoginScreen
import com.example.oftapp.ui.theme.OftAppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Prueba instrumentada Compose UI Test (Anexo Técnico D - Evaluación Parcial 4 y 5):
 * "Al menos un Compose UI Test que introduzca datos inválidos, verifique mensajes
 * y compruebe una acción válida o la navegación".
 */
@RunWith(AndroidJUnit4::class)
class OftAppComposeUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_credencialesInvalidasMuestraError_yCredencialesValidasDisparaNavegacion() {
        var loginExitoso = false
        var rolRecibido = ""

        composeTestRule.setContent {
            OftAppTheme {
                LoginScreen(
                    onLoginSuccess = { _, rol ->
                        loginExitoso = true
                        rolRecibido = rol
                    }
                )
            }
        }

        // 1. Introducir datos inválidos (vaciar campo de correo electrónico) y presionar Iniciar Sesión
        composeTestRule.onNodeWithTag("input_email").performTextClearance()
        composeTestRule.onNodeWithTag("btn_login").performClick()

        // 2. Verificar que el mensaje de error es visible en la interfaz y no se navegó
        composeTestRule.onNodeWithTag("login_error_banner").assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "Credenciales incorrectas. Verifique su usuario y contraseña.",
            substring = true
        ).assertIsDisplayed()
        assertTrue(!loginExitoso)

        // 3. Introducir credenciales válidas y comprobar que la acción de navegación se ejecuta
        composeTestRule.onNodeWithTag("input_email")
            .performTextInput("dr.valenzuela@clinicaofta.cl")
        composeTestRule.onNodeWithTag("btn_login").performClick()

        assertTrue(loginExitoso)
        assertEquals("MEDICO", rolRecibido)
    }
}
