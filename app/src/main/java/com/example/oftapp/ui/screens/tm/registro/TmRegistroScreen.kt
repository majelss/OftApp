package com.example.oftapp.ui.screens.tm.registro

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.model.EstadoExamen
import com.example.oftapp.ui.theme.ClinicalClarityColors
import com.example.oftapp.ui.theme.Color
import com.example.oftapp.ui.theme.OftAppAccentBlue
import com.example.oftapp.ui.theme.OftAppBackground
import com.example.oftapp.ui.theme.OftAppBorder
import com.example.oftapp.ui.theme.OftAppBorderSubtle
import com.example.oftapp.ui.theme.OftAppFichaBlue
import com.example.oftapp.ui.theme.OftAppFichaIconBg
import com.example.oftapp.ui.theme.OftAppNavIndicator
import com.example.oftapp.ui.theme.OftAppTealContainer
import com.example.oftapp.ui.theme.OftAppTealDark
import com.example.oftapp.ui.theme.OftAppTealPrimary
import com.example.oftapp.ui.theme.OftAppTextMuted
import com.example.oftapp.ui.theme.OftAppTextPrimary
import com.example.oftapp.ui.theme.OftAppTextSecondary
import com.example.oftapp.ui.theme.OftappTheme
import com.example.oftapp.model.OjoEvaluado
import com.example.oftapp.ui.components.FechaExamenPicker

