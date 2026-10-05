package com.example.oftapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores personalizados del diseño
val PrimaryBlue = Color(0xFF006689)
val LightBlueBg = Color(0xFFEFF5FC)
val SoftBlueBadge = Color(0xFFCBE6FF)
val CardBackground = Color.White
val InputFieldBg = Color(0xFFF0F4F9)
val SecondaryButtonBg = Color(0xFFC7E7FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {} // Parámetro para conectar la navegación
) {
    var email by remember { mutableStateOf("dr.valenzuela@clinicaofta.cl") }
    var password by remember { mutableStateOf("1234567890") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estado del selector de Rol
    var expandedRol by remember { mutableStateOf(false) }
    val roles = listOf(
        "Médico Oftalmólogo" to "Acceso diagnóstico completo (OD / OS)",
        "Administrativo" to "Gestión de citas y pacientes",
        "Técnico Médica" to "Ingreso de exámenes preliminares"
    )
    var selectedRol by remember { mutableStateOf(roles[0]) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlueBg)
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
                    .size(80.dp)
                    .background(Color(0xFFE0ECFA), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Logo OftApp",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "OftApp",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF002B49)
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

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta Blanca con Formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
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
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
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

                    Spacer(modifier = Modifier.height(16.dp))

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
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryBlue)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null
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

                    // Selector de Rol
                    Text(
                        text = "Rol de Usuario",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    ExposedDropdownMenuBox(
                        expanded = expandedRol,
                        onExpandedChange = { expandedRol = !expandedRol }
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            color = InputFieldBg
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.MedicalServices,
                                    contentDescription = null,
                                    tint = PrimaryBlue
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        selectedRol.first,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(selectedRol.second, fontSize = 11.sp, color = Color.Gray)
                                }
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRol)
                            }
                        }

                        ExposedDropdownMenu(
                            expanded = expandedRol,
                            onDismissRequest = { expandedRol = false }
                        ) {
                            roles.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(item.first, fontWeight = FontWeight.Bold)
                                            Text(item.second, fontSize = 11.sp, color = Color.Gray)
                                        }
                                    },
                                    onClick = {
                                        selectedRol = item
                                        expandedRol = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}