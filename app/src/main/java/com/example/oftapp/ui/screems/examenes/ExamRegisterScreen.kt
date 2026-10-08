package com.example.oftapp.ui.screems.examenes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oftapp.model.DocumentoAdjunto
import com.example.oftapp.model.OjoEvaluado
import com.example.oftapp.ui.theme.OftAppAccentBlue
import com.example.oftapp.ui.theme.OftAppBackground
import com.example.oftapp.ui.theme.OftAppBorder
import com.example.oftapp.ui.theme.OftAppBorderSubtle
import com.example.oftapp.ui.theme.OftAppFichaBlue
import com.example.oftapp.ui.theme.OftAppFichaIconBg
import com.example.oftapp.ui.theme.OftAppNavIndicator
import com.example.oftapp.ui.theme.OftAppShieldBg
import com.example.oftapp.ui.theme.OftAppTealContainer
import com.example.oftapp.ui.theme.OftAppTealDark
import com.example.oftapp.ui.theme.OftAppTealPrimary
import com.example.oftapp.ui.theme.OftAppTextMuted
import com.example.oftapp.ui.theme.OftAppTextPrimary
import com.example.oftapp.ui.theme.OftAppTextSecondary
import com.example.oftapp.ui.theme.OftappTheme

/**
 * Pantalla de Registro de Examen Oftalmológico (DSY1105 - OftApp).
 * Implementa el diseño clínico fiel a la maqueta con soporte para triage,
 * selección de lateralidad, adjunto simulado, observaciones y firma de trazabilidad.
 */
