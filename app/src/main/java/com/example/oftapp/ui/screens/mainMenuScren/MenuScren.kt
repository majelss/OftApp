package com.example.oftapp.ui.screens.mainMenuScren

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
import com.example.oftapp.ui.screens.auth.LoginScreen

val DarkHeaderBlue = Color(0xFF004B72)
val AccentPillBg = Color(0xFF1E5B82)
val SelectedItemBg = Color(0xFFD4EAFA)
val BadgeBg = Color(0xFFB3E5FC)

@Composable
fun MainMenuScreen(
    onNavigateTo: (String) -> Unit = {}
) {
    var selectedBottomTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF8F9FC),
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedBottomTab == 0,
                    onClick = { selectedBottomTab = 0 },
                    icon = { Icon(Icons.Default.GridView, contentDescription = "Dashboard") },
                    label = { Text("Dashboard", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = selectedBottomTab == 1,
                    onClick = { selectedBottomTab = 1 },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "Atenciones") },
                    label = { Text("Atenciones", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = selectedBottomTab == 2,
                    onClick = { selectedBottomTab = 2 },
                    icon = { Icon(Icons.Default.MedicalServices, contentDescription = "Registro") },
                    label = { Text("Registro", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = selectedBottomTab == 3,
                    onClick = { selectedBottomTab = 3 },
                    icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
                    label = { Text("Historial", fontSize = 11.sp) }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            // Header del Médico (Azul Oscuro)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkHeaderBlue)
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
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

                        IconButton(onClick = { /* Cerrar/Acción */ }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color.White
                            )
                        }
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