/**
 * Pantalla de Registro de Examen para Tecnólogo Médico.
 * Implementa el diseño clínico fiel al HTML de Clinical Clarity con soporte para:
 * - Selección de RUT y verificación de paciente
 * - Catálogo de tipos de examen oftalmológico
 * - Toggle segmentado para ojo evaluado (OD / OI / AO)
 * - Selección de ubicación clínica (sucursal y box)
 * - Fecha y hora nativas del dispositivo
 * - Observaciones técnicas con sugerencias rápidas
 * - Adjuntar archivo (PDF/DICOM/Planilla)
 * - Botones guardar/enviar con validación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TmRegistroScreen(
    onNavigateBack: () -> Unit = {},
    onExamenGuardado: (estado: EnvioState) -> Unit = {},
    onExamenEnviado: () -> Unit = {}
) {
    val uiState = remember { mutableStateOf(TmExamenFormState()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Lógicas auxiliares
    val isFormValid by remember {
        derivedStateOf {
            uiState.value.isCamposCompletos
        }
    }

    // Manejo de estado de ojo evaluado
    EyeToggleGroup(uiState = uiState, onSelect = { ojo ->
        uiState = uiState.copy(ojoEvaluado = ojo)
    })

    // Column principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        // --- HEADER SUPERIOR ---
        Surface(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            color = ClinicalClarityColors.Surface,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Título y contexto
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nuevo Registro Clínico",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ClinicalClarityColors.Primary
                    )
                    Text(
                        text = "Tecnólogo Médico • Oftalmología Diagnóstica",
                        fontSize = 12.sp,
                        color = ClinicalClarityColors.OnSurfaceVariant
                    )
                }

                // Botón retroceso
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retroceso")
                        .tint(ClinicalClarityColors.OnSurface)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- SECCIÓN 1: RUT DEL PACIENTE ---
        cardSection(
            title = "RUT del Paciente",
            icon = Icons.Default.Badge,
            color = ClinicalClarityColors.Primary
        ) {
            OutlinedTextField(
                value = uiState.rut,
                onValueChange = { rut -> uiState = uiState.copy(rut = rut) },
                label = { Text("RUT del Paciente", fontSize = 14.sp) },
                placeholder = { Text("15.420.891-K", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Person, tint = ClinicalClarityColors.Primary)
                },
                trailingIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Verify, tint = ClinicalClarityColors.Primary)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF8FAFC),
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedBorderColor = ClinicalClarityColors.Border,
                    focusedBorderColor = ClinicalClarityColors.Primary
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.rutValido ? "Válido" : "No verificado",
                onValueChange = {},
                label = { Text("Estado", fontSize = 12.sp) },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        uiState.rutValido
                            ? Icons.Default.CheckCircle
                            : Icons.Default.ErrorOutline,
                        tint = if (uiState.rutValido) ClinicalClarityColors.ValidatedText else ClinicalClarityColors.Error
                    )
                },
                readOnly = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFFEF3C7),
                    focusedContainerColor = Color(0xFFFEF3C7),
                    unfocusedBorderColor = ClinicalClarityColors.PendingBorder,
                    focusedBorderColor = ClinicalClarityColors.PendingBorder
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN 2: TIPO DE EXAMEN ---
        cardSection(
            title = "Tipo de Examen Oftalmológico",
            icon = Icons.Default.Biotech,
            color = ClinicalClarityColors.Secondary
        ) {
            OutlinedTextField(
                value = uiState.tipoExamen,
                onValueChange = { tipo -> uiState = uiState.copy(tipoExamen = tipo) },
                label = { Text("Tipo de Examen", fontSize = 14.sp) },
                placeholder = { Text("Campimetría Computarizada Humphrey", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Visibility, tint = ClinicalClarityColors.Secondary)
                },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, tint = ClinicalClarityColors.OnSurfaceVariant)
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF8FAFC),
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedBorderColor = ClinicalClarityColors.Border,
                    focusedBorderColor = ClinicalClarityColors.Secondary
                ),
                singleLine = true
            )

            // Protocolo debajo
            Text(
                text: "Protocolo clínico vigente: Humphrey 24-2 SITA Standard",
                fontSize = 11.sp,
                color = ClinicalClarityColors.OnSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN 3: OJO EVALUADO ---
        cardSection(
            title = "Ojo Evaluado",
            icon = Icons.Default.Visibility,
            color = ClinicalClarityColors.SecondaryCyan
        ) {
            // Container de toggles segmentados
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color(0xFFF8FAFC)
            ) {
                // Título descriptivo
                Text(
                    text = "Seleccione el ojo a evaluar:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = ClinicalClarityColors.OnSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Chips segmentados estilo HTML
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalArrangement(Arrangement.SpacedBy(4.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón OD
                    eyeToggleChip(
                        eye = OjoEvaluado.DERECHO,
                        isSelected = uiState.ojoEvaluado == OjoEvaluado.DERECHO,
                        onSelect = { uiState = uiState.copy(ojoEvaluado = it) },
                        color = ClinicalClarityColors.Primary,
                        text = "OD\nOjo Derecho",
                        bgColor = if (uiState.ojoEvaluado == OjoEvaluado.DERECHO)
                            Color(0xFFFFFFFF)
                        else Color(0xFFE2E8F0),
                        textColor = if (uiState.ojoEvaluado == OjoEvaluado.DERECHO)
                            ClinicalClarityColors.Primary
                        else ClinicalClarityColors.OnSurfaceVariant
                    )

                    // Botón OI
                    eyeToggleChip(
                        eye = OjoEvaluado.IZQUIERDO,
                        isSelected = uiState.ojoEvaluado == OjoEvaluado.IZQUIERDO,
                        onSelect = { uiState = uiState.copy(ojoEvaluado = it) },
                        color = ClinicalClarityColors.Secondary,
                        text = "OI\nOjo Izquierdo",
                        bgColor = if (uiState.ojoEvaluado == OjoEvaluado.IZQUIERDO)
                            Color(0xFFFFFFFF)
                        else Color(0xFFE2E8F0),
                        textColor = if (uiState.ojoEvaluado == OjoEvaluado.IZQUIERDO)
                            ClinicalClarityColors.Secondary
                        else ClinicalClarityColors.OnSurfaceVariant
                    )

                    // Botón AO
                    eyeToggleChip(
                        eye = OjoEvaluado.AMBOS,
                        isSelected = uiState.ojoEvaluado == OjoEvaluado.AMBOS,
                        onSelect = { uiState = uiState.copy(ojoEvaluado = it) },
                        color = ClinicalClarityColors.SecondaryCyan,
                        text = "AO\nAmbos Ojos",
                        bgColor = if (uiState.ojoEvaluado == OjoEvaluado.AMBOS)
                            Color(0xFFFFFFFF)
                        else Color(0xFFE2E8F0),
                        textColor = if (uiState.ojoEvaluado == OjoEvaluado.AMBOS)
                            ClinicalClarityColors.SecondaryCyan
                        else ClinicalClarityColors.OnSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN 4: UBICACIÓN CLÍNICA ---
        cardSection(
            title = "Ubicación Clínica",
            icon = Icons.Default.Apartment,
            color = ClinicalClarityColors.Secondary
        ) {
            OutlinedTextField(
                value = uiState.sucursalBox,
                onValueChange = { box -> uiState = uiState.copy(sucursalBox = box) },
                label = { Text("Sucursal y Box", fontSize = 14.sp) },
                placeholder = { Text("Clínica Oftalmológica Andes - Box 4 Diagnóstico", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(Icons.Default.LocationOn, tint = ClinicalClarityColors.Secondary)
                },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, tint = ClinicalClarityColors.OnSurfaceVariant)
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF8FAFC),
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedBorderColor = ClinicalClarityColors.Border,
                    focusedBorderColor = ClinicalClarityColors.Secondary
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN 5: FECHA Y HORA ---
        cardSection(
            title = "Fecha y Hora de Ejecución",
            icon = Icons.Default.Schedule,
            color = ClinicalClarityColors.Secondary
        ) {
            FechaExamenPicker(
                fechaSeleccionada = uiState.fecha,
                onFechaSeleccionada = { fecha ->
                    uiState = uiState.copy(fecha = fecha)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN 6: OBSERVACIONES TÉCNICAS ---
        cardSection(
            title = "Observaciones Técnicas Generales",
            icon = Icons.Default.ClinicalNotes,
            color = ClinicalClarityColors.Secondary
        ) {
            OutlinedTextField(
                value = uiState.observaciones,
                onValueChange = { obs -> uiState = uiState.copy(observaciones = obs) },
                label = { Text("Observaciones", fontSize = 14.sp) },
                placeholder = {
                    Text(
                        "Describa incidencias de la toma, colaboración del paciente, fijación foveal, confiabilidad de la prueba...\n(ej. Paciente colaboradora, pérdidas de fijación < 5%, falsos positivos 0%, prueba de alta confiabilidad técnica).",
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(Icons.Default.ClinicalNotes, tint = ClinicalClarityColors.Secondary)
                },
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Multiline),
                maxLines = 4,
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF8FAFC),
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedBorderColor = ClinicalClarityColors.Border,
                    focusedBorderColor = ClinicalClarityColors.Secondary
                ),
                singleLine = false
            )

            // Sugerencias rápidas
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFE0F2FE),
                        contentColor = ClinicalClarityColors.Primary
                    )
                ) {
                    Text("Excelente colaboración", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFE0F2FE),
                        contentColor = ClinicalClarityColors.Secondary
                    )
                ) {
                    Text("Fijación estable", fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN 7: ADJUNTAR ARCHIVO ---
        cardSection(
            title = "Archivo de Resultados",
            icon = Icons.Default.AttachFile,
            color = ClinicalClarityColors.Primary
        ) {
            OutlinedTextField(
                value = uiState.archivoAdjunto?.nombreArchivo ?: "No adjuntado",
                onValueChange = {},
                label = { Text("Adjuntar Archivo / Resultado", fontSize = 14.sp) },
                placeholder = { Text("PDF, DICOM o XLS", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(Icons.Default.CloudUpload, tint = ClinicalClarityColors.Primary)
                },
                trailingIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.FileDownload, tint = ClinicalClarityColors.Primary)
                    }
                },
                readOnly = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF8FAFC),
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedBorderColor = ClinicalClarityColors.Border,
                    focusedBorderColor = ClinicalClarityColors.Primary
                ),
                singleLine = true
            )

            // Badge de estado
            if (uiState.archivoAdjunto != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFD6E4F0)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            tint = ClinicalClarityColors.ValidatedText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "2 archivos adjuntos",
                            fontSize = 12.sp,
                            color = ClinicalClarityColors.ValidatedText
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.Visibility, tint = ClinicalClarityColors.OnSurfaceVariant, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN 8: VALIDACIÓN Y BOTONES ---
        cardSection(
            title = "Validación y Envío",
            icon = Icons.Default.Security,
            color = ClinicalClarityColors.Primary
        ) {
            // Barra de progreso "5 de 5 campos listos"
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Campos completados: ${uiState.camposCompletados} de 5",
                    fontSize = 12.sp,
                    color = if (uiState.isCamposCompletos) ClinicalClarityColors.ValidatedText else ClinicalClarityColors.OnSurfaceVariant
                )

                Spacer(modifier = Modifier.weight(1f))

                // Badge Listo / Pendiente
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = if (uiState.isCamposCompletos) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
                ) {
                    Text(
                        text = if (uiState.isCamposCompletos) "Listo" : "Pendiente",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (uiState.isCamposCompletos) ClinicalClarityColors.ValidatedText else ClinicalClarityColors.PendingText
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botones de acción inferiores (zona pulgar)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // Botón Guardar como Borrador
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFE8F1FC),
                        contentColor = ClinicalClarityColors.OnSurface
                    )
                ) {
                    Icon(Icons.Default.Save, tint = ClinicalClarityColors.OnSurface, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Guardar Borrador",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botón Enviar a Revisión Médica (primario)
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ClinicalClarityColors.Primary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Send, tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Enviar a Revisión",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Componente auxiliar: Card con sección formateada
 */
