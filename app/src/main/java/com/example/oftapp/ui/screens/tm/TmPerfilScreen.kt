package com.example.oftapp.ui.screens.tm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.R
import com.example.oftapp.ui.theme.ClinicalClarityColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TmPerfilScreen(
    onNavigateBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var showLogoutModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ClinicalClarityColors.Background)
            .verticalScroll(scrollState)
            .padding(bottom = 100.dp)
    ) {
        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 24.dp)
        ) {
            // ============================================================
            // TARJETA DE IDENTIDAD Y PERFIL PRINCIPAL
            // ============================================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = ClinicalClarityColors.SurfaceContainerLowest,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar con badge y botón editar foto
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .padding(bottom = 16.dp)
                    ) {
                        // Avatar principal
                        Surface(
                            modifier = Modifier
                                .size(96.dp)
                                .padding(4.dp),
                            shape = CircleShape,
                            color = ClinicalClarityColors.SurfaceContainerHigh
                        ) {
                            // Placeholder para foto - en producción usar Coil/Glide
                            Box(
                                modifier = Modifier
                                    .size(88.dp)
                                    .clip(CircleShape)
                                    .background(ClinicalClarityColors.SurfaceContainer)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "Foto de perfil del tecnólogo médico",
                                    tint = ClinicalClarityColors.OnSurfaceVariant,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }

                        // Badge de verificación médica (abajo-derecha)
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.BottomEnd)
                        ) {
                            Surface(
                                modifier = Modifier.size(28.dp),
                                shape = CircleShape,
                                color = ClinicalClarityColors.Primary,
                                shadowElevation = 2.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Verified,
                                        contentDescription = "Identidad Médica Verificada",
                                        tint = ClinicalClarityColors.OnPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        // Botón flotante cambiar foto (arriba-derecha)
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.TopEnd)
                        ) {
                            Surface(
                                modifier = Modifier.size(28.dp),
                                shape = CircleShape,
                                color = ClinicalClarityColors.SurfaceContainer,
                                shadowElevation = 1.dp
                            ) {
                                IconButton(onClick = { /* Cambiar foto */ }) {
                                    Icon(
                                        Icons.Default.PhotoCamera,
                                        contentDescription = "Cambiar fotografía de perfil",
                                        tint = ClinicalClarityColors.OnSurfaceVariant,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Nombre y Titulación
                    Text(
                        text = "T.M. Felipe Araya San Martín",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.W700,
                        color = ClinicalClarityColors.OnSurface,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )

                    Text(
                        text = "Tecnólogo Médico — Oftalmología y Optometría",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        color = ClinicalClarityColors.Primary,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 0.dp)
                    )

                    // Badge de Validación Institucional
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .background(ClinicalClarityColors.SecondaryContainer.copy(alpha = 0.4f))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = ClinicalClarityColors.Secondary,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Identidad Validada (ClaveÚnica / SIS)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W700,
                                color = ClinicalClarityColors.OnSecondaryContainer,
                                letterSpacing = 0.4.sp
                            )
                        }
                    }

                    // Datos Rápidos de Contacto Institucional
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 0.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ContactRow(
                            icon = Icons.Default.Mail,
                            label = "Correo Institucional",
                            value = "felipe.araya@oftappclinica.cl",
                            iconColor = ClinicalClarityColors.Primary
                        )
                        ContactRow(
                            icon = Icons.Default.PhoneIphone,
                            label = "Teléfono Clínico Directo",
                            value = "+56 9 8412 9043",
                            iconColor = ClinicalClarityColors.Primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ============================================================
            // CREDENCIALES Y REGISTRO PROFESIONAL
            // ============================================================
            SectionCard(title = "Credenciales Clínicas", icon = Icons.Default.Badge, iconColor = ClinicalClarityColors.Primary) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Registro SIS con botón copiar
                    CredentialRow(
                        label = "Registro SIS Superintendencia",
                        value = "N° 481.920",
                        valueColor = ClinicalClarityColors.Primary,
                        isBold = true,
                        actionLabel = "Copiar",
                        onAction = { /* Copiar al portapapeles */ }
                    )

                    // Grid RUT + Año Titulación
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CredentialBox(
                            label = "RUT Profesional",
                            value = "16.321.405-K",
                            modifier = Modifier.weight(1f)
                        )
                        CredentialBox(
                            label = "Año de Titulación",
                            value = "2017 (8 años)",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Universidad
                    InfoRow(
                        icon = Icons.Default.School,
                        iconColor = ClinicalClarityColors.Secondary,
                        label = "Casa de Estudios",
                        value = "Universidad de Chile",
                        subtitle = "Facultad de Medicina — Campus Norte"
                    )

                    // Especialidad
                    InfoRow(
                        icon = Icons.Default.Visibility,
                        iconColor = ClinicalClarityColors.Primary,
                        label = "Especialidad Funcional",
                        value = "Exploración Funcional y Segmento Posterior",
                        subtitle = "OCT macula/papila, Campimetría Humphrey, Angiografía"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ============================================================
            // SEGURIDAD Y ACCESO
            // ============================================================
            SectionCard(title = "Seguridad y Acceso", icon = Icons.Default.Security, iconColor = ClinicalClarityColors.Tertiary) {
                Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    SettingRow(
                        icon = Icons.Default.Key,
                        iconColor = ClinicalClarityColors.Outline,
                        label = "Contraseña",
                        subtitle = "Actualizada hace 2 meses",
                        trailing = { },
                        actionLabel = "Cambiar",
                        actionColor = ClinicalClarityColors.Primary,
                        onAction = { /* Cambiar contraseña */ }
                    )
                    DividerHorizontal()
                    SettingRow(
                        icon = Icons.Default.PhonelinkLock,
                        iconColor = ClinicalClarityColors.Secondary,
                        label = "Autenticación en 2 pasos",
                        subtitle = "Authenticator App (OTP)",
                        trailing = {
                            BadgeSuccess(text = "Activo")
                        }
                    )
                    DividerHorizontal()
                    SettingRow(
                        icon = Icons.Default.Draw,
                        iconColor = ClinicalClarityColors.Primary,
                        label = "Certificado de Firma Digital",
                        subtitle = "Válido hasta Dic 2025 (e-Sign)",
                        trailing = {
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = "Token HSM activo",
                                tint = ClinicalClarityColors.Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )
                    DividerHorizontal()
                    SettingRow(
                        icon = Icons.Default.Devices,
                        iconColor = ClinicalClarityColors.Outline,
                        label = "Sesiones Activas",
                        subtitle = "1 móvil (Samsung Galaxy S23)",
                        trailing = {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(ClinicalClarityColors.SecondaryContainer)
                                    .clip(CircleShape)
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ============================================================
            // PREFERENCIAS Y NORMATIVA
            // ============================================================
            SectionCard(title = "Preferencias y Normativa", icon = Icons.Default.Tune, iconColor = ClinicalClarityColors.OnSurfaceVariant) {
                Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    SettingRow(
                        icon = Icons.Default.Language,
                        iconColor = ClinicalClarityColors.Outline,
                        label = "Idioma del Sistema",
                        subtitle = "Español (Chile)",
                        trailing = {
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = ClinicalClarityColors.OutlineVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                    DividerHorizontal()
                    SettingRow(
                        icon = Icons.Default.NotificationsActive,
                        iconColor = ClinicalClarityColors.Outline,
                        label = "Notificaciones Clínicas",
                        subtitle = "Push y correo habilitados",
                        trailing = {
                            SwitchControl(checked = true, onCheckedChange = { /* Toggle */ })
                        }
                    )
                    DividerHorizontal()
                    SettingRow(
                        icon = Icons.Default.Policy,
                        iconColor = ClinicalClarityColors.Outline,
                        label = "Consentimiento de Datos Clínicos",
                        subtitle = "Leyes 20.584 y 19.628",
                        trailing = {
                            Text(
                                text = "Revisar",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W700,
                                color = ClinicalClarityColors.Primary
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ============================================================
            // ACCIONES PRINCIPALES Y DESTRUCTIVAS
            // ============================================================
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Editar Datos de Contacto (Secundario Outline)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .border(2.dp, ClinicalClarityColors.Primary, RoundedCornerShape(16.dp))
                        .background(ClinicalClarityColors.SurfaceContainerLowest)
                ) {
                    Button(
                        onClick = { /* Editar datos */ },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = ClinicalClarityColors.Primary
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Editar Datos de Contacto", fontSize = 16.sp, fontWeight = FontWeight.W600)
                        }
                    }
                }

                // Cerrar Sesión (Peligro / Destructivo)
                Button(
                    onClick = { showLogoutModal = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ClinicalClarityColors.ErrorContainer,
                        contentColor = ClinicalClarityColors.OnErrorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar Sesión en este Dispositivo", fontSize = 16.sp, fontWeight = FontWeight.W600)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ============================================================
            // PIE INSTITUCIONAL SUTIL
            // ============================================================
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = ClinicalClarityColors.OnSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Plataforma Oftalmológica Certificada MINSAL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.W600,
                        color = ClinicalClarityColors.OnSurfaceVariant
                    )
                }
                Text(
                    text = "Versión 2.8.4-build (Android Release)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.W600,
                    color = ClinicalClarityColors.Outline
                )
            }
        }

        // ============================================================
        // MODAL DE CONFIRMACIÓN DE LOGOUT
        // ============================================================
        if (showLogoutModal) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 360.dp),
                    color = ClinicalClarityColors.SurfaceContainerLowest,
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Icono superior
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(bottom = 12.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                color = ClinicalClarityColors.ErrorContainer
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.PowerSettingsNew,
                                        contentDescription = null,
                                        tint = ClinicalClarityColors.OnErrorContainer,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = "¿Cerrar sesión activa?",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.W600,
                            color = ClinicalClarityColors.OnSurface,
                            textAlign = TextAlign.Center,
                            lineHeight = 26.sp
                        )

                        Text(
                            text = "Para volver a ingresar deberás validar tu acceso mediante autenticación médica.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400,
                            color = ClinicalClarityColors.OnSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp, start = 0.dp, end = 0.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showLogoutModal = false },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = ClinicalClarityColors.OnSurface
                                )
                            ) {
                                Text("Cancelar", fontSize = 14.sp, fontWeight = FontWeight.W600)
                            }

                            Button(
                                onClick = {
                                    showLogoutModal = false
                                    onLogout()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ClinicalClarityColors.Error,
                                    contentColor = ClinicalClarityColors.OnErrorContainer
                                )
                            ) {
                                Text("Sí, Salir", fontSize = 14.sp, fontWeight = FontWeight.W600)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================
// COMPONENTES REUTILIZABLES
// ============================================================

@Composable
private fun ContactRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ClinicalClarityColors.OutlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        color = ClinicalClarityColors.SurfaceContainerLow,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.W700,
                        color = ClinicalClarityColors.OnSurfaceVariant,
                        letterSpacing = 0.4.sp
                    )
                    Text(
                        text = value,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = ClinicalClarityColors.OnSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = ClinicalClarityColors.OutlineVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun CredentialRow(
    label: String,
    value: String,
    valueColor: Color,
    isBold: Boolean,
    actionLabel: String,
    onAction: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ClinicalClarityColors.OutlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        color = ClinicalClarityColors.SurfaceContainerLow,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W700,
                    color = ClinicalClarityColors.OnSurfaceVariant,
                    letterSpacing = 0.4.sp
                )
                Text(
                    text = value,
                    fontSize = 14.sp,
                    fontWeight = if (isBold) FontWeight.W700 else FontWeight.W500,
                    color = valueColor,
                    letterSpacing = if (isBold) 0.5.sp else 0.sp
                )
            }
            TextButton(
                onClick = onAction,
                modifier = Modifier.padding(horizontal = 4.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = ClinicalClarityColors.Primary
                )
            ) {
                Text(actionLabel, fontSize = 12.sp, fontWeight = FontWeight.W700)
            }
        }
    }
}

@Composable
private fun CredentialBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .border(1.dp, ClinicalClarityColors.OutlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        color = ClinicalClarityColors.SurfaceContainerLow,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.W700,
                color = ClinicalClarityColors.OnSurfaceVariant,
                textAlign = TextAlign.Center,
                letterSpacing = 0.4.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.W700,
                color = ClinicalClarityColors.OnSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    iconColor: Color,
    label: String,
    value: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp).padding(top = 2.dp)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.W700,
                color = ClinicalClarityColors.OnSurfaceVariant,
                letterSpacing = 0.4.sp
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.W700,
                color = ClinicalClarityColors.OnSurface
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                color = ClinicalClarityColors.OnSurfaceVariant
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = ClinicalClarityColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header de sección
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = if (iconColor == ClinicalClarityColors.OnSurfaceVariant) {
                        ClinicalClarityColors.SurfaceContainerHigh
                    } else {
                        surfaceColorForIcon(iconColor)
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = if (iconColor == ClinicalClarityColors.OnSurfaceVariant) {
                                ClinicalClarityColors.OnSurfaceVariant
                            } else {
                                onSurfaceColorForIcon(iconColor)
                            },
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W700,
                    color = ClinicalClarityColors.OnSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    iconColor: Color,
    label: String,
    subtitle: String,
    trailing: @Composable () -> Unit,
    actionLabel: String? = null,
    actionColor: Color = ClinicalClarityColors.Primary,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W700,
                    color = ClinicalClarityColors.OnSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400,
                    color = ClinicalClarityColors.OnSurfaceVariant
                )
            }
        }
        trailing()
        actionLabel?.let { label ->
            TextButton(
                onClick = { onAction?.invoke() },
                colors = ButtonDefaults.textButtonColors(contentColor = actionColor)
            ) {
                Text(label, fontSize = 12.sp, fontWeight = FontWeight.W700)
            }
        }
    }
}