@Composable
fun ExamRegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: ExamRegisterViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToAtenciones: () -> Unit = {},
    onNavigateToHistorial: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.mensajeError) {
        uiState.mensajeError?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onDismissError()
        }
    }

    if (uiState.guardadoExitoso) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissGuardadoExitoso() },
            title = {
                Text(
                    text = "Examen Guardado Exitosamente",
                    fontWeight = FontWeight.Bold,
                    color = OftAppTealDark
                )
            },
            text = {
                Text(
                    text = "El examen '${uiState.tipoExamen}' para el paciente ${uiState.paciente.nombreCompleto} " +
                            "ha sido registrado con trazabilidad médica y firma electrónica (${uiState.firmaProfesional}).",
                    fontSize = 14.sp,
                    color = OftAppTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.onDismissGuardadoExitoso() },
                    colors = ButtonDefaults.buttonColors(containerColor = OftAppTealPrimary)
                ) {
                    Text("Aceptar")
                }
            }
        )
    }

    if (uiState.showDescartarDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onCancelarDescartar() },
            title = {
                Text(
                    text = "¿Descartar borrador?",
                    fontWeight = FontWeight.Bold,
                    color = OftAppTextPrimary
                )
            },
            text = {
                Text(
                    text = "Se restablecerán los campos de este examen a su estado inicial.",
                    fontSize = 14.sp,
                    color = OftAppTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.onConfirmarDescartar() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Descartar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onCancelarDescartar() }) {
                    Text("Continuar editando", color = OftAppTealPrimary)
                }
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OftAppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBarContent(
                onNotificationClick = {},
                onProfileClick = {}
            )
        },
        bottomBar = {
            OftAppBottomNavigationBar(
                selectedTab = "Registro",
                onTabSelected = { tab ->
                    when (tab) {
                        "Dashboard" -> onNavigateToDashboard()
                        "Atenciones" -> onNavigateToAtenciones()
                        "Registro" -> { /* Ya en registro */ }
                        "Historial" -> onNavigateToHistorial()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Sub-barra: Triage Oftalmológico / Registrar Examen
            TriageSubHeader(
                onBackClick = onNavigateBack,
                onQuickSaveClick = { viewModel.onGuardarExamen() }
            )

            // Contenido del Formulario
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Card: Ficha Clínica Digital
                FichaClinicaCard(
                    nombre = uiState.paciente.nombreCompleto,
                    rut = uiState.paciente.rut,
                    edad = uiState.paciente.edad
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Campo: RUT / ID Paciente
                RutPacienteField(
                    rutTexto = uiState.pacienteQuery,
                    onSearchClick = {}
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Campo: Tipo de Examen (Dropdown)
                TipoExamenField(
                    tipoExamen = uiState.tipoExamen,
                    protocolo = uiState.protocoloExamen,
                    isExpanded = uiState.isTipoExamenDropdownOpen,
                    opciones = viewModel.catalogoExamenes,
                    onToggleDropdown = { viewModel.setTipoExamenDropdownOpen(it) },
                    onSelect = { tipo, protocolo ->
                        viewModel.onTipoExamenSelected(tipo, protocolo)
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Campo: Sucursal / Box Clínico (Dropdown)
                SucursalBoxField(
                    sucursal = uiState.sucursalBox,
                    isExpanded = uiState.isSucursalDropdownOpen,
                    opciones = viewModel.catalogoSucursales,
                    onToggleDropdown = { viewModel.setSucursalDropdownOpen(it) },
                    onSelect = { viewModel.onSucursalSelected(it) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Sección: Selección de Ojo Evaluado (Lateralidad)
                LateralidadSection(
                    ojoSeleccionado = uiState.ojoEvaluado,
                    onSelectOjo = { viewModel.onOjoSelected(it) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Sección: Adjuntar Archivo / Resultado
                AdjuntarArchivoSection(
                    archivoAdjunto = uiState.documentoAdjunto,
                    onAdjuntarClick = { viewModel.onAdjuntarArchivoSimulado() },
                    onEliminarClick = { viewModel.onEliminarArchivo() }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Sección: Observaciones Médicas Rápidas
                ObservacionesSection(
                    observaciones = uiState.observaciones,
                    maxChars = uiState.maxCaracteresObservaciones,
                    onTextChanged = { viewModel.onObservacionesChanged(it) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Banner: Trazabilidad Médica OftApp
                TrazabilidadBanner(
                    firma = uiState.firmaProfesional
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Botón Principal: Guardar Examen
                Button(
                    onClick = { viewModel.onGuardarExamen() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OftAppTealPrimary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Guardar",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Guardar Examen",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón Secundario: Descartar borrador
                TextButton(
                    onClick = { viewModel.onSolicitarDescartar() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Descartar borrador",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OftAppTealPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Encabezado de marca superior (OFTAPP CLINICAL - Registro).
 */
@Composable
private fun TopAppBarContent(
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de Ojo y Logo
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = "OftApp Logo",
                tint = OftAppTealPrimary,
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = "OFTAPP CLINICAL",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = OftAppTealPrimary,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Registro",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OftAppTextPrimary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Notificaciones
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notificaciones",
                    tint = OftAppTextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Avatar de Perfil
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(OftAppTealDark)
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Subcabecera de navegación: Triage Oftalmológico / Registrar Examen.
 */
@Composable
private fun TriageSubHeader(
    onBackClick: () -> Unit,
    onQuickSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = OftAppTextPrimary
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TRIAGE OFTALMOLÓGICO",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF007A99),
                    letterSpacing = 0.6.sp
                )
                Text(
                    text = "Registrar Examen",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = OftAppTextPrimary
                )
            }

            IconButton(onClick = onQuickSaveClick) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "Guardar",
                    tint = Color(0xFF007A99),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Línea acento inferior decorativa
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.5.dp)
                .background(Color(0xFF0284C7).copy(alpha = 0.35f))
        )
    }
}

/**
 * Ficha clínica digital con datos del paciente.
 */
@Composable
private fun FichaClinicaCard(
    nombre: String,
    rut: String,
    edad: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = OftAppFichaBlue,
        border = BorderStroke(1.dp, OftAppBorderSubtle)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de Credencial
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(OftAppFichaIconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Badge,
                    contentDescription = "Ficha",
                    tint = Color(0xFF006699),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "FICHA CLÍNICA DIGITAL",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = OftAppTextMuted,
                    letterSpacing = 0.4.sp
                )
                Text(
                    text = nombre,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = OftAppTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "RUT: $rut • Edad: $edad años",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = OftAppAccentBlue
                )
            }

            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Validado",
                tint = OftAppAccentBlue,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Campo RUT / ID Paciente.
 */
@Composable
private fun RutPacienteField(
    rutTexto: String,
    onSearchClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "RUT / ID Paciente",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = OftAppTextSecondary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.dp, OftAppBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = OftAppTealPrimary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = rutTexto,
                    fontSize = 13.5.sp,
                    color = OftAppTextPrimary,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = OftAppTealPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Paciente validado en Registro Civil y Fonasa",
            fontSize = 11.5.sp,
            color = OftAppTextMuted
        )
    }
}

/**
 * Campo Selector Tipo de Examen con Dropdown.
 */
@Composable
private fun TipoExamenField(
    tipoExamen: String,
    protocolo: String,
    isExpanded: Boolean,
    opciones: List<Pair<String, String>>,
    onToggleDropdown: (Boolean) -> Unit,
    onSelect: (String, String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Tipo de Examen",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = OftAppTextSecondary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { onToggleDropdown(!isExpanded) },
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, OftAppBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = OftAppTealPrimary,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = tipoExamen,
                        fontSize = 13.5.sp,
                        color = OftAppTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "▾",
                        fontSize = 16.sp,
                        color = OftAppTextMuted
                    )
                }
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onToggleDropdown(false) },
                modifier = Modifier.background(Color.White)
            ) {
                opciones.forEach { (tipo, prot) ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = tipo,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.5.sp,
                                    color = OftAppTextPrimary
                                )
                                Text(
                                    text = prot,
                                    fontSize = 11.5.sp,
                                    color = OftAppAccentBlue
                                )
                            }
                        },
                        onClick = { onSelect(tipo, prot) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = protocolo,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Medium,
            color = OftAppAccentBlue
        )
    }
}

/**
 * Campo Selector Sucursal / Box Clínico.
 */
@Composable
private fun SucursalBoxField(
    sucursal: String,
    isExpanded: Boolean,
    opciones: List<String>,
    onToggleDropdown: (Boolean) -> Unit,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Sucursal / Box Clínico",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = OftAppTextSecondary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { onToggleDropdown(!isExpanded) },
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, OftAppBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Apartment,
                        contentDescription = null,
                        tint = OftAppTealPrimary,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = sucursal,
                        fontSize = 13.5.sp,
                        color = OftAppTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "▾",
                        fontSize = 16.sp,
                        color = OftAppTextMuted
                    )
                }
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onToggleDropdown(false) },
                modifier = Modifier.background(Color.White)
            ) {
                opciones.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                fontSize = 13.5.sp,
                                color = OftAppTextPrimary
                            )
                        },
                        onClick = { onSelect(item) }
                    )
                }
            }
        }
    }
}

