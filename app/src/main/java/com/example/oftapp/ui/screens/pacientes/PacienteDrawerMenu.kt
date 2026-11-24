package com.example.oftapp.ui.screens.pacientes

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class PacienteDrawerItem(
    val id: String,
    val titulo: String,
    val icono: ImageVector
)

@Composable
fun PacienteDrawerMenu(
    opcionSeleccionada: String,
    onSeleccionar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        PacienteDrawerItem("inicio", "Inicio / Resumen", Icons.Default.Home),
        PacienteDrawerItem("examenes", "Mis Exámenes", Icons.Default.Description),
        PacienteDrawerItem("historial", "Historial Clínico", Icons.Default.History),
        PacienteDrawerItem("perfil", "Mi Perfil", Icons.Default.AccountCircle),
        PacienteDrawerItem("compartir", "Compartir Ficha (QR)", Icons.Default.QrCode)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "OFTAPP PACIENTE",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF006689),
            modifier = Modifier.padding(bottom = 4.dp, start = 12.dp)
        )
        Text(
            text = "Portal de Salud Visual",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp, start = 12.dp)
        )

        items.forEach { item ->
            val estaSeleccionado = opcionSeleccionada.equals(item.id, ignoreCase = true) ||
                    opcionSeleccionada.equals(item.titulo, ignoreCase = true)

            NavigationDrawerItem(
                label = { Text(text = item.titulo) },
                icon = { Icon(imageVector = item.icono, contentDescription = null) },
                selected = estaSeleccionado,
                onClick = { onSeleccionar(item.id) },
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