@Composable
private fun DividerHorizontal() {
    androidx.compose.material3.Divider(
        color = ClinicalClarityColors.SurfaceContainerHigh.copy(alpha = 0.6f),
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 0.dp)
    )
}

@Composable
private fun BadgeSuccess(text: String) {
    Box(
        modifier = Modifier
            .background(ClinicalClarityColors.SecondaryContainer)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = ClinicalClarityColors.Secondary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.W700,
                color = ClinicalClarityColors.Secondary
            )
        }
    }
}

@Composable
private fun SwitchControl(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    androidx.compose.material3.Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = ClinicalClarityColors.Primary,
            checkedTrackColor = ClinicalClarityColors.PrimaryContainer,
            uncheckedThumbColor = ClinicalClarityColors.OnSurfaceVariant,
            uncheckedTrackColor = ClinicalClarityColors.OutlineVariant
        )
    )
}

// Helpers para colores de iconos en SectionCard
private fun surfaceColorForIcon(iconColor: Color): Color = when {
    iconColor == ClinicalClarityColors.Primary -> ClinicalClarityColors.PrimaryContainer
    iconColor == ClinicalClarityColors.Secondary -> ClinicalClarityColors.SecondaryContainer
    iconColor == ClinicalClarityColors.Tertiary -> ClinicalClarityColors.TertiaryContainer
    iconColor == ClinicalClarityColors.Error -> ClinicalClarityColors.ErrorContainer
    else -> ClinicalClarityColors.SurfaceContainerHigh
}

private fun onSurfaceColorForIcon(iconColor: Color): Color = when {
    iconColor == ClinicalClarityColors.Primary -> ClinicalClarityColors.OnPrimaryContainer
    iconColor == ClinicalClarityColors.Secondary -> ClinicalClarityColors.OnSecondaryContainer
    iconColor == ClinicalClarityColors.Tertiary -> ClinicalClarityColors.TertiaryContainer
    iconColor == ClinicalClarityColors.Error -> ClinicalClarityColors.OnErrorContainer
    else -> ClinicalClarityColors.OnSurfaceVariant
}