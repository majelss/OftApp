package com.example.oftapp.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oftapp.viewmodel.AdminUserViewModel

private data class RolOption(
    val key: String,
    val label: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserFormScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateTab: (AdminTab) -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: AdminUserViewModel = viewModel(
        factory = AdminUserViewModel.factory(context)
    )
    val state by viewModel.uiState.collectAsState()

    var menuUsuariosExpandido by remember { mutableStateOf(false) }
    var especialidadExpandida by remember { mutableStateOf(false) }
    var sucursalAdminExpandida by remember { mutableStateOf(false) }
    var previsionExpandida by remember { mutableStateOf(false) }

    val roles = remember {
        listOf(
            RolOption("paciente", "Paciente", Icons.Default.PersonalInjury),
            RolOption("medico", "Médico Oftalmólogo", Icons.Default.MedicalServices),
            RolOption("tecnologo", "Tecnólogo Médico", Icons.Default.Memory),
            RolOption("administrativo", "Administrativo", Icons.Default.PointOfSale),
            RolOption("administrador", "Administrador", Icons.Default.AdminPanelSettings)
        )
    }

    Scaffold(
        containerColor = ClinicalClarityColors.Surface,
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = ClinicalClarityColors.SurfaceLowest,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(64.dp)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = ClinicalClarityColors.OnSurface
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(11.dp))
                                .background(ClinicalClarityColors.SurfaceHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = ClinicalClarityColors.Primary,
                                modifier = Modifier.size(21.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "OFTAPP ADMIN",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = ClinicalClarityColors.Primary,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Detalle De Usuario",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = ClinicalClarityColors.OnSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box {
                            IconButton(
                                onClick = { menuUsuariosExpandido = true },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Seleccionar u opciones de usuario",
                                    tint = ClinicalClarityColors.OnSurfaceVariant
                                )
                            }
                            DropdownMenu(
                                expanded = menuUsuariosExpandido,
                                onDismissRequest = { menuUsuariosExpandido = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Crear Nuevo Usuario",
                                            fontWeight = FontWeight.Bold,
                                            color = ClinicalClarityColors.Primary
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.PersonAdd,
                                            contentDescription = null,
                                            tint = ClinicalClarityColors.Primary
                                        )
                                    },
                                    onClick = {
                                        menuUsuariosExpandido = false
                                        if (state.modoEdicion) viewModel.alternarModoCreacionEdicion()
                                    }
                                )
                                if (state.usuariosRegistrados.isNotEmpty()) {
                                    HorizontalDivider()
                                    state.usuariosRegistrados.forEach { usr ->
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(usr.nombreCompleto, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                                    Text("${usr.id} • ${usr.rut}", fontSize = 11.sp, color = ClinicalClarityColors.Outline)
                                                }
                                            },
                                            onClick = {
                                                menuUsuariosExpandido = false
                                                viewModel.seleccionarUsuarioExistente(usr)
                                            }
                                        )
                                    }
                                }
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Cerrar Sesión Admin",
                                            color = ClinicalClarityColors.Error,
                                            fontWeight = FontWeight.Bold
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.AutoMirrored.Filled.Logout,
                                            contentDescription = null,
                                            tint = ClinicalClarityColors.Error
                                        )
                                    },
                                    onClick = {
                                        menuUsuariosExpandido = false
                                        onNavigateBack()
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            onClick = onNavigateBack,
                            shape = RoundedCornerShape(50),
                            color = ClinicalClarityColors.ErrorContainer,
                            border = BorderStroke(1.dp, ClinicalClarityColors.Error.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = "Cerrar Sesión",
                                    tint = ClinicalClarityColors.Error,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Salir",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ClinicalClarityColors.Error
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Barra de acciones fijas del formulario (Suspender / Guardar Usuario)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = ClinicalClarityColors.SurfaceLowest,
                    shadowElevation = 6.dp,
                    border = BorderStroke(1.dp, ClinicalClarityColors.CardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.suspenderUsuario() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ClinicalClarityColors.SurfaceContainer,
                                contentColor = ClinicalClarityColors.Error
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Block,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Suspender",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                        }

                        Button(
                            onClick = { viewModel.guardarUsuario() },
                            modifier = Modifier
                                .weight(1.75f)
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ClinicalClarityColors.Primary,
                                contentColor = ClinicalClarityColors.OnPrimary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Guardar Usuario",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                        }
                    }
                }

                // Barra de navegación inferior del Módulo Admin
                AdminBottomNavBar(
                    selectedTab = AdminTab.USUARIOS,
                    onSelectTab = onNavigateTab
                )
            }
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
                // 1. Banner Contextual: Modo Edición / Creación
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = ClinicalClarityColors.SurfaceLow,
                    border = BorderStroke(1.dp, ClinicalClarityColors.SurfaceHighest)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(ClinicalClarityColors.PrimaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ManageAccounts,
                                    contentDescription = null,
                                    tint = ClinicalClarityColors.OnPrimaryContainer,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (state.modoEdicion) "MODO EDICIÓN" else "MODO CREACIÓN",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ClinicalClarityColors.Primary,
                                        letterSpacing = 0.5.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = ClinicalClarityColors.SecondaryContainer,
                                        shape = RoundedCornerShape(50)
                                    ) {
                                        Text(
                                            text = "Admin SIS",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ClinicalClarityColors.OnSecondaryContainer,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (state.nombreCompleto.isNotBlank()) state.nombreCompleto else "Crear Nuevo Registro Clínico",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ClinicalClarityColors.OnSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "ID: ${state.usuarioId} • ${state.ultimaModificacion}",
                                    fontSize = 13.sp,
                                    color = ClinicalClarityColors.OnSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            onClick = { viewModel.alternarModoCreacionEdicion() },
                            shape = RoundedCornerShape(10.dp),
                            color = ClinicalClarityColors.SurfaceHighest
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (state.modoEdicion) Icons.Default.PersonAdd else Icons.Default.Edit,
                                    contentDescription = if (state.modoEdicion) "Nuevo usuario" else "Volver a edición",
                                    tint = ClinicalClarityColors.Primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Banner de error de validación (si aplica)
                if (state.mensajeError != null) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = ClinicalClarityColors.ErrorContainer,
                        border = BorderStroke(1.dp, ClinicalClarityColors.Error)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = ClinicalClarityColors.OnErrorContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = state.mensajeError!!,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = ClinicalClarityColors.OnErrorContainer
                            )
                        }
                    }
                }

                // 2. Sección: Perfil y Rol Asignado
                AdminSectionCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = ClinicalClarityColors.Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Perfil y Rol Asignado",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ClinicalClarityColors.OnSurface
                            )
                        }
                        Surface(
                            color = ClinicalClarityColors.SurfaceContainer,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "Requerido",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = ClinicalClarityColors.PrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Seleccione el rol para cargar los permisos y protocolos clínicos pertinentes.",
                        fontSize = 13.5.sp,
                        color = ClinicalClarityColors.OnSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Cuadrícula visible con todos los 5 roles sin ocultar ninguno fuera de pantalla
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        roles.chunked(2).forEach { filaRoles ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                filaRoles.forEach { rol ->
                                    val isSelected = state.rolSeleccionado == rol.key
                                    Surface(
                                        onClick = { viewModel.onRolChange(rol.key) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) ClinicalClarityColors.Primary else ClinicalClarityColors.SurfaceLow,
                                        border = BorderStroke(
                                            1.dp,
                                            if (isSelected) ClinicalClarityColors.Primary else ClinicalClarityColors.CardBorder
                                        ),
                                        shadowElevation = if (isSelected) 2.dp else 0.dp
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .height(48.dp)
                                                .padding(horizontal = 12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = rol.icon,
                                                contentDescription = null,
                                                tint = if (isSelected) ClinicalClarityColors.OnPrimary else ClinicalClarityColors.OnSurface,
                                                modifier = Modifier.size(19.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = rol.label,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (isSelected) ClinicalClarityColors.OnPrimary else ClinicalClarityColors.OnSurface,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Sección: Datos Personales y Acceso
                AdminSectionCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AssignmentInd,
                            contentDescription = null,
                            tint = ClinicalClarityColors.Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Datos Personales y Acceso",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ClinicalClarityColors.OnSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Nombre y Apellidos
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RequiredFieldLabel("Nombre y Apellidos")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = ClinicalClarityColors.Primary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Registrado",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ClinicalClarityColors.Primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    AdminTextField(
                        value = state.nombreCompleto,
                        onValueChange = viewModel::onNombreChange,
                        placeholder = "Ej. Dra. Sofía Valenzuela Morales",
                        leadingIcon = Icons.Default.AccountCircle
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // RUT / Cédula de Identidad
                    RequiredFieldLabel("RUT / Cédula de Identidad")
                    Spacer(modifier = Modifier.height(6.dp))
                    val rutValido = validarFormatoRutChileno(state.rut)
                    AdminTextField(
                        value = state.rut,
                        onValueChange = viewModel::onRutChange,
                        placeholder = "15.842.109-K",
                        leadingIcon = Icons.Default.Badge,
                        trailingIcon = {
                            if (rutValido) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircleOutline,
                                    contentDescription = "RUT Validado",
                                    tint = ClinicalClarityColors.Secondary
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Formato chileno con puntos y guion verificador.",
                        fontSize = 12.5.sp,
                        color = ClinicalClarityColors.OnSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Correo Electrónico Institucional
                    RequiredFieldLabel("Correo Electrónico Institucional")
                    Spacer(modifier = Modifier.height(6.dp))
                    AdminTextField(
                        value = state.email,
                        onValueChange = viewModel::onEmailChange,
                        placeholder = "usuario@oftalmolaser.cl",
                        leadingIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Teléfono de Contacto Clínico (WhatsApp)
                    Text(
                        text = "Teléfono de Contacto Clínico (WhatsApp)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ClinicalClarityColors.OnSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    AdminTextField(
                        value = state.telefono,
                        onValueChange = viewModel::onTelefonoChange,
                        placeholder = "+56 9 8765 4321",
                        leadingIcon = Icons.Default.PhoneIphone,
                        keyboardType = KeyboardType.Phone
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bloque Credenciales de Acceso
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = ClinicalClarityColors.SurfaceLow
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
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
                                        imageVector = Icons.Default.LockReset,
                                        contentDescription = null,
                                        tint = ClinicalClarityColors.Primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Credenciales de Acceso",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ClinicalClarityColors.OnSurface
                                    )
                                }

                                Surface(
                                    onClick = { viewModel.restablecerClave() },
                                    shape = RoundedCornerShape(10.dp),
                                    color = ClinicalClarityColors.SurfaceHighest
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VpnKey,
                                            contentDescription = null,
                                            tint = ClinicalClarityColors.Primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Restablecer Clave",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ClinicalClarityColors.Primary
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.onExigirCambioClaveChange(!state.exigirCambioClave) }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                    Text(
                                        text = "Exigir cambio de clave",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ClinicalClarityColors.OnSurface
                                    )
                                    Text(
                                        text = "Obliga al usuario a renovar contraseña en su primer inicio",
                                        fontSize = 12.5.sp,
                                        color = ClinicalClarityColors.OnSurfaceVariant
                                    )
                                }
                                Checkbox(
                                    checked = state.exigirCambioClave,
                                    onCheckedChange = viewModel::onExigirCambioClaveChange,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = ClinicalClarityColors.Primary
                                    )
                                )
                            }
                        }
                    }
                }

                // 4. Sección Dinámica según Rol Seleccionado
                when (state.rolSeleccionado) {
                    "medico" -> {
                        AdminSectionCard {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.MedicalServices,
                                        contentDescription = null,
                                        tint = ClinicalClarityColors.Primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Atributos Clínicos • Médico",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ClinicalClarityColors.OnSurface
                                    )
                                }
                                Surface(
                                    color = ClinicalClarityColors.SurfaceContainer,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = "OD / OS",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ClinicalClarityColors.Primary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            RequiredFieldLabel("N° Registro Superintendencia de Salud / Colmed")
                            Spacer(modifier = Modifier.height(6.dp))
                            AdminTextField(
                                value = state.registroSis,
                                onValueChange = viewModel::onRegistroSisChange,
                                placeholder = "SIS-78942-CL",
                                leadingIcon = Icons.Default.VerifiedUser
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Especialidad Principal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ClinicalClarityColors.OnSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            ExposedDropdownMenuBox(
                                expanded = especialidadExpandida,
                                onExpandedChange = { especialidadExpandida = !especialidadExpandida }
                            ) {
                                OutlinedTextField(
                                    value = state.especialidad,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = ClinicalClarityColors.OnSurfaceVariant
                                        )
                                    },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = especialidadExpandida)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = ClinicalClarityColors.SurfaceLowest,
                                        unfocusedContainerColor = ClinicalClarityColors.SurfaceLowest,
                                        focusedBorderColor = ClinicalClarityColors.Primary,
                                        unfocusedBorderColor = ClinicalClarityColors.CardBorder
                                    ),
                                    singleLine = true
                                )
                                ExposedDropdownMenu(
                                    expanded = especialidadExpandida,
                                    onDismissRequest = { especialidadExpandida = false }
                                ) {
                                    viewModel.especialidadesCatalogo.forEach { esp ->
                                        DropdownMenuItem(
                                            text = { Text(esp) },
                                            onClick = {
                                                viewModel.onEspecialidadChange(esp)
                                                especialidadExpandida = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Box / Pabellón Habitual",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ClinicalClarityColors.OnSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            AdminTextField(
                                value = state.boxHabitual,
                                onValueChange = viewModel::onBoxHabitualChange,
                                placeholder = "Box 04 - Torre Quirúrgica Oftalmo",
                                leadingIcon = Icons.Default.MeetingRoom
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Tarjeta Firma Electrónica Avanzada (FEA)
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
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(ClinicalClarityColors.SecondaryContainer),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Draw,
                                                contentDescription = null,
                                                tint = ClinicalClarityColors.OnSecondaryContainer,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "Firma Electrónica Avanzada (FEA)",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = ClinicalClarityColors.OnSurface
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "✓ Certificado Token Activo",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ClinicalClarityColors.Secondary
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Certificado activo",
                                        tint = ClinicalClarityColors.Primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }

                    "tecnologo" -> {
                        AdminSectionCard {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Memory,
                                    contentDescription = null,
                                    tint = ClinicalClarityColors.Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Atributos • Tecnólogo Médico",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ClinicalClarityColors.OnSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Mención",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ClinicalClarityColors.OnSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = state.mencionTecnologo,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = ClinicalClarityColors.SurfaceLow,
                                    unfocusedContainerColor = ClinicalClarityColors.SurfaceLow,
                                    unfocusedBorderColor = ClinicalClarityColors.CardBorder
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Equipos Clínicos Autorizados",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ClinicalClarityColors.OnSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            viewModel.equiposCatalogo.chunked(2).forEach { fila ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    fila.forEach { eq ->
                                        val checked = state.equiposAutorizados.contains(eq)
                                        Surface(
                                            onClick = { viewModel.onToggleEquipoTecnologo(eq) },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(10.dp),
                                            color = ClinicalClarityColors.SurfaceLow
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Checkbox(
                                                    checked = checked,
                                                    onCheckedChange = { viewModel.onToggleEquipoTecnologo(eq) },
                                                    colors = CheckboxDefaults.colors(checkedColor = ClinicalClarityColors.Primary)
                                                )
                                                Text(
                                                    text = eq,
                                                    fontSize = 12.5.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = ClinicalClarityColors.OnSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "administrativo" -> {
                        AdminSectionCard {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.PointOfSale,
                                    contentDescription = null,
                                    tint = ClinicalClarityColors.Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Atributos • Administrativo / Admisión",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ClinicalClarityColors.OnSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Sucursal Asignada",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ClinicalClarityColors.OnSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            ExposedDropdownMenuBox(
                                expanded = sucursalAdminExpandida,
                                onExpandedChange = { sucursalAdminExpandida = !sucursalAdminExpandida }
                            ) {
                                OutlinedTextField(
                                    value = state.sucursalAdministrativo,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = sucursalAdminExpandida)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = ClinicalClarityColors.SurfaceLowest,
                                        unfocusedContainerColor = ClinicalClarityColors.SurfaceLowest,
                                        focusedBorderColor = ClinicalClarityColors.Primary,
                                        unfocusedBorderColor = ClinicalClarityColors.CardBorder
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = sucursalAdminExpandida,
                                    onDismissRequest = { sucursalAdminExpandida = false }
                                ) {
                                    viewModel.sucursalesAdminCatalogo.forEach { suc ->
                                        DropdownMenuItem(
                                            text = { Text(suc) },
                                            onClick = {
                                                viewModel.onSucursalAdministrativoChange(suc)
                                                sucursalAdminExpandida = false
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Ventanilla / Módulo",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ClinicalClarityColors.OnSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            AdminTextField(
                                value = state.ventanillaAdministrativo,
                                onValueChange = viewModel::onVentanillaChange,
                                placeholder = "Recepción Piso 2 - Exámenes",
                                leadingIcon = Icons.Default.Storefront
                            )
                        }
                    }

                    "paciente" -> {
                        AdminSectionCard {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.PersonalInjury,
                                    contentDescription = null,
                                    tint = ClinicalClarityColors.Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Atributos • Expediente del Paciente",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ClinicalClarityColors.OnSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Ficha Clínica Asignada",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ClinicalClarityColors.OnSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = state.fichaPaciente,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = ClinicalClarityColors.SurfaceLow,
                                    unfocusedContainerColor = ClinicalClarityColors.SurfaceLow,
                                    unfocusedBorderColor = ClinicalClarityColors.CardBorder
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Previsión de Salud",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ClinicalClarityColors.OnSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            ExposedDropdownMenuBox(
                                expanded = previsionExpandida,
                                onExpandedChange = { previsionExpandida = !previsionExpandida }
                            ) {
                                OutlinedTextField(
                                    value = state.previsionPaciente,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = previsionExpandida)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = ClinicalClarityColors.SurfaceLowest,
                                        unfocusedContainerColor = ClinicalClarityColors.SurfaceLowest,
                                        focusedBorderColor = ClinicalClarityColors.Primary,
                                        unfocusedBorderColor = ClinicalClarityColors.CardBorder
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = previsionExpandida,
                                    onDismissRequest = { previsionExpandida = false }
                                ) {
                                    viewModel.previsionesCatalogo.forEach { prev ->
                                        DropdownMenuItem(
                                            text = { Text(prev) },
                                            onClick = {
                                                viewModel.onPrevisionChange(prev)
                                                previsionExpandida = false
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = ClinicalClarityColors.SurfaceLow
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.onModoBajaVisionChange(!state.modoBajaVision) }
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                        Text(
                                            text = "Modo Visual Accesible (Baja Visión)",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = ClinicalClarityColors.OnSurface
                                        )
                                        Text(
                                            text = "Contraste reforzado e iconografía de 32dp por defecto",
                                            fontSize = 12.5.sp,
                                            color = ClinicalClarityColors.OnSurfaceVariant
                                        )
                                    }
                                    Checkbox(
                                        checked = state.modoBajaVision,
                                        onCheckedChange = viewModel::onModoBajaVisionChange,
                                        colors = CheckboxDefaults.colors(checkedColor = ClinicalClarityColors.Primary)
                                    )
                                }
                            }
                        }
                    }

                    else -> {
                        AdminSectionCard {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = ClinicalClarityColors.Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Atributos • Super Administrador",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ClinicalClarityColors.OnSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Acceso total a auditorías, borrado lógico de fichas médicas y gestión de llaves criptográficas DICOM/PACS.",
                                fontSize = 13.5.sp,
                                color = ClinicalClarityColors.OnSurfaceVariant
                            )
                        }
                    }
                }

                // 5. Sección: Estado y Permisos Especiales
                AdminSectionCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = ClinicalClarityColors.Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Estado y Permisos Especiales",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ClinicalClarityColors.OnSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Switch de Estado de la Cuenta
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = ClinicalClarityColors.SurfaceLow
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (state.cuentaActiva) ClinicalClarityColors.SecondaryContainer
                                            else ClinicalClarityColors.ErrorContainer
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (state.cuentaActiva) Icons.Default.CheckCircleOutline else Icons.Default.Cancel,
                                        contentDescription = null,
                                        tint = if (state.cuentaActiva) ClinicalClarityColors.OnSecondaryContainer else ClinicalClarityColors.OnErrorContainer,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Estado de la Cuenta",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ClinicalClarityColors.OnSurface
                                    )
                                    Text(
                                        text = if (state.cuentaActiva) "Cuenta Activa y Habilitada" else "Cuenta Inactiva / Suspendida",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (state.cuentaActiva) ClinicalClarityColors.Secondary else ClinicalClarityColors.Error
                                    )
                                }
                            }

                            Switch(
                                checked = state.cuentaActiva,
                                onCheckedChange = viewModel::onCuentaActivaChange,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = ClinicalClarityColors.Primary,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = ClinicalClarityColors.SurfaceHighest
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "AUTORIZACIONES CLÍNICAS ESPECÍFICAS",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = ClinicalClarityColors.OnSurfaceVariant,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    PermissionCheckboxRow(
                        icon = Icons.Default.HistoryEdu,
                        title = "Acceso a Registros Históricos",
                        subtitle = "Consultar exámenes previos a más de 5 años",
                        checked = state.permisoHistoricos,
                        onCheckedChange = viewModel::onPermisoHistoricosChange
                    )

                    PermissionCheckboxRow(
                        icon = Icons.Default.CloudDownload,
                        title = "Autorización de Exportación Masiva",
                        subtitle = "Descarga de tomografías y reportes en lote ZIP",
                        checked = state.permisoExportacionMasiva,
                        onCheckedChange = viewModel::onPermisoExportacionChange
                    )

                    PermissionCheckboxRow(
                        icon = Icons.Default.VpnLock,
                        title = "Acceso Remoto Fuera de Red Clínica",
                        subtitle = "Requiere validación 2FA por SMS o App Token",
                        checked = state.permisoAccesoRemoto,
                        onCheckedChange = viewModel::onPermisoAccesoRemotoChange
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Toast flotante superior
            AdminToastBanner(
                visible = state.toastVisible,
                title = state.toastMensaje,
                badgeRight = "SIS Sincronizado",
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun AdminSectionCard(
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
            content = content
        )
    }
}

@Composable
private fun RequiredFieldLabel(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ClinicalClarityColors.OnSurface
        )
        Text(
            text = " *",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = ClinicalClarityColors.Error
        )
    }
}

@Composable
private fun AdminTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                color = ClinicalClarityColors.Outline,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = ClinicalClarityColors.OnSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = ClinicalClarityColors.SurfaceLowest,
            unfocusedContainerColor = ClinicalClarityColors.SurfaceLowest,
            focusedBorderColor = ClinicalClarityColors.Primary,
            unfocusedBorderColor = ClinicalClarityColors.CardBorder
        ),
        singleLine = true
    )
}

@Composable
private fun PermissionCheckboxRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        onClick = { onCheckedChange(!checked) },
        shape = RoundedCornerShape(10.dp),
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ClinicalClarityColors.OnSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
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
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = ClinicalClarityColors.Primary
                )
            )
        }
    }
}
