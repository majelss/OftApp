package com.example.oftapp.ui.screens.tm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.ui.theme.ClinicalClarityColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TmPerfilScreen(
    onNavigateBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ClinicalClarityColors.Background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, tint = ClinicalClarityColors.Primary, contentDescription = "Retroceso")
            }
            Text(
                text = "Perfil de Tecnólogo Médico",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ClinicalClarityColors.Primary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Avatar
        Surface(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(50.dp),
            color = ClinicalClarityColors.PrimaryContainer
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    Icons.Default.Person,
                    tint = ClinicalClarityColors.OnPrimary,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tecnólogo Médico",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ClinicalClarityColors.OnSurface
        )
        Text(
            text = "Especialidad: Oftalmología",
            fontSize = 14.sp,
            color = ClinicalClarityColors.OnSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Logout button
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ClinicalClarityColors.Error,
                contentColor = ClinicalClarityColors.OnPrimary
            )
        ) {
            Icon(Icons.Default.Logout, contentDescription = "Logout")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}