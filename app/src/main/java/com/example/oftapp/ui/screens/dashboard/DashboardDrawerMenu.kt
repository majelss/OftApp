package com.example.oftapp.ui.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Contenido del menú lateral (drawer) del Dashboard con opción de Cerrar Sesión.
 */
@Composable
fun DashboardDrawerMenu(
    opcionSeleccionada: String,
    onSeleccionar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listaDeOpciones = listOf(
        "Dashboard / Inicio",
        "Lista de Atenciones",
        "Registrar Examen",
        "Historial Clínico",
        "Patologías & Agudeza"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "OFTAPP CLINICAL",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF006689),
            modifier = Modifier.padding(bottom = 20.dp, start = 12.dp)
        )

        listaDeOpciones.forEach { opcion ->
            val estaSeleccionado = (opcionSeleccionada == opcion)

            NavigationDrawerItem(
                label = { Text(text = opcion) },
                selected = estaSeleccionado,
                onClick = { onSeleccionar(opcion) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFFE2E8F0))
        Spacer(modifier = Modifier.height(12.dp))

        NavigationDrawerItem(
            label = {
                Text(
                    text = "Cerrar Sesión",
                    color = Color(0xFFBA1A1A),
                    fontWeight = FontWeight.Bold
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Cerrar Sesión",
                    tint = Color(0xFFBA1A1A)
                )
            },
            selected = false,
            onClick = { onSeleccionar("login") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