/**
 * Selección de Ojo Evaluado (Lateralidad OD / OI / AO).
 */
@Composable
private fun LateralidadSection(
    ojoSeleccionado: OjoEvaluado,
    onSelectOjo: (OjoEvaluado) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Selección de Ojo Evaluado",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = OftAppTextSecondary
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(OftAppTealContainer)
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "Lateralidad",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = OftAppTealPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OjoEvaluado.entries.forEach { ojo ->
            val isSelected = ojo == ojoSeleccionado
            val cardBg = if (isSelected) Color(0xFFE9F5FA) else Color(0xFFF8FAFC)
            val borderCol = if (isSelected) OftAppTealPrimary else OftAppBorder
            val badgeBg = if (isSelected) OftAppTealPrimary else Color(0xFFE2EBF4)
            val badgeTextCol = if (isSelected) Color.White else Color(0xFF475569)

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onSelectOjo(ojo) },
                shape = RoundedCornerShape(12.dp),
                color = cardBg,
                border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderCol)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge con código (OD, OI, AO)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(badgeBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ojo.codigo,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeTextCol
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = ojo.titulo,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) OftAppTextPrimary else Color(0xFF334155)
                        )
                        Text(
                            text = ojo.detalle,
                            fontSize = 12.sp,
                            color = OftAppTextMuted
                        )
                    }

                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectOjo(ojo) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = OftAppTealPrimary,
                            unselectedColor = Color(0xFFCBD5E1)
                        )
                    )
                }
            }
        }
    }
}

