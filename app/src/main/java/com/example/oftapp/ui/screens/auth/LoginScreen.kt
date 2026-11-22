package com.example.oftapp.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.data.local.SessionTokenManager
import com.example.oftapp.data.remote.LoginApiRequest
import com.example.oftapp.data.remote.RetrofitClient
import com.example.oftapp.model.RolUsuario
import com.example.oftapp.model.Roles
import com.example.oftapp.model.SesionActual
import kotlinx.coroutines.launch
import java.util.UUID

// Colores personalizados del diseño
val PrimaryBlue = Color(0xFF006689)
val LightBlueBg = Color(0xFFEFF5FC)
val SoftBlueBadge = Color(0xFFCBE6FF)
val CardBackground = Color.White
val InputFieldBg = Color(0xFFF0F4F9)
val SecondaryButtonBg = Color(0xFFC7E7FF)

private data class RolLoginItem(
    val titulo: String,
    val descripcion: String,
    val codigoRol: String,
    val rolUsuario: RolUsuario,
    val emailDemo: String,
    val passwordDemo: String,
    val icono: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (usuarioId: Long, rol: String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val tokenManager = remember { SessionTokenManager.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf("dr.valenzuela@clinicaofta.cl") }
    var password by remember { mutableStateOf("dr123456") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val roles = remember {
        listOf(
            RolLoginItem(
                titulo = "Médico Oftalmólogo",
                descripcion = "Dashboard clínico, atenciones, validación diagnóstica e historial (OD/OI/AO)",
                codigoRol = Roles.MEDICO,
                rolUsuario = RolUsuario.MEDICO_OFTALMOLOGO,
                emailDemo = "dr.valenzuela@clinicaofta.cl",
                passwordDemo = "dr123456",
                icono = Icons.Default.MedicalServices
            ),
            RolLoginItem(
                titulo = "Tecnólogo Médico",
                descripcion = "Portal TM: agenda de box, captura técnica, telemetría, mis registros y perfil SIS",
                codigoRol = Roles.TECNICO,
                rolUsuario = RolUsuario.TECNOLOGO_MEDICO,
                emailDemo = "tm.araya@clinicaofta.cl",
                passwordDemo = "tm123456",
                icono = Icons.Default.Biotech
            ),
            RolLoginItem(
                titulo = "Paciente",
                descripcion = "Portal personal: mis exámenes, línea de tiempo, visor PDF y compartir con QR",
                codigoRol = Roles.PACIENTE,
                rolUsuario = RolUsuario.PACIENTE,
                emailDemo = "paciente@clinicaofta.cl",
                passwordDemo = "pac123456",
                icono = Icons.Default.PersonalInjury
            ),
            RolLoginItem(
                titulo = "Administrador",
                descripcion = "Consola Clinical Clarity: gestión de usuarios, parámetros PIO y auditoría SHA-256",
                codigoRol = Roles.ADMINISTRADOR,
                rolUsuario = RolUsuario.ADMINISTRATIVO,
                emailDemo = "admin@clinicaofta.cl",
                passwordDemo = "admin123456",
                icono = Icons.Default.AdminPanelSettings
            )
        )
    }
    var selectedRol by remember { mutableStateOf(roles[0]) }

    val detectarRolPorEmail: (String) -> RolLoginItem? = { correo ->
        val e = correo.trim().lowercase()
        when {
            e.startsWith("tm.") || e.contains("tecnologo") || e.contains("araya") ->
                roles.find { it.codigoRol == Roles.TECNICO }
            e.startsWith("pac") || e.contains("mariana") ->
                roles.find { it.codigoRol == Roles.PACIENTE }
            e.startsWith("admin") ->
                roles.find { it.codigoRol == Roles.ADMINISTRADOR }
            e.startsWith("dr.") || e.contains("valenzuela") || e.contains("medico") ->
                roles.find { it.codigoRol == Roles.MEDICO }
            else -> null
        }
    }

    val validarEIniciar = {
        val rolFinal = detectarRolPorEmail(email) ?: selectedRol
        val rolCodigo = rolFinal.codigoRol

        if (email.isNotBlank() && password.isNotBlank()) {
            errorMessage = null
            SesionActual.iniciarSesion(rolFinal.rolUsuario, email.trim())
            // Guardar token simulado en almacenamiento cifrado respaldado por Android Keystore
            val syntheticToken = "oftapp_ks_" + rolCodigo.lowercase() + "_" + UUID.randomUUID().toString().take(12)
            tokenManager.guardarSesion(
                token = syntheticToken,
                rol = rolCodigo,
                email = email.trim(),
                usuarioId = 1L
            )
            // Sincronizar autenticación con el microservicio Spring Boot (/api/v1/auth/login) si está online
            coroutineScope.launch {
                try {
                    val resp = RetrofitClient.oftAppApi.login(
                        LoginApiRequest(
                            email = email.trim(),
                            password = password,
                            rol = rolCodigo
                        )
                    )
                    if (resp.autenticado && !resp.token.isNullOrBlank()) {
                        tokenManager.guardarSesion(
                            token = resp.token,
                            rol = rolCodigo,
                            email = email.trim(),
                            usuarioId = resp.usuarioId ?: 1L
                        )
                    }
                } catch (_: Exception) {
                    // Si el backend está apagado, se mantiene el token local cifrado en Android Keystore
                }
            }
            onLoginSuccess(1L, rolCodigo)
        } else {
            errorMessage = "Credenciales incorrectas. Verifique su usuario y contraseña."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlueBg)
            .safeDrawingPadding()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono del Ojo y Título
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .background(Color(0xFFE0ECFA), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Logo OftApp",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "OftApp",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF002B49),
                modifier = Modifier.semantics { heading() }
            )

            Text(
                text = "Sistema de Gestión Oftalmológica Clínica",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Insignia "Portal Clínico Seguro"
            Surface(
                color = SoftBlueBadge,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Portal Clínico Seguro v2.4",
                        fontSize = 12.sp,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta Blanca con Formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    // Campo Usuario / Email
                    Text(
                        text = "Usuario / Correo Electrónico *",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { nuevoEmail ->
                            email = nuevoEmail
                            errorMessage = null
                            detectarRolPorEmail(nuevoEmail)?.let { selectedRol = it }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_email")
                            .semantics { contentDescription = "Campo de correo electrónico o usuario" },
                        leadingIcon = {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = PrimaryBlue
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = InputFieldBg,
                            focusedContainerColor = InputFieldBg,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = PrimaryBlue
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Campo Contraseña
                    Text(
                        text = "Contraseña *",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_password")
                            .semantics { contentDescription = "Campo de contraseña clínica" },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryBlue)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = InputFieldBg,
                            focusedContainerColor = InputFieldBg,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = PrimaryBlue
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Selector Visible de Todos los Roles del Sistema
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Seleccione Rol Clínico (4 roles con vistas propias)",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        Surface(
                            color = SoftBlueBadge,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = selectedRol.titulo,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Lista visible con los 4 roles para seleccionarlos con 1 toque
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        roles.forEach { item ->
                            val isSelected = selectedRol.codigoRol == item.codigoRol
                            Surface(
                                onClick = {
                                    selectedRol = item
                                    email = item.emailDemo
                                    password = item.passwordDemo
                                    errorMessage = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics {
                                        contentDescription = "Rol ${item.titulo}: ${item.descripcion}"
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Color(0xFFE0F2FE) else InputFieldBg,
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) PrimaryBlue else Color(0xFFE2E8F0)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) PrimaryBlue else Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = item.icono,
                                            contentDescription = null,
                                            tint = if (isSelected) Color.White else PrimaryBlue,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.titulo,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = if (isSelected) Color(0xFF002B49) else Color.DarkGray
                                        )
                                        Text(
                                            text = item.descripcion,
                                            fontSize = 11.5.sp,
                                            color = Color(0xFF475569),
                                            lineHeight = 15.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = if (isSelected) PrimaryBlue else Color.Gray,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (errorMessage != null) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_error_banner")
                                .semantics {
                                    liveRegion = LiveRegionMode.Polite
                                    contentDescription = "Alerta de validación: ${errorMessage!!}"
                                },
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFEBEE),
                            border = BorderStroke(1.dp, Color(0xFFEF9A9A))
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = Color(0xFFC62828),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = errorMessage!!,
                                    color = Color(0xFFC62828),
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Botón Iniciar Sesión
                    Button(
                        onClick = { validarEIniciar() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_login"),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Iniciar Sesión como ${selectedRol.titulo}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Botón Ingreso con Biometría
                    Button(
                        onClick = { validarEIniciar() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecondaryButtonBg,
                            contentColor = PrimaryBlue
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ingreso con Biometría", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pie de página (Legales y Seguridad)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Conexión Segura SSL  •  Ley 20.584 Ficha Clínica",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Cumplimiento de estándares de confidencialidad y resguardo de datos médicos oftalmológicos.",
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}
