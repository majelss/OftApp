package com.example.oftapp.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.material.icons.automirrored.filled.Logout

val DarkHeaderBlue = Color(0xFF004B72)
val AccentPillBg = Color(0xFF1E5B82)
val SelectedItemBg = Color(0xFFD4EAFA)
val BadgeBg = Color(0xFFB3E5FC)

@Composable
fun MainMenuScreen(
    onNavigateTo: (String) -> Unit = {}
) {
    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .background(Color.White)
        ) {
            // Header del Médico (Azul Oscuro)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkHeaderBlue)
                    .statusBarsPadding()
                    .padding(20.dp)
            ) {
                Column {
                    // Avatar del Médico
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color.LightGray, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Foto Doctor",
                            tint = Color.White,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Dr. Carlos Morales",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Médico Oftalmólogo • Reg. Med. #44921",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Pill "En Turno"
                    Surface(
                        color = AccentPillBg,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(0xFF81D4FA), shape = CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "En Turno — Box 4 Central",
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Opciones de Menú
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                MenuItemRow(
                    icon = Icons.Default.GridView,
                    title = "Dashboard / Inicio",
                    isSelected = true,
                    onClick = { onNavigateTo("dashboard") }
                )

                MenuItemRow(
                    icon = Icons.Default.FormatListBulleted,
                    title = "Lista de Atenciones",
                    badgeText = "8 hoy",
                    onClick = { onNavigateTo("lista_atenciones") }
                )

                MenuItemRow(
                    icon = Icons.Default.AddCircleOutline,
                    title = "Registrar Examen",
                    onClick = { onNavigateTo("registrar_examen") }
                )

                MenuItemRow(
                    icon = Icons.Default.History,
                    title = "Historial Clínico",
                    onClick = { onNavigateTo("historial") }
                )

                MenuItemRow(
                    icon = Icons.Default.Visibility,
                    title = "Patologías & Agudeza",
                    onClick = { onNavigateTo("patologias") }
                )

                Spacer(modifier = Modifier.weight(1f))

                HorizontalDivider(
                    color = Color(0xFFE2E8F0),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
                )

                MenuItemRow(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = "Cerrar Sesión",
                    onClick = { onNavigateTo("login") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "OftApp v2.4 • Clínica Oftalmológica Central",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun MenuItemRow(
    icon: ImageVector,
    title: String,
    badgeText: String? = null,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        color = if (isSelected) SelectedItemBg else Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF003355),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = Color(0xFF002233),
                modifier = Modifier.weight(1f)
            )

            if (badgeText != null) {
                Surface(
                    color = BadgeBg,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004B72),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            } else if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFF003355), shape = CircleShape)
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun menuScreenPreview() {
    MainMenuScreen()
}