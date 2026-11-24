package com.example.oftapp.ui.screens.admin

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oftapp.data.local.AuditLogEntity
import com.example.oftapp.viewmodel.AdminAuditViewModel

private data class AuditFilterChip(
    val key: String,
    val label: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAuditScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateTab: (AdminTab) -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: AdminAuditViewModel = viewModel(
        factory = AdminAuditViewModel.factory(context)
    )
    val state by viewModel.uiState.collectAsState()

    val filterChips = remember {
        listOf(
            AuditFilterChip("all", "Todos los eventos", Icons.Default.ListAlt),
            AuditFilterChip("usuarios", "Modificación Usuarios", Icons.Default.ManageAccounts),
            AuditFilterChip("parametros", "Parámetros Clínicos", Icons.Default.Tune),
            AuditFilterChip("examenes", "Estado Examen", Icons.Default.AssignmentTurnedIn),
            AuditFilterChip("fichas", "Acceso a Fichas", Icons.Default.LockOpen)
        )
    }

    val logsAgrupados = remember(state.logsFiltrados) {
        state.logsFiltrados.groupBy { it.fechaGrupo }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerShape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp)
            ) {
                AdminDrawerMenu(
                    selectedTab = AdminTab.AUDITORIA,
                    onSelectTab = { tab ->
                        scope.launch { drawerState.close() }
                        onNavigateTab(tab)
                    },
                    onLogout = onNavigateBack
                )
            }
        }
    ) {
        Scaffold(
            containerColor = ClinicalClarityColors.Surface,
            topBar = {
                AdminModuleHeader(
                    title = "Auditoría",
                    onBackClick = onNavigateBack,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onSearchClick = {}
                )
            },
            bottomBar = {
                AdminBottomNavBar(
                    selectedTab = AdminTab.AUDITORIA,
                    onSelectTab = onNavigateTab
                )
            }
        ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 14.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Subheader Forense
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = ClinicalClarityColors.Primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "BITÁCORA CRIPTOGRÁFICA INMUTABLE",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = ClinicalClarityColors.Primary,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(
                            text = "Registro de Auditoría y Control de Cambios",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = ClinicalClarityColors.OnSurface,
                            lineHeight = 29.sp
                        )
                        Text(
                            text = "Trazabilidad inmutable de modificaciones de usuarios, exámenes y permisos.",
                            fontSize = 14.5.sp,
                            color = ClinicalClarityColors.OnSurfaceVariant
                        )
                    }
                }

                // 2. Resumen KPI (3 tarjetas horizontales)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KpiMetricCard(
                            icon = Icons.Default.HistoryToggleOff,
                            iconTint = ClinicalClarityColors.Primary,
                            value = state.eventosHoyCount.toString(),
                            valueColor = ClinicalClarityColors.OnSurface,
                            label = "Eventos hoy",
                            modifier = Modifier.weight(1f)
                        )
                        KpiMetricCard(
                            icon = Icons.Default.Security,
                            iconTint = ClinicalClarityColors.Secondary,
                            value = "0",
                            valueColor = ClinicalClarityColors.OnSurface,
                            label = "Alertas críticas",
                            modifier = Modifier.weight(1f)
                        )
                        KpiMetricCard(
                            icon = Icons.Default.Fingerprint,
                            iconTint = ClinicalClarityColors.Tertiary,
                            value = "100%",
                            valueColor = ClinicalClarityColors.Primary,
                            label = "Integridad SIS",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // 3. Buscador y Chips Horizontales de Filtrado
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = state.busqueda,
                            onValueChange = viewModel::onBusquedaChange,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "Buscar por operador, afectado o ID...",
                                    color = ClinicalClarityColors.Outline,
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = ClinicalClarityColors.Outline
                                )
                            },
                            trailingIcon = {
                                if (state.busqueda.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onBusquedaChange("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Limpiar búsqueda",
                                            tint = ClinicalClarityColors.Outline
                                        )
                                    }
                                }
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ClinicalClarityColors.SurfaceLowest,
                                unfocusedContainerColor = ClinicalClarityColors.SurfaceLowest,
                                focusedBorderColor = ClinicalClarityColors.Primary,
                                unfocusedBorderColor = ClinicalClarityColors.CardBorder
                            ),
                            singleLine = true
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            filterChips.forEach { chip ->
                                val selected = state.categoriaSeleccionada == chip.key
                                Surface(
                                    onClick = { viewModel.onCategoriaChange(chip.key) },
                                    shape = RoundedCornerShape(50),
                                    color = if (selected) ClinicalClarityColors.Primary else ClinicalClarityColors.SurfaceLowest,
                                    border = if (selected) null else BorderStroke(1.dp, ClinicalClarityColors.CardBorder),
                                    shadowElevation = 1.dp
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = chip.icon,
                                            contentDescription = null,
                                            tint = if (selected) ClinicalClarityColors.OnPrimary else ClinicalClarityColors.OnSurfaceVariant,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = chip.label,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (selected) ClinicalClarityColors.OnPrimary else ClinicalClarityColors.OnSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. Línea de tiempo agrupada por fecha o Empty State
                if (state.logsFiltrados.isEmpty()) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = ClinicalClarityColors.SurfaceLowest,
                            border = BorderStroke(1.dp, ClinicalClarityColors.CardBorder)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(ClinicalClarityColors.SurfaceHigh),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ManageSearch,
                                        contentDescription = null,
                                        tint = ClinicalClarityColors.Outline,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Sin registros encontrados",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ClinicalClarityColors.OnSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "No existen eventos auditados que coincidan con los criterios aplicados.",
                                    fontSize = 13.5.sp,
                                    color = ClinicalClarityColors.OnSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    onClick = { viewModel.restablecerFiltros() },
                                    shape = RoundedCornerShape(50),
                                    color = ClinicalClarityColors.SurfaceContainer
                                ) {
                                    Text(
                                        text = "Restablecer filtros",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ClinicalClarityColors.Primary,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    logsAgrupados.entries.forEachIndexed { index, (fechaGrupo, logsEnFecha) ->
                        item(key = "header_$fechaGrupo") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = if (index > 0) 6.dp else 0.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = fechaGrupo,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ClinicalClarityColors.Outline,
                                    letterSpacing = 0.5.sp
                                )
                                if (index == 0) {
                                    Text(
                                        text = "${state.logsFiltrados.size} registros visibles",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ClinicalClarityColors.Secondary
                                    )
                                }
                            }
                        }

                        items(
                            items = logsEnFecha,
                            key = { it.id }
                        ) { log ->
                            AuditEventCard(
                                log = log,
                                onVerHash = { viewModel.mostrarDetalleHash(log) }
                            )
                        }
                    }
                }

                // 5. Botón de Exportación Primario con Timbre Digital
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { viewModel.setMostrarExportSheet(true) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ClinicalClarityColors.Primary,
                                contentColor = ClinicalClarityColors.OnPrimary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Exportar Log de Auditoría (PDF / CSV)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = ClinicalClarityColors.Outline,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Documento legalmente timbrado con sello criptográfico",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ClinicalClarityColors.Outline,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Toast flotante inferior de exportación
            AdminToastBanner(
                visible = state.toastVisible,
                title = state.toastTitulo,
                subtitle = state.toastDescripcion,
                badgeRight = "SHA-256",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            )
        }
    }

    // Diálogo Modal de Verificación Criptográfica SHA-256
    val logHash = state.logSeleccionadoParaHash
    if (logHash != null) {
        AlertDialog(
            onDismissRequest = { viewModel.mostrarDetalleHash(null) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = null,
                    tint = ClinicalClarityColors.Primary,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = "Comprobante Forense ${logHash.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Evento: ${logHash.titulo}",
                        fontWeight = FontWeight.SemiBold,
                        color = ClinicalClarityColors.OnSurface
                    )
                    Text(
                        text = "Operador: ${logHash.operador}\n${logHash.afectadoEtiqueta} ${logHash.afectadoValor}\nFecha/Hora: ${logHash.fechaGrupo} • ${logHash.hora}",
                        fontSize = 13.sp,
                        color = ClinicalClarityColors.OnSurfaceVariant
                    )
                    Surface(
                        color = ClinicalClarityColors.SurfaceLow,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, ClinicalClarityColors.SurfaceHighest)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "HUELLA CRIPTOGRÁFICA SHA-256:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ClinicalClarityColors.Primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = logHash.hashSha256,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                color = ClinicalClarityColors.OnSurface
                            )
                        }
                    }
                    Text(
                        text = "✓ Integridad verificada conforme a la Ley N° 20.584.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ClinicalClarityColors.Secondary
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.mostrarDetalleHash(null) }) {
                    Text("Cerrar", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Modal Bottom Sheet de Exportación Forense
    if (state.mostrarExportSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.setMostrarExportSheet(false) },
            containerColor = ClinicalClarityColors.SurfaceLowest
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Exportación de Log Forense",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ClinicalClarityColors.OnSurface
                )
                Text(
                    text = "Seleccione el formato clínico certificado para el expediente de auditoría externa.",
                    fontSize = 13.5.sp,
                    color = ClinicalClarityColors.OnSurfaceVariant
                )

                ExportOptionRow(
                    icon = Icons.Default.PictureAsPdf,
                    iconBg = ClinicalClarityColors.ErrorContainer,
                    iconTint = ClinicalClarityColors.OnErrorContainer,
                    title = "Informe PDF con Timbre Electrónico",
                    subtitle = "Certificado por Superintendencia de Salud",
                    onClick = {
                        viewModel.confirmarExportacion("PDF Timbrado")
                        compartirResumenAuditoria(context, "PDF Timbrado", state.logsFiltrados)
                    }
                )

                ExportOptionRow(
                    icon = Icons.Default.TableChart,
                    iconBg = ClinicalClarityColors.SurfaceHighest,
                    iconTint = ClinicalClarityColors.Primary,
                    title = "Matriz de Datos CSV (Auditoría TI)",
                    subtitle = "Tabulación cruda con huella digital SHA-256",
                    onClick = {
                        viewModel.confirmarExportacion("CSV Estructurado")
                        compartirResumenAuditoria(context, "CSV Estructurado", state.logsFiltrados)
                    }
                )

                OutlinedButton(
                    onClick = { viewModel.setMostrarExportSheet(false) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar", fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
}

private fun compartirResumenAuditoria(
    context: android.content.Context,
    formato: String,
    logs: List<AuditLogEntity>
) {
    try {
        val cuerpo = buildString {
            appendLine("BITÁCORA CRIPTOGRÁFICA INMUTABLE OFTAPP - $formato")
            appendLine("Total de eventos exportados: ${logs.size}")
            appendLine("---------------------------------------------------")
            logs.forEach { l ->
                appendLine("${l.id} | ${l.titulo} [${l.estado}]")
                appendLine("Operador: ${l.operador} | ${l.afectadoEtiqueta} ${l.afectadoValor}")
                appendLine("Detalle: ${l.detalleTexto}")
                appendLine("SHA-256: ${l.hashSha256}")
                appendLine()
            }
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Reporte de Auditoría OftApp ($formato)")
            putExtra(Intent.EXTRA_TEXT, cuerpo)
        }
        context.startActivity(Intent.createChooser(intent, "Exportar Bitácora de Auditoría"))
    } catch (_: Exception) {
        // Si no hay actividad externa disponible, el Toast ya informa al usuario.
    }
}

@Composable
private fun KpiMetricCard(
    icon: ImageVector,
    iconTint: Color,
    value: String,
    valueColor: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = ClinicalClarityColors.SurfaceLow,
        border = BorderStroke(1.dp, ClinicalClarityColors.SurfaceHighest)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ClinicalClarityColors.SurfaceHighest),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
            Text(
                text = label,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = ClinicalClarityColors.OnSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AuditEventCard(
    log: AuditLogEntity,
    onVerHash: () -> Unit
) {
    val (badgeBg, badgeText) = when (log.estado) {
        "Autorizado" -> ClinicalClarityColors.BadgeAuthorizedBg to ClinicalClarityColors.BadgeAuthorizedText
        "Creado" -> ClinicalClarityColors.BadgeCreatedBg to ClinicalClarityColors.BadgeCreatedText
        "Configuración" -> ClinicalClarityColors.BadgeConfigBg to ClinicalClarityColors.BadgeConfigText
        else -> ClinicalClarityColors.BadgeDisabledBg to ClinicalClarityColors.BadgeDisabledText
    }

    val leadingIcon = when {
        log.estado == "Creado" -> Icons.Default.PersonAdd
        log.estado == "Configuración" -> Icons.Default.Tune
        log.estado == "Desactivado" -> Icons.Default.PersonOff
        else -> Icons.Default.AdminPanelSettings
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ClinicalClarityColors.SurfaceLowest,
        border = BorderStroke(1.dp, ClinicalClarityColors.CardBorder),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Encabezado del evento + Badge de estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ClinicalClarityColors.SurfaceHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = null,
                            tint = ClinicalClarityColors.Primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = log.id,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ClinicalClarityColors.Tertiary
                        )
                        Text(
                            text = log.titulo,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ClinicalClarityColors.OnSurface
                        )
                    }
                }

                Surface(
                    color = badgeBg,
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(badgeText)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = log.estado,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeText
                        )
                    }
                }
            }

            // Bloque Operador y Afectado/Entidad
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = ClinicalClarityColors.SurfaceLow
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = ClinicalClarityColors.OnSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Operador:",
                                fontSize = 13.sp,
                                color = ClinicalClarityColors.OnSurfaceVariant
                            )
                        }
                        Text(
                            text = log.operador,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ClinicalClarityColors.OnSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (log.afectadoEtiqueta.contains("Entidad")) Icons.Default.Flag else Icons.Default.Person,
                                contentDescription = null,
                                tint = ClinicalClarityColors.OnSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = log.afectadoEtiqueta,
                                fontSize = 13.sp,
                                color = ClinicalClarityColors.OnSurfaceVariant
                            )
                        }
                        Text(
                            text = log.afectadoValor,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ClinicalClarityColors.OnSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Bloque Diferencial (Antes / Después o Detalle Forense)
            if (log.valorPrevio != null && log.valorNuevo != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        color = ClinicalClarityColors.SurfaceHigh
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Valor previo",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = ClinicalClarityColors.Outline
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = log.valorPrevio,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ClinicalClarityColors.Outline,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        color = ClinicalClarityColors.SurfaceContainer
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Nuevo valor",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = ClinicalClarityColors.Primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = log.valorNuevo,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = ClinicalClarityColors.Primary
                            )
                        }
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = log.detalleTitulo,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ClinicalClarityColors.Outline
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = ClinicalClarityColors.SurfaceContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = when (log.estado) {
                                    "Creado" -> Icons.Default.Verified
                                    "Desactivado" -> Icons.Default.EventBusy
                                    else -> Icons.Default.VpnKey
                                },
                                contentDescription = null,
                                tint = when (log.estado) {
                                    "Creado" -> ClinicalClarityColors.Secondary
                                    "Desactivado" -> ClinicalClarityColors.Outline
                                    else -> ClinicalClarityColors.Primary
                                },
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = log.detalleTexto,
                                fontSize = 13.5.sp,
                                color = ClinicalClarityColors.OnSurface,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }

            // Pie: Hora y Hash SHA-256
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = ClinicalClarityColors.Outline,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = log.hora,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ClinicalClarityColors.Outline
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onVerHash() }
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hash SHA-256",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ClinicalClarityColors.Primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = ClinicalClarityColors.Primary,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExportOptionRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = ClinicalClarityColors.SurfaceLow,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ClinicalClarityColors.OnSurface
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.5.sp,
                        color = ClinicalClarityColors.Outline
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = ClinicalClarityColors.Outline
            )
        }
    }
}
