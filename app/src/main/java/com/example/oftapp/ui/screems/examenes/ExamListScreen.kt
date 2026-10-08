package com.example.oftapp.ui.screems.examenes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.ui.theme.OftAppAccentBlue
import com.example.oftapp.ui.theme.OftAppBackground
import com.example.oftapp.ui.theme.OftAppBorder
import com.example.oftapp.ui.theme.OftAppNavIndicator
import com.example.oftapp.ui.theme.OftAppTealContainer
import com.example.oftapp.ui.theme.OftAppTealDark
import com.example.oftapp.ui.theme.OftAppTealPrimary
import com.example.oftapp.ui.theme.OftAppTextMuted
import com.example.oftapp.ui.theme.OftAppTextPrimary
import com.example.oftapp.ui.theme.OftAppTextSecondary

data class ItemAtencion(
    val id: String,
    val paciente: String,
    val rut: String,
    val examen: String,
    val ojo: String,
    val sucursal: String,
    val fecha: String,
    val estado: String
)

/**
 * Pantalla de Consulta de Atenciones y Exámenes (RF02, RF07).
 * Incluye búsqueda por paciente/RUT, filtros por estado y navegación al detalle.
 */
@Composable
fun ExamListScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetalle: () -> Unit = {},
    onNavigateToRegistro: () -> Unit = {},
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToHistorial: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var filtroEstado by remember { mutableStateOf("Todos") }

    val listaAtenciones = remember {
        listOf(
            ItemAtencion(
                id = "#CP-2024-8841",
                paciente = "Mariana González Riquelme",
                rut = "14.821.903-2",
                examen = "Campimetría Computarizada Humphrey 30-2",
                ojo = "OD",
                sucursal = "Sucursal Providencia - Box 3",
                fecha = "24 Oct 2024 • 10:30",
                estado = "Validado"
            ),
            ItemAtencion(
                id = "#OCT-2024-4112",
                paciente = "Carlos Venegas Soto",
                rut = "18.332.194-K",
                examen = "OCT Macular Alta Resolución",
                ojo = "AO",
                sucursal = "Sucursal Puerto Montt - Box 1",
                fecha = "24 Oct 2024 • 09:15",
                estado = "Pendiente"
            ),
            ItemAtencion(
                id = "#TOP-2024-0982",
                paciente = "Beatriz Morales Lara",
                rut = "12.784.551-8",
                examen = "Topografía Corneal Pentacam HR",
                ojo = "OI",
                sucursal = "Sucursal Las Condes - Box 5",
                fecha = "23 Oct 2024 • 16:45",
                estado = "Observado"
            ),
            ItemAtencion(
                id = "#AV-2024-3310",
                paciente = "Ignacio Silva Reyes",
                rut = "16.920.104-3",
                examen = "Agudeza Visual y Refracción Clínica",
                ojo = "AO",
                sucursal = "Sucursal Viña del Mar - Box 2",
                fecha = "23 Oct 2024 • 11:20",
                estado = "Validado"
            )
        )
    }

    val listaFiltrada = listaAtenciones.filter { item ->
        val matchesSearch = item.paciente.contains(searchQuery, ignoreCase = true) ||
                item.rut.contains(searchQuery, ignoreCase = true) ||
                item.id.contains(searchQuery, ignoreCase = true)
        val matchesFiltro = filtroEstado == "Todos" || item.estado == filtroEstado
        matchesSearch && matchesFiltro
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OftAppBackground,
        topBar = {
            Surface(color = Color.White, shadowElevation = 1.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = OftAppTealPrimary,
                        modifier = Modifier.size(24.dp)
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
                            text = "Atenciones & Exámenes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OftAppTextPrimary
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToRegistro,
                containerColor = OftAppTealPrimary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Examen")
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 6.dp) {
                val items = listOf(
                    Triple("Dashboard", Icons.Default.GridView, "Dashboard"),
                    Triple("Atenciones", Icons.AutoMirrored.Filled.Assignment, "Atenciones"),
                    Triple("Registro", Icons.Default.AddBox, "Registro"),
                    Triple("Historial", Icons.Default.History, "Historial")
                )
                items.forEach { (nombre, icono, etiqueta) ->
                    val isSelected = nombre == "Atenciones"
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            when (nombre) {
                                "Dashboard" -> onNavigateToDashboard()
                                "Registro" -> onNavigateToRegistro()
                                "Historial" -> onNavigateToHistorial()
                            }
                        },
                        icon = { Icon(icono, contentDescription = etiqueta) },
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Buscador
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por paciente, RUT o #examen...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = OftAppTealPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OftAppTealPrimary,
                    unfocusedBorderColor = OftAppBorder,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Chips de Filtro (RF07)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Todos", "Validado", "Pendiente", "Observado").forEach { estado ->
                    val isSelected = filtroEstado == estado
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) OftAppTealPrimary else Color.White)
                            .border(1.dp, if (isSelected) OftAppTealPrimary else OftAppBorder, RoundedCornerShape(16.dp))
                            .clickable { filtroEstado = estado }
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = estado,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else OftAppTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${listaFiltrada.size} exámenes encontrados",
                fontSize = 12.sp,
                color = OftAppTextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Lista de Atenciones
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listaFiltrada) { atencion ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToDetalle() },
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, OftAppBorder),
                        shadowElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = atencion.id,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OftAppTealPrimary
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(OftAppTealContainer)
                                            .padding(horizontal = 6.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = atencion.ojo,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = OftAppTealPrimary
                                        )
                                    }
                                }

                                val estadoColor = when (atencion.estado) {
                                    "Validado" -> Color(0xFF15803D)
                                    "Observado" -> Color(0xFFDC2626)
                                    else -> Color(0xFFB45309)
                                }
                                val estadoBg = when (atencion.estado) {
                                    "Validado" -> Color(0xFFDCFCE7)
                                    "Observado" -> Color(0xFFFEE2E2)
                                    else -> Color(0xFFFEF3C7)
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(estadoBg)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = atencion.estado,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = estadoColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = atencion.paciente,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = OftAppTextPrimary
                            )

                            Text(
                                text = "RUT: ${atencion.rut}",
                                fontSize = 11.5.sp,
                                color = OftAppTextMuted
                            )

                            Text(
                                text = atencion.examen,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF004D63),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${atencion.fecha} • ${atencion.sucursal}",
                                    fontSize = 10.5.sp,
                                    color = OftAppTextMuted
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Ver Detalle",
                                    tint = OftAppTealPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
