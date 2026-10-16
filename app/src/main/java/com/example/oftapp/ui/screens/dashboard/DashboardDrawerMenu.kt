package com.example.oftapp.ui.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Contenido del menú lateral (drawer) del Dashboard.
 *
 * Antes se llamaba `MainMenuScreen`, pero ese nombre ya lo usa la pantalla
 * de menú principal del proyecto (ui.screens.dashboard.MainMenuScreen).
 * Se renombra para evitar ambigüedad en la integración.
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
        "Buscar Paciente",
        "Revisión OD / OI",
        "Recetas y Lentes"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "OFTAPP CLINICAL",
            style = MaterialTheme.typography.titleLarge,
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
    }
}
