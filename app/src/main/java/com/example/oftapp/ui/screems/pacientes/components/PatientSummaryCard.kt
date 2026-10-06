package com.example.oftapp.ui.screems.pacientes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.oftapp.ui.components.ClinicalTag
import com.example.oftapp.ui.theme.*

@Composable
fun PatientSummaryCard(
    nombre: String,
    rut: String,
    edad: String,
    fonasa: String,
    activo: Boolean,
    atenciones: Int,
    validados: Int,
    etiquetas: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    // Calcular iniciales desde las dos primeras palabras del nombre
    val iniciales = nombre.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(ClinicalPrimary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = iniciales,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ClinicalPrimary
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            ClinicalTag(
                                text = fonasa,
                                backgroundColor = ClinicalPrimary.copy(alpha = 0.08f),
                                contentColor = ClinicalPrimary
                            )
                            if (activo) {
                                ClinicalTag(
                                    text = "● Activo",
                                    backgroundColor = ClinicalSuccess.copy(alpha = 0.1f),
                                    contentColor = ClinicalSuccess
                                )
                            }
                        }
                        Text(
                            text = "$rut  •  $edad años",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ClinicalOnSurfaceVariant
                        )
                    }
                }
            }

            // Etiquetas opcionales pasadas por parámetro (sin datos clínicos fijos)
            if (etiquetas.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    etiquetas.forEach { etiqueta ->
                        ClinicalTag(
                            text = etiqueta,
                            backgroundColor = ClinicalPrimary.copy(alpha = 0.08f),
                            contentColor = ClinicalOnSurface
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Atenciones",
                        tint = ClinicalPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "$atenciones atenciones",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                Box(modifier = Modifier.height(20.dp).width(1.dp).background(Color(0xFFE2E8F0)))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Verified,
                        contentDescription = "Validados",
                        tint = ClinicalSuccess,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "$validados validados",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = ClinicalSuccess
                    )
                }
            }
        }
    }
}
