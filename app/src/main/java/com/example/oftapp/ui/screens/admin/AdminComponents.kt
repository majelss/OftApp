package com.example.oftapp.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Tokens de color del Sistema de Diseño "Clinical Clarity" para el módulo Administrador.
 */
object ClinicalClarityColors {
    val Surface = Color(0xFFF8F9FF)
    val SurfaceLowest = Color(0xFFFFFFFF)
    val SurfaceLow = Color(0xFFEFF4FF)
    val SurfaceContainer = Color(0xFFE6EEFF)
    val SurfaceHigh = Color(0xFFDDE9FF)
    val SurfaceHighest = Color(0xFFD5E3FD)

    val Primary = Color(0xFF006385)
    val OnPrimary = Color(0xFFFFFFFF)
    val PrimaryContainer = Color(0xFF067DA7)
    val OnPrimaryContainer = Color(0xFFFBFCFF)
    val PrimaryFixed = Color(0xFFC3E8FF)
    val PrimaryFixedDim = Color(0xFF79D1FE)

    val Secondary = Color(0xFF006970)
    val OnSecondary = Color(0xFFFFFFFF)
    val SecondaryContainer = Color(0xFF6FF2FE)
    val OnSecondaryContainer = Color(0xFF006E75)

    val Tertiary = Color(0xFF325F9B)
    val OnSurface = Color(0xFF0D1C2F)
    val OnSurfaceVariant = Color(0xFF3F484E)
    val Outline = Color(0xFF6F787F)
    val OutlineVariant = Color(0xFFBEC8CF)
    val CardBorder = Color(0xFFE2E8F0)

    val InverseSurface = Color(0xFF233144)
    val InverseOnSurface = Color(0xFFEBF1FF)

    val Error = Color(0xFFBA1A1A)
    val OnError = Color(0xFFFFFFFF)
    val ErrorContainer = Color(0xFFFFDAD6)
    val OnErrorContainer = Color(0xFF93000A)

    // Badges semánticos clínicos
    val BadgeAuthorizedBg = Color(0xFFDCFCE7)
    val BadgeAuthorizedText = Color(0xFF15803D)
    val BadgeCreatedBg = Color(0xFFE0F2FE)
    val BadgeCreatedText = Color(0xFF0369A1)
    val BadgeConfigBg = Color(0xFFFEF3C7)
    val BadgeConfigText = Color(0xFF92400E)
    val BadgeDisabledBg = Color(0xFFD5E3FD)
    val BadgeDisabledText = Color(0xFF3F484E)
}

enum class AdminTab(val label: String, val icon: ImageVector) {
    USUARIOS("Usuarios", Icons.Default.Group),
    CATALOGOS("Catálogos", Icons.Default.MenuBook),
    SISTEMA("Sistema", Icons.Default.Tune),
    AUDITORIA("Auditoría", Icons.Default.VerifiedUser)
}

/**
 * Barra de navegación inferior persistente exclusiva del módulo Administrador (4 destinos).
 */
@Composable
fun AdminBottomNavBar(
    selectedTab: AdminTab,
    onSelectTab: (AdminTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = ClinicalClarityColors.SurfaceLowest,
        shadowElevation = 8.dp,
        border = BorderStroke(1.dp, ClinicalClarityColors.CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(68.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AdminTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                val contentColor = if (isSelected) ClinicalClarityColors.Primary else ClinicalClarityColors.OnSurfaceVariant

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectTab(tab) }
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(28.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (isSelected) ClinicalClarityColors.SurfaceHigh else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = contentColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = tab.label,
                        fontSize = 11.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = contentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * Header superior para las pantallas de Sistema y Auditoría del módulo OftApp Admin.
 */
@Composable
fun AdminModuleHeader(
    title: String,
    onBackClick: () -> Unit,
    onSearchClick: (() -> Unit)? = null,
    onProfileClick: (() -> Unit)? = null
) {
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
                    onClick = onBackClick,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver al menú",
                        tint = ClinicalClarityColors.OnSurface
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ClinicalClarityColors.SurfaceHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = ClinicalClarityColors.Primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "OFTAPP ADMIN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ClinicalClarityColors.Secondary,
                        letterSpacing = 0.6.sp
                    )
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ClinicalClarityColors.OnSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onSearchClick != null) {
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = ClinicalClarityColors.OnSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                var menuPerfilExpandido by remember { mutableStateOf(false) }
                Box {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(ClinicalClarityColors.Primary)
                            .clickable {
                                if (onProfileClick != null) onProfileClick() else menuPerfilExpandido = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil Admin",
                            tint = ClinicalClarityColors.OnPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = menuPerfilExpandido,
                        onDismissRequest = { menuPerfilExpandido = false }
                    ) {
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
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = null,
                                    tint = ClinicalClarityColors.Error
                                )
                            },
                            onClick = {
                                menuPerfilExpandido = false
                                onBackClick()
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Toast flotante institucional con diseño InverseSurface ("Clinical Clarity").
 */
@Composable
fun AdminToastBanner(
    visible: Boolean,
    title: String,
    subtitle: String? = null,
    badgeRight: String? = "SIS Sincronizado",
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(14.dp),
            color = ClinicalClarityColors.InverseSurface,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.TaskAlt,
                        contentDescription = null,
                        tint = ClinicalClarityColors.SecondaryContainer,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            color = ClinicalClarityColors.InverseOnSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (!subtitle.isNullOrBlank()) {
                            Text(
                                text = subtitle,
                                color = ClinicalClarityColors.SurfaceHighest,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                if (!badgeRight.isNullOrBlank()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = badgeRight,
                        color = ClinicalClarityColors.PrimaryFixedDim,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Valida que el RUT chileno tenga formato xx.xxx.xxx-x o xxxxxxxx-x con dígito verificador 0-9 o K.
 */
fun validarFormatoRutChileno(rut: String): Boolean {
    val limpio = rut.trim().uppercase()
    val regex = Regex("^\\d{1,2}\\.?\\d{3}\\.?\\d{3}-[\\dK]$")
    return regex.matches(limpio)
}
