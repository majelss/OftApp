package com.example.oftapp.ui.screens.tm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.model.TmRegistroExamen
import com.example.oftapp.model.TmEstadoExamen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TmDetalleRegistroScreen(
    onNavigateBack: () -> Unit = {},
    onEditarExamen: () -> Unit = {},
    examen: TmRegistroExamen? = null
) {
    val bgSurface = Color(0xFFF8F9FF)
    val cardBg = Color(0xFFFFFFFF)
    val primaryColor = Color(0xFF006385)
    val secondaryColor = Color(0xFF006970)

    var showConfirmDialog by remember { mutableStateOf(false) }
    var estadoEnvio by remember { mutableStateOf("Pendiente") } // "Pendiente", "Enviando", "Enviado"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Detalle De Registro", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(onClick = {}) {
                                Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones", tint = Color(0xFF3F484E))
                            }
                            Box(
                                modifier = Modifier.size(34.dp).clip(CircleShape).background(primaryColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color(0xFF0D1C2F))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgSurface)
            )
        },
        containerColor = bgSurface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Context header: Volver a Mis Registros y Chip de Rol
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable { onNavigateBack() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = primaryColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Volver a Mis Registros", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                }
                Surface(
                    color = Color(0xFFDDE9FF),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.Description, contentDescription = null, tint = primaryColor, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Rol: T.M. Oftalmología", fontSize = 10.sp, color = Color(0xFF0D1C2F), fontWeight = FontWeight.Medium)
                    }
                }
            }

            // --- 1. HEADER CARD & STATUS INFO ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = cardBg,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (estadoEnvio == "Enviado") Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(6.dp).clip(CircleShape).background(
                                        if (estadoEnvio == "Enviado") Color(0xFF15803D) else Color(0xFFD97706)
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    if (estadoEnvio == "Enviado") "En Revisión Médica" else "Pendiente de Envío",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (estadoEnvio == "Enviado") Color(0xFF15803D) else Color(0xFF92400E)
                                )
                            }
                        }
                        Surface(color = Color(0xFFE6EEFF), shape = RoundedCornerShape(4.dp)) {
                            Text("#REG-TM-2024-8841", fontSize = 10.5.sp, color = Color(0xFF3F484E), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Campimetría Computarizada Humphrey 24-2",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D1C2F)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF6F787F))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("16 de Octubre de 2024, 10:30 hrs", fontSize = 12.sp, color = Color(0xFF6F787F))
                    }
                }
            }

            // --- 2. VERIFIED PATIENT DATA CARD ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = cardBg,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = Color(0xFF6FF2FE), shape = CircleShape, modifier = Modifier.size(24.dp)) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFF006E75), modifier = Modifier.size(14.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Datos del Paciente Verificados", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = secondaryColor, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Validado", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = secondaryColor)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xFFDDE9FF), shape = CircleShape, modifier = Modifier.size(44.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("MG", fontWeight = FontWeight.Bold, color = primaryColor, fontSize = 15.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Mariana González R.", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                            Text("RUT: 15.420.891-K", fontSize = 11.5.sp, color = Color(0xFF6F787F))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFEFF4FF),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Teléfono de Contacto", fontSize = 10.5.sp, color = Color(0xFF6F787F))
                                Text("+56 9 8765 4321", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0D1C2F))
                            }
                            Column {
                                Text("Previsión Médica", fontSize = 10.5.sp, color = Color(0xFF6F787F))
                                Text("Fonasa B", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                            }
                        }
                    }
                }
            }

            // --- 3. TECHNICAL PARAMETERS REGISTERED ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = cardBg,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Settings, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Parámetros Técnicos Registrados", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                        }
                        Surface(color = primaryColor, shape = RoundedCornerShape(12.dp)) {
                            Text("AO • Ambos Ojos", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    // Param 1: Box
                    Row(verticalAlignment = Alignment.Top) {
                        Surface(color = Color(0xFFE6EEFF), shape = RoundedCornerShape(8.dp), modifier = Modifier.size(32.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.Science, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Box y Equipamiento", fontSize = 10.5.sp, color = Color(0xFF6F787F))
                            Text("Humphrey Field Analyzer 3 • Box 4 Diagnóstico", fontSize = 12.sp, color = Color(0xFF0D1C2F))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    // Param 2: Estrategia
                    Row(verticalAlignment = Alignment.Top) {
                        Surface(color = Color(0xFFE6EEFF), shape = RoundedCornerShape(8.dp), modifier = Modifier.size(32.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.Speed, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Estrategia Aplicada", fontSize = 10.5.sp, color = Color(0xFF6F787F))
                            Text("SITA-Standard 24-2 con estímulo III Blanco", fontSize = 12.sp, color = Color(0xFF0D1C2F))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    // Param 3: Tecnólogo
                    Row(verticalAlignment = Alignment.Top) {
                        Surface(color = Color(0xFFE6EEFF), shape = RoundedCornerShape(8.dp), modifier = Modifier.size(32.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.AccountCircle, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Tecnólogo Médico Responsable", fontSize = 10.5.sp, color = Color(0xFF6F787F))
                            Text("T.M. Felipe Araya M.", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                            Text("Reg. SIS: 48201", fontSize = 10.5.sp, color = Color(0xFF6F787F))
                        }
                    }
                }
            }

            // --- 4. OBSERVACIONES TÉCNICAS & TELEMETRÍA ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = cardBg,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Assignment, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Observaciones Técnicas Ingresadas", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFEFF4FF),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            "\"Paciente colaboradora en todo el proceso. Pérdidas de fijación 1/14 (7%), falsos positivos 2%, falsos negativos 0%. Confiabilidad alta. Se observa defecto escotomatoso arcuato incipiente en cuadrante nasal superior de OI.\"",
                            fontSize = 11.5.sp,
                            color = Color(0xFF0D1C2F),
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(modifier = Modifier.weight(1f), color = Color(0xFFE6EEFF), shape = RoundedCornerShape(8.dp)) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Pérdida Fijación", fontSize = 9.5.sp, color = Color(0xFF6F787F))
                                Text("7% (1/14)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                            }
                        }
                        Surface(modifier = Modifier.weight(1f), color = Color(0xFFE6EEFF), shape = RoundedCornerShape(8.dp)) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Falsos (+)", fontSize = 9.5.sp, color = Color(0xFF6F787F))
                                Text("2%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = secondaryColor)
                            }
                        }
                        Surface(modifier = Modifier.weight(1f), color = Color(0xFFE6EEFF), shape = RoundedCornerShape(8.dp)) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Falsos (-)", fontSize = 9.5.sp, color = Color(0xFF6F787F))
                                Text("0%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = secondaryColor)
                            }
                        }
                    }
                }
            }

            // --- 5. ARCHIVOS Y RESULTADOS ADJUNTOS ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = cardBg,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.AttachFile, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Archivos y Resultados Adjuntos", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                        }
                        Text("2 archivos", fontSize = 11.sp, color = Color(0xFF6F787F))
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    // Archivo 1: PDF
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFEFF4FF),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Surface(color = Color(0xFFFFDAD6), shape = RoundedCornerShape(6.dp), modifier = Modifier.size(34.dp)) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color(0xFFBA1A1A), modifier = Modifier.size(18.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Campimetria_24_2_Mariana...", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                                    Text("2.4 MB • PDF Clínico Firmado", fontSize = 10.sp, color = Color(0xFF6F787F))
                                }
                            }
                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6EEFF), contentColor = primaryColor),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(Icons.Outlined.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ver", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Archivo 2: CSV
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFEFF4FF),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Surface(color = Color(0xFF6FF2FE), shape = RoundedCornerShape(6.dp), modifier = Modifier.size(34.dp)) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF006E75), modifier = Modifier.size(18.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Mapa_Sensibilidad_Raw.csv", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                                    Text("142 KB • Planilla de datos brutos", fontSize = 10.sp, color = Color(0xFF6F787F))
                                }
                            }
                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6EEFF), contentColor = primaryColor),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Bajar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // --- 6. ACCIONES PRINCIPALES ---
            if (estadoEnvio != "Enviado") {
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviar a revisión médica", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                TextButton(
                    onClick = onEditarExamen,
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Editar datos técnicos del examen", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    color = Color(0xFFDCFCE7),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF15803D))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Enviado a revisión médica", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                    }
                }
            }

            // Nota de seguridad y bloqueo
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFEFF4FF),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Outlined.Lock, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Al enviar a revisión médica, el registro se bloqueará para edición técnica y quedará disponible para el Médico Oftalmólogo asignado.",
                        fontSize = 11.sp,
                        color = Color(0xFF3F484E)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Dialog de Confirmación de Envío Médico
        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                icon = {
                    Surface(color = Color(0xFF6FF2FE), shape = CircleShape, modifier = Modifier.size(48.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Color(0xFF006E75), modifier = Modifier.size(24.dp))
                        }
                    }
                },
                title = { Text("¿Confirmar envío médico?", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                text = {
                    Column {
                        Text("Se enviará a la bandeja de validación oftalmológica.", fontSize = 13.sp, color = Color(0xFF3F484E))
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(color = Color(0xFFEFF4FF), shape = RoundedCornerShape(8.dp)) {
                            Text(
                                "El registro pasará a estado \"En Revisión Médica\" y perderás permisos de modificación sobre los parámetros técnicos cargados.",
                                fontSize = 11.sp,
                                color = Color(0xFF3F484E),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            estadoEnvio = "Enviado"
                            showConfirmDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Sí, enviar ahora", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = false }) {
                        Text("Cancelar y revisar", color = Color(0xFF6F787F))
                    }
                }
            )
        }
    }
}
