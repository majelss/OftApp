package com.example.oftapp.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oftapp.data.SucursalClinica

private data class RetentionOption(
    val key: String,
    val title: String,
    val subtitle: String
)

@Composable
fun AdminSystemConfigScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateTab: (AdminTab) -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: AdminSystemConfigViewModel = viewModel(
        factory = AdminSystemConfigViewModel.factory(context)
    )
    val state by viewModel.uiState.collectAsState()

    val retentionOptions = remember {
        listOf(
            RetentionOption("24h", "24 hrs", "Alta rotación"),
            RetentionOption("48h", "48 hrs", "Recomendado"),
            RetentionOption("7d", "7 días", "Extendido")
        )
    }

    Scaffold(
        containerColor = ClinicalClarityColors.Surface,
        topBar = {
            AdminModuleHeader(
                title = "Sistema",
                onBackClick = onNavigateBack,
                onSearchClick = {}
            )
        },
        bottomBar = {
            AdminBottomNavBar(
                selectedTab = AdminTab.SISTEMA,
                onSelectTab = onNavigateTab
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Encabezado Clínico de Configuración Global
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(
                        color = ClinicalClarityColors.SurfaceHigh,
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = ClinicalClarityColors.Primary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "MÓDULO CLÍNICO CENTRAL",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ClinicalClarityColors.Primary,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Text(
                        text = "Configuración Global del Sistema",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = ClinicalClarityColors.OnSurface
                    )
                    Text(
                        text = "Ajuste de parámetros operativos, protocolos clínicos y seguridad.",
                        fontSize = 14.5.sp,
                        color = ClinicalClarityColors.OnSurfaceVariant
                    )
                }

                // 2. Banner de Estado de Integridad
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = ClinicalClarityColors.SurfaceLowest,
                    border = BorderStroke(1.dp, ClinicalClarityColors.CardBorder),
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(ClinicalClarityColors.PrimaryFixed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SecurityUpdateGood,
                                contentDescription = null,
                                tint = ClinicalClarityColors.Primary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Protocolo Clínico Validado",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = ClinicalClarityColors.OnSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Servicios sincronizados y conformes con normativa de salud vigente.",
                                fontSize = 13.sp,
                                color = ClinicalClarityColors.OnSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // 3. MÓDULO 1: Parámetros Clínicos & Retención
                SystemSectionCard(
                    icon = Icons.Default.Timelapse,
                    title = "Parámetros Clínicos & Retención",
                    subtitle = "Vigencias, alertas biométricas y formatos"
                ) {
                    // Vigencia de códigos de descarga
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = ClinicalClarityColors.SurfaceLow
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = ClinicalClarityColors.Secondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Vigencia de Códigos de Descarga (Pacientes)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ClinicalClarityColors.OnSurface
                                )
                            }
                            Text(
                                text = "Tiempo límite para acceso con token de seguridad único",
                                fontSize = 12.5.sp,
                                color = ClinicalClarityColors.OnSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                retentionOptions.forEach { opt ->
                                    val selected = state.vigenciaCodigos == opt.key
                                    Surface(
                                        onClick = { viewModel.onVigenciaChange(opt.key) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(54.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (selected) ClinicalClarityColors.Primary else ClinicalClarityColors.SurfaceHighest,
                                        shadowElevation = if (selected) 2.dp else 0.dp
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 4.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = opt.title,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (selected) ClinicalClarityColors.OnPrimary else ClinicalClarityColors.OnSurface
                                            )
                                            Text(
                                                text = opt.subtitle,
                                                fontSize = 10.5.sp,
                                                color = if (selected) ClinicalClarityColors.OnPrimary else ClinicalClarityColors.OnSurfaceVariant,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Umbral Alerta PIO Crítica
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = ClinicalClarityColors.SurfaceLow
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
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
                                    Icon(
                                        imageVector = Icons.Default.WarningAmber,
                                        contentDescription = null,
                                        tint = ClinicalClarityColors.Error,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Umbral Alerta PIO Crítica",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ClinicalClarityColors.OnSurface
                                    )
                                }
                                Surface(
                                    color = ClinicalClarityColors.ErrorContainer,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = "Alerta Activa",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ClinicalClarityColors.OnErrorContainer,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Text(
                                text = "Dispara aviso inmediato en historia clínica cuando el tonómetro reporte este valor o superior.",
                                fontSize = 12.5.sp,
                                color = ClinicalClarityColors.OnSurfaceVariant,
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    color = ClinicalClarityColors.SurfaceLowest,
                                    border = BorderStroke(1.dp, ClinicalClarityColors.CardBorder)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = state.umbralPio.toString(),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ClinicalClarityColors.Primary
                                        )
                                        Text(
                                            text = "mmHg",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = ClinicalClarityColors.OnSurfaceVariant,
                                            modifier = Modifier.align(Alignment.CenterEnd)
                                        )
                                    }
                                }

                                Surface(
                                    onClick = { viewModel.onDisminuirPio() },
                                    modifier = Modifier.size(52.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    color = ClinicalClarityColors.SurfaceHigh
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "−",
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ClinicalClarityColors.OnSurface
                                        )
                                    }
                                }

                                Surface(
                                    onClick = { viewModel.onAumentarPio() },
                                    modifier = Modifier.size(52.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    color = ClinicalClarityColors.SurfaceHigh
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "+",
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ClinicalClarityColors.OnSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Toggle Compresión Médica Lossless (DICOM)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = ClinicalClarityColors.SurfaceLow
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(end = 10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoLibrary,
                                        contentDescription = null,
                                        tint = ClinicalClarityColors.Primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Compresión Médica Lossless (DICOM)",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ClinicalClarityColors.OnSurface
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Archivado sin pérdida de resolución para tomografía (OCT) y angiografías",
                                    fontSize = 12.5.sp,
                                    color = ClinicalClarityColors.OnSurfaceVariant,
                                    lineHeight = 17.sp
                                )
                            }

                            Switch(
                                checked = state.compresionDicom,
                                onCheckedChange = viewModel::onCompresionDicomChange,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = ClinicalClarityColors.Primary,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = ClinicalClarityColors.SurfaceHighest
                                )
                            )
                        }
                    }
                }

                // 4. MÓDULO 2: Infraestructura Clínica
                SystemSectionCard(
                    icon = Icons.Default.Domain,
                    title = "Infraestructura Clínica",
                    subtitle = "Sedes, boxes y asignación de equipos"
                ) {
                    state.sucursales.forEach { sucursal ->
                        SucursalConfigCard(
                            sucursal = sucursal,
                            onEditClick = { viewModel.abrirDialogoEditarSucursal(sucursal) }
                        )
                    }

                    Surface(
                        onClick = { viewModel.abrirDialogoNuevaSucursal() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = ClinicalClarityColors.SurfaceHigh
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircleOutline,
                                contentDescription = null,
                                tint = ClinicalClarityColors.Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Añadir Nuevo Box o Sucursal",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ClinicalClarityColors.Primary
                            )
                        }
                    }
                }

                // 5. MÓDULO 3: Políticas de Seguridad y Privacidad
                SystemSectionCard(
                    icon = Icons.Default.VerifiedUser,
                    title = "Políticas de Seguridad y Privacidad",
                    subtitle = "Cumplimiento Ley N° 20.584 y 19.628"
                ) {
                    SecurityPolicySwitchRow(
                        title = "2FA Obligatorio para Personal Clínico",
                        subtitle = "Exigir código OTP a Médicos y Tecnólogos Médicos",
                        checked = state.twoFaObligatorio,
                        onCheckedChange = viewModel::onTwoFaChange
                    )

                    SecurityPolicySwitchRow(
                        title = "Bloqueo por Inactividad (15 min)",
                        subtitle = "Cierre automático de pantallas de box desatendidas",
                        checked = state.bloqueoInactividad,
                        onCheckedChange = viewModel::onBloqueoInactividadChange
                    )

                    SecurityPolicySwitchRow(
                        title = "Cifrado de Extremo a Extremo",
                        subtitle = "Protección en envío de informes vía WhatsApp y Email",
                        checked = state.cifradoExtremo,
                        onCheckedChange = viewModel::onCifradoExtremoChange
                    )
                }

                // 6. MÓDULO 4: Gestión de Datos y Respaldos
                SystemSectionCard(
                    icon = Icons.Default.CloudSync,
                    title = "Gestión de Datos y Respaldos",
                    subtitle = "Bases de datos relacionales y exámenes oftalmológicos"
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = ClinicalClarityColors.SurfaceLow
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Último Respaldo Automático",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ClinicalClarityColors.OnSurface
                                )
                                Surface(
                                    color = ClinicalClarityColors.SecondaryContainer,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = "100% Íntegro",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ClinicalClarityColors.OnSecondaryContainer,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = ClinicalClarityColors.Secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = state.ultimoRespaldoFecha,
                                    fontSize = 14.sp,
                                    color = ClinicalClarityColors.OnSurface
                                )
                            }

                            Text(
                                text = state.ultimoRespaldoTamano,
                                fontSize = 12.5.sp,
                                color = ClinicalClarityColors.OnSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.generarRespaldoManual() },
                        enabled = !state.respaldoEnProgreso,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.respaldoCompletadoRecien) ClinicalClarityColors.Secondary else ClinicalClarityColors.Primary,
                            contentColor = ClinicalClarityColors.OnPrimary
                        )
                    ) {
                        Icon(
                            imageVector = when {
                                state.respaldoEnProgreso -> Icons.Default.Sync
                                state.respaldoCompletadoRecien -> Icons.Default.Done
                                else -> Icons.Default.Save
                            },
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when {
                                state.respaldoEnProgreso -> "Generando volcado seguro..."
                                state.respaldoCompletadoRecien -> "¡Respaldo Exitoso! (4.83 GB)"
                                else -> "Generar Respaldo Manual de Base de Datos"
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }

                    TextButton(
                        onClick = { viewModel.restablecerValoresPredeterminados() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = null,
                            tint = ClinicalClarityColors.Error,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Restablecer Valores Predeterminados",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ClinicalClarityColors.Error
                        )
                    }
                }

                // 7. Botón Primario Guardar Todos los Cambios del Sistema
                Button(
                    onClick = { viewModel.guardarTodosLosCambios() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ClinicalClarityColors.Primary,
                        contentColor = ClinicalClarityColors.OnPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.TaskAlt,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Guardar Todos los Cambios del Sistema",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Toast flotante inferior
            AdminToastBanner(
                visible = state.toastVisible,
                title = state.toastTitulo,
                subtitle = state.toastSubtitulo,
                badgeRight = "Sincronizado",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            )
        }
    }

    // Diálogo Modal para Añadir o Editar Sucursal / Box
    if (state.mostrarDialogoSucursal) {
        val editando = state.sucursalEnEdicion
        var nombre by remember(editando) { mutableStateOf(editando?.nombre ?: "") }
        var tipoBadge by remember(editando) { mutableStateOf(editando?.tipoBadge ?: "Anexo") }
        var direccion by remember(editando) { mutableStateOf(editando?.direccion ?: "") }
        var boxesStr by remember(editando) { mutableStateOf((editando?.boxesActivos ?: 3).toString()) }
        var equipamiento by remember(editando) { mutableStateOf(editando?.equipamiento ?: "OCT + Tonometría") }

        AlertDialog(
            onDismissRequest = { viewModel.cerrarDialogoSucursal() },
            title = {
                Text(
                    text = if (editando != null) "Editar Sede / Sucursal" else "Añadir Nuevo Box o Sucursal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre de la Sede") },
                        placeholder = { Text("Ej. Sucursal Viña del Mar") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección Clínica") },
                        placeholder = { Text("Ej. Av. Libertad 450, Of. 301") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = boxesActivosSafeText(boxesStr),
                            onValueChange = { boxesStr = it.filter { c -> c.isDigit() } },
                            label = { Text("N° Boxes") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = tipoBadge,
                            onValueChange = { tipoBadge = it },
                            label = { Text("Categoría") },
                            placeholder = { Text("Principal / Anexo") },
                            singleLine = true,
                            modifier = Modifier.weight(1.3f)
                        )
                    }
                    OutlinedTextField(
                        value = equipamiento,
                        onValueChange = { equipamiento = it },
                        label = { Text("Equipamiento Asignado") },
                        placeholder = { Text("OCT + Campimetría + Láser") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.guardarSucursalDialogo(
                            nombre = nombre,
                            tipoBadge = tipoBadge,
                            direccion = direccion,
                            boxesActivos = boxesStr.toIntOrNull() ?: 1,
                            equipamiento = equipamiento
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ClinicalClarityColors.Primary)
                ) {
                    Text("Guardar Sede")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cerrarDialogoSucursal() }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

private fun boxesActivosSafeText(input: String): String = input

@Composable
private fun SystemSectionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ClinicalClarityColors.SurfaceLowest,
        border = BorderStroke(1.dp, ClinicalClarityColors.CardBorder),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ClinicalClarityColors.SurfaceHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = ClinicalClarityColors.Primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 17.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ClinicalClarityColors.OnSurface
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.5.sp,
                        color = ClinicalClarityColors.OnSurfaceVariant
                    )
                }
            }

            content()
        }
    }
}

