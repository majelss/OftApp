package com.example.oftapp.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class AdminDrawerItem(
    val tab: AdminTab,
    val titulo: String,
    val icono: ImageVector
)

@Composable
fun AdminDrawerMenu(
    selectedTab: AdminTab,
    onSelectTab: (AdminTab) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        AdminDrawerItem(AdminTab.USUARIOS, "Gestión de Usuarios", Icons.Default.Group),
        AdminDrawerItem(AdminTab.SISTEMA, "Configuración del Sistema", Icons.Default.Tune),
        AdminDrawerItem(AdminTab.AUDITORIA, "Auditoría de Seguridad", Icons.Default.VerifiedUser)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "OFTAPP ADMIN",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = ClinicalClarityColors.Primary,
            modifier = Modifier.padding(bottom = 4.dp, start = 12.dp)
        )
        Text(
            text = "Consola Clinical Clarity",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp, start = 12.dp)
        )

        items.forEach { item ->
            val estaSeleccionado = selectedTab == item.tab

            NavigationDrawerItem(
                label = { Text(text = item.titulo) },
                icon = { Icon(imageVector = item.icono, contentDescription = null) },
                selected = estaSeleccionado,
                onClick = { onSelectTab(item.tab) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFFE2E8F0))
        Spacer(modifier = Modifier.height(12.dp))

        NavigationDrawerItem(
            label = {
                Text(
                    text = "Cerrar Sesión",
                    color = Color(0xFFBA1A1A),
                    fontWeight = FontWeight.Bold
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Cerrar Sesión",
                    tint = Color(0xFFBA1A1A)
                )
            },
            selected = false,
            onClick = onLogout,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
