package com.example.oftapp.ui.screems.pacientes.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.oftapp.ui.theme.ClinicalOnSurfaceVariant
import com.example.oftapp.ui.theme.ClinicalPrimary

@Composable
fun TimelineFilterBar(
    registrosCount: Int,
    filtroSeleccionado: String,
    onFiltroChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val opciones = listOf("Todos los eventos", "Exámenes", "Observaciones", "Recetas")
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Línea de Tiempo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Surface(shape = MaterialTheme.shapes.small, color = ClinicalPrimary.copy(alpha = 0.08f), contentColor = ClinicalPrimary) {
                Text(" Registros", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
            }
        }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)) {
                Text(filtroSeleccionado, style = MaterialTheme.typography.labelMedium, color = ClinicalOnSurfaceVariant)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = ClinicalOnSurfaceVariant)
            }
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(text = { Text(opcion) }, onClick = { onFiltroChange(opcion); expanded = false })
                }
            }
        }
    }
}
