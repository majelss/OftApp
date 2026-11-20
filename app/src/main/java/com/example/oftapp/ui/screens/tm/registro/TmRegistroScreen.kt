package com.example.oftapp.ui.screens.tm.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.oftapp.ui.theme.ClinicalClarityColors
import com.example.oftapp.domain.RutValidator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TmRegistroScreen(
    onNavigateBack: () -> Unit = {},
    onExamenEnviado: () -> Unit = {}
) {
    val bgSurface = Color(0xFFF8F9FF)
    val cardBg = Color(0xFFFFFFFF)
    val primaryColor = Color(0xFF006385)
    val secondaryColor = Color(0xFF006970)

    var rutText by remember { mutableStateOf("15.420.891-K") }
    var pacienteVerificado by remember { mutableStateOf(true) }
    var selectedExam by remember { mutableStateOf("Campimetría Computarizada Humphrey (24-2 / 30-2)") }
    var expandedExamDropdown by remember { mutableStateOf(false) }
    var selectedEye by remember { mutableStateOf("OD") }
    var selectedBox by remember { mutableStateOf("Clínica Oftalmológica Andes - Box 4 Diagnóstico") }
    var examDate by remember { mutableStateOf("16 Oct 2024") }
    var examTime by remember { mutableStateOf("10:30 hrs") }
    var observaciones by remember { 
        mutableStateOf("Paciente colaboradora durante toda la sesión. Buena fijación foveal mantenida. Pérdidas de fijación < 4%, Falsos Positivos 0%, Falsos Negativos 2%. Curva con índice de confiabilidad óptimo.") 
    }
    var archivoAdjunto by remember { mutableStateOf<String?>("Campimetria_24_2_MarianaGonzalez.pdf") }

    val examOptions = listOf(
        "Campimetría Computarizada Humphrey (24-2 / 30-2)",
        "Topografía Corneal Pentacam (Elevación / Paquimetría)",
        "OCT Macular y Papila Óptica (Capa Fibras Nerviosas)",
        "Curva de Tensión Aplanática (Goldmann)",
        "Agudeza Visual, Refracción Subjetiva y Cicloplejia",
        "Recuento de Células Endoteliales (Microscopía Especular)"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "OFTAPP CLÍNICA",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryColor
                            )
                            Surface(
                                color = Color(0xFFE0F2FE),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "TECNÓLOGO MÉDICO",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones", tint = Color(0xFF3F484E))
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
            // --- BANNER SUPERIOR MATERIAL 3 ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFDDE9FF),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        color = primaryColor,
                        shape = CircleShape,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.Assignment, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Nuevo Registro Clínico",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D1C2F)
                            )
                            Surface(
                                color = primaryColor,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "ACTIVO",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Los campos marcados con asterisco (*) son obligatorios para enviar a revisión del médico oftalmólogo tratante.",
                            fontSize = 11.5.sp,
                            color = Color(0xFF3F484E),
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // --- 1. RUT DEL PACIENTE & VERIFICACIÓN ---
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
                            Icon(Icons.Outlined.AccountCircle, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("RUT del Paciente *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                        }
                        Text("Formato chileno", fontSize = 11.sp, color = Color(0xFF6F787F))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = rutText,
                        onValueChange = { rutText = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = {
                            if (rutText.isNotEmpty()) {
                                IconButton(onClick = { rutText = "" }) {
                                    Icon(Icons.Default.Cancel, contentDescription = "Limpiar", tint = Color(0xFF6F787F), modifier = Modifier.size(18.dp))
                                }
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFEFF4FF),
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = primaryColor
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { pacienteVerificado = true },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6FF2FE), contentColor = Color(0xFF006E75))
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Verificar paciente", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    if (pacienteVerificado) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFE6EEFF),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = primaryColor,
                                    shape = CircleShape,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("MG", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Mariana González R.", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = Color(0xFF0D1C2F))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = secondaryColor, modifier = Modifier.size(16.dp))
                                    }
                                    Text("42 años • Femenino • Ficha #OF-88219 • Fonasa B", fontSize = 11.sp, color = Color(0xFF3F484E))
                                }
                                Surface(
                                    color = Color(0xFFC3E8FF),
                                    shape = CircleShape,
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Outlined.Visibility, contentDescription = "Ver ficha", tint = primaryColor, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- 2. TIPO DE EXAMEN OFTALMOLÓGICO ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = cardBg,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Science, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Tipo de Examen Oftalmológico *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedExamDropdown,
                        onExpandedChange = { expandedExamDropdown = !expandedExamDropdown }
                    ) {
                        OutlinedTextField(
                            value = selectedExam,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedExamDropdown) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFEFF4FF),
                                focusedContainerColor = Color.White,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = primaryColor
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedExamDropdown,
                            onDismissRequest = { expandedExamDropdown = false }
                        ) {
                            examOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, fontSize = 13.sp) },
                                    onClick = {
                                        selectedExam = option
                                        expandedExamDropdown = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Info, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF6F787F))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Protocolo clínico vigente: Humphrey 24-2 SITA Standard", fontSize = 11.sp, color = Color(0xFF6F787F))
                    }
                }
            }

            // --- 3. OJO EVALUADO (TOGGLE OD / OI / AO) ---
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
                            Icon(Icons.Outlined.Visibility, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ojo Evaluado *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                        }
                        Surface(color = Color(0xFFC3E8FF), shape = RoundedCornerShape(4.dp)) {
                            Text(
                                "$selectedEye SELECCIONADO",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().background(Color(0xFFEFF4FF), RoundedCornerShape(12.dp)).padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("OD" to "Ojo Derecho", "OI" to "Ojo Izquierdo", "AO" to "Ambos Ojos").forEach { (code, label) ->
                            val isSelected = selectedEye == code
                            Surface(
                                modifier = Modifier.weight(1f).height(50.dp).clickable { selectedEye = code },
                                color = if (isSelected) primaryColor else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        code,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color(0xFF0D1C2F)
                                    )
                                    Text(
                                        label,
                                        fontSize = 10.sp,
                                        color = if (isSelected) Color.White.copy(alpha = 0.9f) else Color(0xFF6F787F)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // --- 4. SUCURSAL Y BOX ASIGNADO ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = cardBg,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Apartment, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sucursal y Box Asignado *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = selectedBox,
                        onValueChange = { selectedBox = it },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = { Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Color(0xFF6F787F)) },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFEFF4FF),
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = primaryColor
                        )
                    )
                }
            }

            // --- 5. FECHA Y HORA DE EJECUCIÓN ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = cardBg,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Schedule, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Fecha y Hora de Ejecución *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = examDate,
                            onValueChange = { examDate = it },
                            modifier = Modifier.weight(1f),
                            leadingIcon = { Icon(Icons.Outlined.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFEFF4FF),
                                focusedContainerColor = Color.White,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = primaryColor
                            )
                        )
                        OutlinedTextField(
                            value = examTime,
                            onValueChange = { examTime = it },
                            modifier = Modifier.weight(1f),
                            leadingIcon = { Icon(Icons.Outlined.Alarm, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFEFF4FF),
                                focusedContainerColor = Color.White,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = primaryColor
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Marca temporal sincronizada automáticamente con el servidor local del box.",
                        fontSize = 11.sp,
                        color = Color(0xFF6F787F)
                    )
                }
            }

            // --- 6. OBSERVACIONES TÉCNICAS GENERALES ---
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
                            Icon(Icons.Outlined.Description, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Observaciones Técnicas Generales", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                        }
                        Text("Opcional", fontSize = 11.sp, color = Color(0xFF6F787F))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = observaciones,
                        onValueChange = { observaciones = it },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFEFF4FF),
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = primaryColor
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("+ Excelente colaboración", "+ Fijación estable", "+ Fatiga leve").forEach { chipText ->
                            Surface(
                                modifier = Modifier.clickable {
                                    observaciones = if (observaciones.isBlank()) chipText.removePrefix("+ ") else "$observaciones • ${chipText.removePrefix("+ ")}"
                                },
                                color = Color(0xFFE6EEFF),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(chipText, fontSize = 10.5.sp, color = primaryColor, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                        }
                    }
                }
            }

            // --- 7. ARCHIVO DE RESULTADOS ---
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
                            Text("Archivo de Resultados *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                        }
                        Text("PDF, DICOM o XLS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = secondaryColor)
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Drop zone visual
                    Surface(
                        modifier = Modifier.fillMaxWidth().clickable {
                            archivoAdjunto = "Campimetria_24_2_MarianaGonzalez.pdf"
                        },
                        color = Color(0xFFEFF4FF),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(color = Color(0xFFDDE9FF), shape = CircleShape, modifier = Modifier.size(42.dp)) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Outlined.CloudUpload, contentDescription = null, tint = primaryColor)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Tocar para seleccionar o arrastrar archivo", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                            Text("Formatos permitidos: PDF, DCM (DICOM), PNG, JPG, XLSX (Máx. 25 MB)", fontSize = 10.sp, color = Color(0xFF6F787F))
                        }
                    }

                    if (archivoAdjunto != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFE6EEFF),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Surface(color = Color(0xFFFFDAD6), shape = RoundedCornerShape(8.dp), modifier = Modifier.size(36.dp)) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color(0xFFBA1A1A), modifier = Modifier.size(20.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(archivoAdjunto!!, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("2.4 MB  •  ", fontSize = 10.sp, color = Color(0xFF6F787F))
                                            Icon(Icons.Default.Check, contentDescription = null, tint = secondaryColor, modifier = Modifier.size(12.dp))
                                            Text("Validado técnicamente", fontSize = 10.sp, color = secondaryColor, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                Row {
                                    IconButton(onClick = {}) { Icon(Icons.Default.Sync, contentDescription = "Reemplazar", tint = Color(0xFF3F484E)) }
                                    IconButton(onClick = { archivoAdjunto = null }) { Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFBA1A1A)) }
                                }
                            }
                        }
                    }
                }
            }

            // --- VALIDACIÓN 5 DE 5 LISTOS ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFEFF4FF),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TaskAlt, contentDescription = null, tint = secondaryColor, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Todos los campos requeridos (*): 5 de 5 listos", fontSize = 12.sp, color = Color(0xFF0D1C2F))
                    }
                    Surface(color = Color(0xFF6FF2FE), shape = RoundedCornerShape(12.dp)) {
                        Text("LISTO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006E75), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                }
            }

            // --- 8. BOTONES DE ACCIÓN INFERIORES ---
            Button(
                onClick = onExamenEnviado,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar y Pasar a Revisión", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onExamenEnviado,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryColor),
                border = androidx.compose.foundation.BorderStroke(2.dp, primaryColor)
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar como Borrador (Pendiente)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