@Composable
private fun SucursalConfigCard(
    sucursal: SucursalClinica,
    onEditClick: () -> Unit
) {
    val isPrincipal = sucursal.tipoBadge.equals("Principal", ignoreCase = true)
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = ClinicalClarityColors.SurfaceLow
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = sucursal.nombre,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ClinicalClarityColors.OnSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = if (isPrincipal) ClinicalClarityColors.SecondaryContainer else ClinicalClarityColors.SurfaceHighest,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = sucursal.tipoBadge,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPrincipal) ClinicalClarityColors.OnSecondaryContainer else ClinicalClarityColors.OnSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = sucursal.direccion,
                        fontSize = 13.sp,
                        color = ClinicalClarityColors.OnSurfaceVariant
                    )
                }

                Surface(
                    onClick = onEditClick,
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = ClinicalClarityColors.SurfaceLowest,
                    shadowElevation = 1.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar sucursal ${sucursal.nombre}",
                            tint = ClinicalClarityColors.Primary,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MeetingRoom,
                        contentDescription = null,
                        tint = ClinicalClarityColors.Secondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${sucursal.boxesActivos} Boxes activos",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ClinicalClarityColors.OnSurface
                    )
                }

                Text(
                    text = sucursal.equipamiento,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = ClinicalClarityColors.Primary,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun SecurityPolicySwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = ClinicalClarityColors.SurfaceLow
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 10.dp)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ClinicalClarityColors.OnSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.5.sp,
                    color = ClinicalClarityColors.OnSurfaceVariant,
                    lineHeight = 17.sp
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = ClinicalClarityColors.Primary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = ClinicalClarityColors.SurfaceHighest
                )
            )
        }
    }
}
