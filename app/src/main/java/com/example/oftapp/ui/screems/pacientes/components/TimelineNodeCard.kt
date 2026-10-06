package com.example.oftapp.ui.screems.pacientes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.oftapp.ui.theme.*

@Composable
fun TimelineNodeCard(
    fecha: String,
    etiquetaRelativa: String,
    titulo: String,
    subtitulo: String,
    profesional: String,
    especialidad: String,
    estadoLabel: String? = null,
    tieneBotonAccion: Boolean = false,
    textoBotonAccion: String = "Ver Informe Examen",
    onBotonAccionClick: () -> Unit = {},
    mostrarLinea: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(24.dp)) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(ClinicalPrimary)
                    .border(4.dp, Color.White, CircleShape)
            )
            if (mostrarLinea) {
                Spacer(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(Color(0xFFE2E8F0))
                )
            }
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(fecha, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                Text(etiquetaRelativa, style = MaterialTheme.typography.labelMedium, color = ClinicalOnSurfaceVariant)
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(titulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text(subtitulo, style = MaterialTheme.typography.bodyMedium, color = ClinicalOnSurfaceVariant)
                        Text(" • ", style = MaterialTheme.typography.bodySmall, color = ClinicalOnSurfaceVariant)
                    }
                    if (estadoLabel != null) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("Estado:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                            Text(estadoLabel, style = MaterialTheme.typography.bodySmall, color = ClinicalPrimary, fontWeight = FontWeight.Medium)
                        }
                    }
                    if (tieneBotonAccion) {
                        OutlinedButton(onClick = onBotonAccionClick, modifier = Modifier.fillMaxWidth()) {
                            Text(textoBotonAccion)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
