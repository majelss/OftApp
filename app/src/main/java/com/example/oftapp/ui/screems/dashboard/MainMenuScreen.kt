package com.example.oftapp.ui.screems.dashboard

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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.ui.theme.OftAppBackground
import com.example.oftapp.ui.theme.OftAppBorder
import com.example.oftapp.ui.theme.OftAppTealDark
import com.example.oftapp.ui.theme.OftAppTealPrimary
import com.example.oftapp.ui.theme.OftAppTextMuted
import com.example.oftapp.ui.theme.OftAppTextPrimary
import com.example.oftapp.ui.theme.OftAppTextSecondary

/**
 * Pantalla de Menú Principal Clínico (DSY1105).
 */
@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onNavigateToRegistro: () -> Unit = {},
    onNavigateToAtenciones: () -> Unit = {},
    onNavigateToHistorial: () -> Unit = {},
    onNavigateToDetalle: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OftAppBackground,
        topBar = {
            Surface(color = Color.White, shadowElevation = 1.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = OftAppTextPrimary
                        )
                    }
                    Text(
                        text = "Menú Principal",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = OftAppTextPrimary
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Tarjeta de Perfil
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OftAppBorder)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(OftAppTealDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Dr. Carlos Mendoza", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OftAppTextPrimary)
                        Text(text = "Oftalmólogo • Sucursal Providencia", fontSize = 12.sp, color = OftAppTextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Módulos del Sistema OftApp",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = OftAppTealDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            MenuItemCard(
                titulo = "Triage y Registrar Examen",
                descripcion = "Carga de antecedentes, lateralidad y archivo simulado",
                icono = Icons.Default.AddBox,
                onClick = onNavigateToRegistro
            )

            Spacer(modifier = Modifier.height(8.dp))

            MenuItemCard(
                titulo = "Consulta de Atenciones",
                descripcion = "Listado de exámenes con filtros por estado y fecha",
                icono = Icons.AutoMirrored.Filled.Assignment,
                onClick = onNavigateToAtenciones
            )

            Spacer(modifier = Modifier.height(8.dp))

            MenuItemCard(
                titulo = "Visor de Campimetría y Detalle",
                descripcion = "Interpretación visual de campo visual e índices",
                icono = Icons.Default.Visibility,
                onClick = onNavigateToDetalle
            )

            Spacer(modifier = Modifier.height(8.dp))

            MenuItemCard(
                titulo = "Historial por Paciente Ficticio",
                descripcion = "Trazabilidad de antecedentes anteriores (RF06)",
                icono = Icons.Default.History,
                onClick = onNavigateToHistorial
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuItemCard(
                titulo = "Cerrar Sesión",
                descripcion = "Salir de la aplicación y volver a autenticación",
                icono = Icons.AutoMirrored.Filled.ExitToApp,
                onClick = onLogout,
                colorIcono = Color(0xFFDC2626)
            )
        }
    }
}

@Composable
private fun MenuItemCard(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    onClick: () -> Unit,
    colorIcono: Color = OftAppTealPrimary
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, OftAppBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorIcono.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = colorIcono, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = titulo, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = OftAppTextPrimary)
                Text(text = descripcion, fontSize = 11.5.sp, color = OftAppTextMuted)
            }
        }
    }
}
