package com.example.oftapp.ui.screens.tm

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class TmDrawerItem(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
)

@Composable
fun TmDrawerMenu(
    opcionSeleccionada: String,
    onSeleccionar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        TmDrawerItem(TmRutas.INICIO, "Inicio / Box TM", Icons.Default.Dashboard),
        TmDrawerItem(TmRutas.REGISTRAR, "Registrar Examen", Icons.Default.AddCircle),
        TmDrawerItem(TmRutas.REGISTROS, "Mis Registros", Icons.AutoMirrored.Filled.Assignment),
        TmDrawerItem(TmRutas.PERFIL, "Mi Perfil & SIS", Icons.Default.AccountCircle)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "OFTAPP TECNÓLOGO",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF006385),
            modifier = Modifier.padding(bottom = 4.dp, start = 12.dp)
        )
        Text(
            text = "Captura Técnica y Telemetría",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp, start = 12.dp)
        )

        items.forEach { item ->
            val estaSeleccionado = opcionSeleccionada == item.ruta || opcionSeleccionada == item.titulo

            NavigationDrawerItem(
                label = { Text(text = item.titulo) },
                icon = { Icon(imageVector = item.icono, contentDescription = null) },
                selected = estaSeleccionado,
                onClick = { onSeleccionar(item.ruta) },
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