/**
 * Adjuntar Archivo / Resultado con soporte DICOM / PDF.
 */
@Composable
private fun AdjuntarArchivoSection(
    archivoAdjunto: DocumentoAdjunto?,
    onAdjuntarClick: () -> Unit,
    onEliminarClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Adjuntar Archivo / Resultado",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = OftAppTextSecondary
            )

            Text(
                text = "DICOM / PDF",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = OftAppTextMuted
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Contenedor de subida de archivos
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFB9D8F5))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0F2FE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "Subir",
                        tint = OftAppAccentBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Adjuntar PDF / Imagen simulada",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = OftAppTextPrimary
                )

                Text(
                    text = "Formatos DICOM, PDF, JPG (Máx. 25MB)",
                    fontSize = 11.5.sp,
                    color = OftAppTextMuted
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onAdjuntarClick,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0F2FE),
                        contentColor = OftAppTealPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Explorar Almacenamiento",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Ficha del archivo cargado (si existe)
        archivoAdjunto?.let { doc ->
            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF1F6FB),
                border = BorderStroke(1.dp, Color(0xFFD6E4F0))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = "Archivo",
                        tint = OftAppTealPrimary,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = doc.nombreArchivo,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = OftAppTextPrimary
                        )
                        Text(
                            text = "${doc.tamano} • ${doc.tiempoSubida}",
                            fontSize = 11.sp,
                            color = OftAppTextMuted
                        )
                    }

                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Subido",
                        tint = OftAppAccentBlue,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = onEliminarClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Quitar",
                            tint = OftAppTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Sección de Observaciones Médicas Rápidas con contador de caracteres.
 */
@Composable
private fun ObservacionesSection(
    observaciones: String,
    maxChars: Int,
    onTextChanged: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Observaciones Médicas Rápidas",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = OftAppTextSecondary
            )

            Text(
                text = "${observaciones.length}/$maxChars",
                fontSize = 12.sp,
                color = OftAppTextMuted
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = observaciones,
            onValueChange = onTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            placeholder = {
                Text(
                    text = "Indicar hallazgos preliminares o indicaciones al tecnólogo...",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8)
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OftAppTealPrimary,
                unfocusedBorderColor = OftAppBorder,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = OftAppTextMuted,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "Visibles en informe al paciente y derivación",
                fontSize = 11.5.sp,
                color = OftAppTextMuted
            )
        }
    }
}

/**
 * Banner de trazabilidad médica con firma electrónica activa.
 */
@Composable
private fun TrazabilidadBanner(firma: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFE8F1FC),
        border = BorderStroke(1.dp, Color(0xFFD6E4F0))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(OftAppShieldBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Trazabilidad",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Trazabilidad Médica OftApp",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = OftAppTealDark
                )
                Text(
                    text = "Firma electrónica avanzada activa: $firma",
                    fontSize = 11.5.sp,
                    color = OftAppTextSecondary
                )
            }
        }
    }
}

/**
 * Barra inferior de navegación con 4 pestañas: Dashboard, Atenciones, Registro, Historial.
 */
@Composable
private fun OftAppBottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val items = listOf<Triple<String, ImageVector, String>>(
        Triple("Dashboard", Icons.Default.GridView, "Dashboard"),
        Triple("Atenciones", Icons.AutoMirrored.Filled.Assignment, "Atenciones"),
        Triple("Registro", Icons.Default.AddBox, "Registro"),
        Triple("Historial", Icons.Default.History, "Historial")
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 6.dp
    ) {
        items.forEach { (nombre, icono, etiqueta) ->
            val isSelected = selectedTab == nombre

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(nombre) },
                icon = {
                    Icon(
                        imageVector = icono,
                        contentDescription = etiqueta,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = etiqueta,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OftAppTealPrimary,
                    selectedTextColor = OftAppTealPrimary,
                    indicatorColor = OftAppNavIndicator,
                    unselectedIconColor = OftAppTextMuted,
                    unselectedTextColor = OftAppTextMuted
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExamRegisterScreenPreview() {
    OftappTheme {
        ExamRegisterScreen()
    }
}
