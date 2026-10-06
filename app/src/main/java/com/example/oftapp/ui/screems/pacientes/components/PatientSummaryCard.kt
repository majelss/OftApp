package com.example.oftapp.ui.screems.pacientes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Visibility
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(ClinicalPrimary.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                        Text("JP", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ClinicalPrimary)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            ClinicalTag(text = fonasa, backgroundColor = ClinicalPrimary.copy(alpha = 0.08f), contentColor = ClinicalPrimary)
                            if (activo) {
                                ClinicalTag(text = "● Activo", backgroundColor = ClinicalSuccess.copy(alpha = 0.1f), contentColor = ClinicalSuccess)
                            }
                        }
                        Text(" •  años", style = MaterialTheme.typography.bodyMedium, color = ClinicalOnSurfaceVariant)
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                ClinicalTag(text = "Glaucoma Sospecha AO", backgroundColor = ClinicalError.copy(alpha = 0.1f), contentColor = ClinicalError)
                Icon(Icons.Filled.Warning, null, tint = ClinicalError, modifier = Modifier.size(16.dp))
                ClinicalTag(text = "Miopía Magna OD", backgroundColor = ClinicalSecondary.copy(alpha = 0.1f), contentColor = ClinicalSecondary)
                Icon(Icons.Outlined.Visibility, null, tint = ClinicalSecondary, modifier = Modifier.size(16.dp))
                ClinicalTag(text = "PIO: 16 mmHg", backgroundColor = Color(0xFF0F766E).copy(alpha = 0.1f), contentColor = Color(0xFF0F766E))
                Icon(Icons.Filled.CheckCircle, null, tint = Color(0xFF0F766E), modifier = Modifier.size(16.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CheckCircle, null, tint = ClinicalPrimary, modifier = Modifier.size(18.dp))
                    Text(" atenciones", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                }
                Box(modifier = Modifier.height(20.dp).width(1.dp).background(Color(0xFFE2E8F0)))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Verified, null, tint = ClinicalSuccess, modifier = Modifier.size(18.dp))
                    Text(" validados", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = ClinicalSuccess)
                }
            }
        }
    }
}
