package com.example.oftapp.ui.screems.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


 // Componentes Composable que representa el menú de navegación principal de la aplicación.
 //@param opcionSeleccionada Nombre de la opción que está actualmente activa en la aplicación.
 //@param onSeleccionar Función (callback) que se ejecuta cuando el usuario presiona una opción del menú.
 //@param modifier Modificador opcional para personalizar el diseño externo del contenedor.

@Composable
fun MainMenuScreen(
    opcionSeleccionada: String,
    onSeleccionar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Lista con los nombres de las secciones principales del sistema
    val listaDeOpciones = listOf(
        "Dashboard / Inicio",
        "Lista de Atenciones",
        "Registrar Examen",
        "Historial Clínico",
        "Buscar Paciente",
        "Revisión OD / OI",
        "Recetas y Lentes"
    )

    // Contenedor vertical principal del menú
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Título del encabezado del menú
        Text(
            text = "OFTAPP CLINICAL",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 20.dp, start = 12.dp)
        )

        // Iteración sobre la lista de opciones para construir cada ítem seleccionable
        listaDeOpciones.forEach { opcion ->
            // Determina si el ítem actual coincide con la opción seleccionada globalmente
            val estaSeleccionado = (opcionSeleccionada == opcion)

            NavigationDrawerItem(
                label = { Text(text = opcion) },
                selected = estaSeleccionado,
                onClick = {
                    // Notifica al componente padre sobre el cambio de sección seleccionado
                    onSeleccionar(opcion)
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

@Composable
fun PantallaMenuPrincipal(
    opcionSeleccionada: String,
    onSeleccionar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    MainMenuScreen(
        opcionSeleccionada = opcionSeleccionada,
        onSeleccionar = onSeleccionar,
        modifier = modifier
    )
}

// Vista previa del menu principal
@Preview(showBackground = true)
@Composable
fun VistaPreviaPantallaMenuPrincipal() {
    // Estado local simulado para cambiar de sección en la vista previa
    var estadoSeleccionActual by remember { mutableStateOf("Dashboard / Inicio") }

    Surface(modifier = Modifier.fillMaxSize()) {
        MainMenuScreen(
            opcionSeleccionada = estadoSeleccionActual,
            onSeleccionar = { nuevaOpcion ->
                estadoSeleccionActual = nuevaOpcion
            }
        )
    }
}