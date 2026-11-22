package com.example.oftapp.ui.screens.tm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.oftapp.model.TmPaciente
import com.example.oftapp.ui.components.OftAppUnifiedTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TmMisRegistrosScreen(
    onNavigateBack: () -> Unit = {},
    onVerDetalle: (TmRegistroExamen?) -> Unit = {},
    onEditarRegistro: (TmRegistroExamen?) -> Unit = {},
    listaExamenes: List<TmRegistroExamen> = emptyList()
) {
    val bgSurface = Color(0xFFF8F9FF)
    val cardBg = Color(0xFFFFFFFF)
    val primaryColor = Color(0xFF006385)
    val secondaryColor = Color(0xFF006970)

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }

    val filterOptions = listOf(
        "Todos (8)",
        "Pendientes (3)",
        "En revisión (3)",
        "Validados (2)"
    )

    Scaffold(
        topBar = {
            OftAppUnifiedTopBar(
                title = "Mis Registros TM",
                badgeSubtitle = "OFTAPP CLINICAL • TM",
                onBackClick = onNavigateBack
            )
        },
        containerColor = bgSurface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Título de la sección
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mis Registros", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                Surface(
                    color = Color(0xFFC3E8FF),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = primaryColor, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("T.M. Box 4", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                    }
                }
            }
            Text("Exámenes ingresados y estado en el flujo clínico", fontSize = 12.sp, color = Color(0xFF3F484E))

            Spacer(modifier = Modifier.height(12.dp))

            // Buscador
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por RUT o nombre...", fontSize = 13.sp, color = Color(0xFF6F787F)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF6F787F)) },
                trailingIcon = { Icon(Icons.Default.FilterList, contentDescription = "Filtro", tint = Color(0xFF6F787F)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFE6EEFF),
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = primaryColor
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Chips Horizontales de Filtros
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                items(filterOptions.size) { index ->
                    val option = filterOptions[index]
                    val isSelected = (index == 0 && selectedFilter == "Todos") || selectedFilter == option
                    
                    val chipBg = when {
                        isSelected -> primaryColor
                        option.contains("Pendientes") -> Color(0xFFFEF3C7)
                        option.contains("revisión") -> Color(0xFFE0F2FE)
                        option.contains("Validados") -> Color(0xFFDCFCE7)
                        else -> Color(0xFFE6EEFF)
                    }
                    val chipText = when {
                        isSelected -> Color.White
                        option.contains("Pendientes") -> Color(0xFF92400E)
                        option.contains("revisión") -> Color(0xFF0369A1)
                        option.contains("Validados") -> Color(0xFF15803D)
                        else -> Color(0xFF0D1C2F)
                    }

                    Surface(
                        modifier = Modifier.clickable { selectedFilter = option },
                        color = chipBg,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSelected) {
                                Icon(Icons.Outlined.Dashboard, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                            } else {
                                Box(
                                    modifier = Modifier.size(6.dp).clip(CircleShape).background(chipText)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(option, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = chipText)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de Tarjetas
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Card 1: Borrador T.M. - Mariana González
                item {
                    RegistroExamenCard(
                        etiquetaRol = "Borrador T.M.",
                        pacienteNombre = "Mariana González R.",
                        rut = "15.420.891-K",
                        estadoBadge = "Pendiente (Borrador)",
                        estadoBadgeBg = Color(0xFFFEF3C7),
                        estadoBadgeText = Color(0xFF92400E),
                        examenTitulo = "Campimetría Computarizada",
                        ojo = "AO",
                        horario = "Hoy 10:30 hrs • Box 4",
                        alerta = "Falta adjuntar reporte de confiabilidad visual.",
                        esEditable = true,
                        onVerDetalle = { onVerDetalle(null) },
                        onEditar = { onEditarRegistro(null) }
                    )
                }

                // Card 2: En captura - Carlos Mendoza
                item {
                    RegistroExamenCard(
                        etiquetaRol = "En captura",
                        pacienteNombre = "Carlos Mendoza Silva",
                        rut = "12.873.402-3",
                        estadoBadge = "Pendiente",
                        estadoBadgeBg = Color(0xFFFEF3C7),
                        estadoBadgeText = Color(0xFF92400E),
                        examenTitulo = "Topografía Pentacam",
                        ojo = "OD",
                        horario = "Hoy 09:15 hrs • Box 4",
                        esEditable = true,
                        onVerDetalle = { onVerDetalle(null) },
                        onEditar = { onEditarRegistro(null) }
                    )
                }

                // Card 3: Solo lectura - Roberto Sánchez
                item {
                    RegistroExamenCard(
                        etiquetaRol = "Solo lectura",
                        pacienteNombre = "Roberto Sánchez V.",
                        rut = "9.432.112-8",
                        estadoBadge = "En Revisión Médica",
                        estadoBadgeBg = Color(0xFFE0F2FE),
                        estadoBadgeText = Color(0xFF0369A1),
                        examenTitulo = "Curva de Tensión Aplanática",
                        ojo = "OI",
                        horario = "15 Oct 2024 • Box 4",
                        infoAdicional = "Enviado a Dra. Valenzuela (Oftalmología)",
                        esEditable = false,
                        onVerDetalle = { onVerDetalle(null) }
                    )
                }

                // Card 4: Solo lectura - Andrea Peñaloza
                item {
                    RegistroExamenCard(
                        etiquetaRol = "Solo lectura",
                        pacienteNombre = "Andrea Peñaloza T.",
                        rut = "17.654.321-0",
                        estadoBadge = "Validado",
                        estadoBadgeBg = Color(0xFFDCFCE7),
                        estadoBadgeText = Color(0xFF15803D),
                        examenTitulo = "Paquimetría Corneal",
                        ojo = "AO",
                        horario = "14 Oct 2024 • Box 4",
                        infoAdicional = "Validado y archivado por Dr. Camus",
                        esEditable = false,
                        onVerDetalle = { onVerDetalle(null) }
                    )
                }

                // Banner Informativo Regla de Flujo Clínico
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        color = Color(0xFFEFF4FF),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Outlined.Info, contentDescription = null, tint = primaryColor, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Regla del flujo clínico", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                                Text(
                                    "Como Tecnólogo Médico, puedes editar borradores pendientes antes de enviarlos a revisión médica. Una vez en revisión o validados, el registro pasa a custodia médica.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF3F484E)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RegistroExamenCard(
    etiquetaRol: String,
    pacienteNombre: String,
    rut: String,
    estadoBadge: String,
    estadoBadgeBg: Color,
    estadoBadgeText: Color,
    examenTitulo: String,
    ojo: String,
    horario: String,
    alerta: String? = null,
    infoAdicional: String? = null,
    esEditable: Boolean,
    onVerDetalle: () -> Unit,
    onEditar: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (esEditable) Icons.Default.Assignment else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (esEditable) Color(0xFF006385) else Color(0xFF6F787F),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(etiquetaRol.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (esEditable) Color(0xFF006385) else Color(0xFF6F787F))
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(pacienteNombre, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                    Text("RUT $rut", fontSize = 11.5.sp, color = Color(0xFF6F787F))
                }
                Surface(color = estadoBadgeBg, shape = RoundedCornerShape(12.dp)) {
                    Text(
                        estadoBadge,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = estadoBadgeText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sub-box de examen y ojo
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFEFF4FF),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Visibility, contentDescription = null, tint = Color(0xFF006385), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(examenTitulo, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1C2F))
                        }
                        Surface(color = Color(0xFF325F9B), shape = RoundedCornerShape(4.dp)) {
                            Text(ojo, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Schedule, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFF6F787F))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(horario, fontSize = 11.sp, color = Color(0xFF6F787F))
                    }
                }
            }

            // Alerta opcional (Acción pendiente)
            if (alerta != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFDAD6),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFBA1A1A), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text("Acción pendiente", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF93000A))
                            Text(alerta, fontSize = 10.5.sp, color = Color(0xFF93000A))
                        }
                    }
                }
            }

            // Info de derivación médica opcional
            if (infoAdicional != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFE6EEFF),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF006385), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(infoAdicional, fontSize = 11.sp, color = Color(0xFF0D1C2F))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botones según permisos del flujo clínico
            if (esEditable) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onEditar,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6FF2FE), contentColor = Color(0xFF006E75))
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Editar registro", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = onVerDetalle,
                        modifier = Modifier.height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6EEFF), contentColor = Color(0xFF0D1C2F))
                    ) {
                        Icon(Icons.Outlined.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ver detalle", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Button(
                    onClick = onVerDetalle,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6EEFF), contentColor = Color(0xFF0D1C2F))
                ) {
                    Icon(Icons.Outlined.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver detalle de examen", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