@Composable
fun cardSection(
    title: String,
    icon: androidx.compose.material.icons.Icon,
    color: Color,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF8FAFC),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalArrangement(Arrangement.SpaceBetween),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.padding(start = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        icon,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ClinicalClarityColors.OnSurface
                )
            }

            Divider(modifier = Modifier.height(1.dp).color(Color(0xFFE2E8F0)))

            content()
        }
    }
}

/**
 * Componente: Chip toggle segmentado para ojo evaluado
 */
@Composable
fun eyeToggleChip(
    eye: OjoEvaluado,
    isSelected: Boolean,
    onSelect: (OjoEvaluado) -> Unit,
    color: Color,
    text: String,
    bgColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(bgColor, RoundedCornerShape(16.dp))
            .clickable(onClick = { onSelect(eye) }),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}

/**
 * Estado del formulario para TM - replicado del ExamenFormState
 */
@Preview(showBackground = true)
@Composable
fun TmExamenFormStatePreview() {
    val initialState = TmExamenFormState(
        rut = "15.420.895-K",
        rutValido = true,
        pacienteVerificado = "Mariana González",
        tipoExamen = "Campimetría Computarizada Humphrey",
        ojoEvaluado = OjoEvaluado.DERECHO,
        sucursalBox = "Clínica Oftalmológica Andes - Box 4 Diagnóstico",
        fecha = "16 Oct 2024",
        hora = "09:30",
        observaciones = "Sin observaciones",
        archivoAdjunto = null,
        errorMensaje = null
    )

    TmRegistroScreen(
        onNavigateBack = {},
        onExamenGuardado = {},
        onExamenEnviado = {}
    ) {
        // Estado interno manejado dentro
    }
}

/** Estado interno replicado */
private data class TmExamenFormState(
    val rut: String = "",
    val rutValido: Boolean = false,
    val pacienteVerificado: String? = null,
    val tipoExamen: String = "",
    val ojoEvaluado: OjoEvaluado = OjoEvaluado.DERECHO,
    val sucursalBox: String = "",
    val fecha: String = "",
    val hora: String = "",
    val observaciones: String = "",
    val archivoAdjunto: String? = null,
    val errorMensaje: String? = null
) {
    val isCamposCompletos: Boolean
        get() {
            var count = 0
            if (rutValido && pacienteVerificado != null) count++
            if (tipoExamen.isNotBlank()) count++
            if (ojoEvaluado != OjoEvaluado.DESCONOCIDO) count++
            if (sucursalBox.isNotBlank()) count++
            if (archivoAdjunto != null) count++
            return count == 5
        }
}