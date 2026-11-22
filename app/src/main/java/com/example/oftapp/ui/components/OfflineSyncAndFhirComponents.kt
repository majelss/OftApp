package com.example.oftapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.data.remote.FhirReferenciaSintetica

/**
 * Banner de resiliencia Offline/Online con Room y Spring Boot (Requisito 4 y Anexo C).
 * Muestra explícitamente «Modo offline» y «Sincronización pendiente» cuando el microservicio
 * no está disponible, y permite disparar la sincronización al reconectar.
 */
@Composable
fun OfflineSyncStatusBanner(
    isOffline: Boolean,
    pendientesCount: Int,
    estadoSincronizacion: String,
    onSincronizarClick: () -> Unit,
    onAlternarOfflineDemo: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val bgColor = when {
        isOffline -> Color(0xFFFEF3C7)
        pendientesCount > 0 -> Color(0xFFE0F2FE)
        else -> Color(0xFFDCFCE7)
    }
    val borderColor = when {
        isOffline -> Color(0xFFF59E0B)
        pendientesCount > 0 -> Color(0xFF0284C7)
        else -> Color(0xFF22C55E)
    }
    val textColor = when {
        isOffline -> Color(0xFF92400E)
        pendientesCount > 0 -> Color(0xFF0369A1)
        else -> Color(0xFF15803D)
    }
    val icon = when {
        isOffline -> Icons.Default.CloudOff
        pendientesCount > 0 -> Icons.Default.CloudSync
        else -> Icons.Default.CloudDone
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                liveRegion = LiveRegionMode.Polite
                contentDescription = "Estado de conectividad: $estadoSincronizacion"
            },
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = estadoSincronizacion,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = if (isOffline) {
                            "Respaldo local Room SQLite activo • Sin pérdida de datos"
                        } else {
                            "Microservicio Spring Boot y Room SQLite sincronizados"
                        },
                        fontSize = 11.sp,
                        color = textColor.copy(alpha = 0.85f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (onAlternarOfflineDemo != null) {
                    Surface(
                        onClick = onAlternarOfflineDemo,
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.75f),
                        border = BorderStroke(1.dp, borderColor.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = if (isOffline) "Conectar" else "Offline",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }
                }

                Surface(
                    onClick = onSincronizarClick,
                    shape = RoundedCornerShape(8.dp),
                    color = textColor
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sincronizar ahora",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sincronizar",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Tarjeta visual que integra el consumo de la API REST externa pública HAPI FHIR R4
 * (https://hapi.fhir.org/baseR4/DiagnosticReport) con datos sintéticos (IE 3.1.4).
 */
@Composable
fun FhirExternalReferenceCard(
    estadoFhir: String,
    referencias: List<FhirReferenciaSintetica>,
    onRecargarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            tint = Color(0xFF006385),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ESTÁNDAR HL7 FHIR R4 (API REST EXTERNA)",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF006385),
                            letterSpacing = 0.4.sp
                        )
                        Text(
                            text = estadoFhir,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155)
                        )
                    }
                }

                IconButton(
                    onClick = onRecargarClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Actualizar API externa HAPI FHIR",
                        tint = Color(0xFF006385),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            referencias.take(3).forEach { item ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            Text(
                                text = item.nombreEstudio,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0D1C2F),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${item.fhirId} • ${item.codigoEstandar} • ${item.fechaReferencia}",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color(0xFFE0F2FE)
                        ) {
                            Text(
                                text = item.estadoFhir,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0369A1),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
