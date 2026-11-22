package com.example.oftapp.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File
import java.io.FileOutputStream

/**
 * Visor de PDF integrado con `android.graphics.pdf.PdfRenderer` (Requisito 5 y Anexo A).
 * Renderiza páginas reales de un documento PDF clínico sintético con navegación entre páginas
 * y soporte de zoom táctil (pellizco) y botones accesibles (>= 48dp) compatibles con TalkBack.
 */
@Composable
fun PdfViewerDialog(
    idExamen: String,
    pacienteNombre: String,
    pacienteRut: String,
    tipoExamen: String,
    observaciones: String,
    medicoValidador: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var currentPage by remember { mutableIntStateOf(0) }
    var totalPages by remember { mutableIntStateOf(1) }
    var renderedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val pdfFile = remember(idExamen) {
        generarPdfClinicoSintetico(
            context = context,
            idExamen = idExamen,
            pacienteNombre = pacienteNombre,
            pacienteRut = pacienteRut,
            tipoExamen = tipoExamen,
            observaciones = observaciones,
            medicoValidador = medicoValidador
        )
    }

    LaunchedEffect(pdfFile, currentPage) {
        try {
            ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)?.use { pfd ->
                PdfRenderer(pfd).use { renderer ->
                    totalPages = renderer.pageCount.coerceAtLeast(1)
                    val safePage = currentPage.coerceIn(0, totalPages - 1)
                    renderer.openPage(safePage).use { page ->
                        val width = (page.width * 2).coerceAtLeast(600)
                        val height = (page.height * 2).coerceAtLeast(850)
                        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        bmp.eraseColor(AndroidColor.WHITE)
                        page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                        renderedBitmap = bmp
                    }
                }
            }
        } catch (_: Exception) {
            // Fallback seguro en caso de error de E/S
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            shape = RoundedCornerShape(18.dp),
            color = Color(0xFFF8F9FF)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header del Visor PDF
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFFDAD6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = null,
                                tint = Color(0xFF93000A),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Visor PDF Integrado (PdfRenderer)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D1C2F),
                                modifier = Modifier.semantics { heading() }
                            )
                            Text(
                                text = "${pdfFile.name} • Página ${currentPage + 1} de $totalPages",
                                fontSize = 12.sp,
                                color = Color(0xFF3F484E),
                                modifier = Modifier.semantics {
                                    liveRegion = LiveRegionMode.Polite
                                }
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(48.dp)
                            .semantics { contentDescription = "Cerrar visor de informe PDF" }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar visor PDF",
                            tint = Color(0xFF0D1C2F)
                        )
                    }
                }

                // Barra de controles de Zoom accesibles (>= 48dp)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Zoom: ${(scale * 100).toInt()}%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF006385)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    scale = (scale - 0.25f).coerceAtLeast(1f)
                                    if (scale == 1f) offset = Offset.Zero
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .semantics { contentDescription = "Alejar documento PDF" }
                            ) {
                                Icon(Icons.Default.ZoomOut, contentDescription = "Alejar PDF")
                            }

                            IconButton(
                                onClick = {
                                    scale = (scale + 0.25f).coerceAtMost(3f)
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .semantics { contentDescription = "Acercar documento PDF" }
                            ) {
                                Icon(Icons.Default.ZoomIn, contentDescription = "Acercar PDF")
                            }

                            IconButton(
                                onClick = {
                                    scale = 1f
                                    offset = Offset.Zero
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .semantics { contentDescription = "Restablecer zoom del PDF al 100 por ciento" }
                            ) {
                                Icon(Icons.Default.RestartAlt, contentDescription = "Restablecer zoom")
                            }
                        }
                    }
                }

                // Lienzo del PDF renderizado con gesto de pellizco (detectTransformGestures)
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                val newScale = (scale * zoom).coerceIn(1f, 3.5f)
                                scale = newScale
                                offset = if (newScale > 1f) {
                                    Offset(
                                        x = (offset.x + pan.x).coerceIn(-500f, 500f),
                                        y = (offset.y + pan.y).coerceIn(-700f, 700f)
                                    )
                                } else {
                                    Offset.Zero
                                }
                            }
                        }
                        .semantics {
                            contentDescription =
                                "Página ${currentPage + 1} del informe clínico PDF de $pacienteNombre. Use gesto de pellizco o botones superiores para hacer zoom."
                        },
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val bmp = renderedBitmap
                        if (bmp != null) {
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = "Informe oftalmológico PDF página ${currentPage + 1}",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer(
                                        scaleX = scale,
                                        scaleY = scale,
                                        translationX = offset.x,
                                        translationY = offset.y
                                    )
                            )
                        } else {
                            CircularProgressIndicator(color = Color(0xFF006385))
                        }
                    }
                }

                // Paginador inferior accesible (>= 48dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            if (currentPage > 0) {
                                currentPage--
                                scale = 1f
                                offset = Offset.Zero
                            }
                        },
                        enabled = currentPage > 0,
                        modifier = Modifier
                            .height(48.dp)
                            .semantics { contentDescription = "Ir a página anterior del PDF" },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.NavigateBefore, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Anterior", fontWeight = FontWeight.SemiBold)
                    }

                    Text(
                        text = "Pág. ${currentPage + 1} / $totalPages",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D1C2F)
                    )

                    Button(
                        onClick = {
                            if (currentPage < totalPages - 1) {
                                currentPage++
                                scale = 1f
                                offset = Offset.Zero
                            }
                        },
                        enabled = currentPage < totalPages - 1,
                        modifier = Modifier
                            .height(48.dp)
                            .semantics { contentDescription = "Ir a página siguiente del PDF" },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006385))
                    ) {
                        Text("Siguiente", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
                    }
                }
            }
        }
    }
}

/**
 * Visor de imágenes clínicas oftalmológicas sintéticas (Retina / Topografía Corneal)
 * utilizando `AsyncImage` de la biblioteca **Coil** con gesto de zoom real (`detectTransformGestures`)
 * y controles accesibles >= 48dp para TalkBack (Requisito 5 y Anexo A/B).
 */
@Composable
fun CoilRetinaZoomCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var modoTopografia by remember { mutableStateOf(false) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val imageFile = remember(modoTopografia) {
        generarImagenClinicaSinteticaCoil(context, esTopografia = modoTopografia)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color(0xFF006385),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "IMAGEN CLÍNICA CON COIL & ZOOM TÁCTIL",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF006385),
                            letterSpacing = 0.4.sp
                        )
                        Text(
                            text = if (modoTopografia) "Topografía Corneal Scheimpflug (OI)" else "Retinografía Digital de Fondo de Ojo (OD)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D1C2F),
                            modifier = Modifier.semantics { heading() }
                        )
                    }
                }
            }

            // Selector accesible entre Fondo de Retina y Topografía (>= 48dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        modoTopografia = false
                        scale = 1f
                        offset = Offset.Zero
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .semantics {
                            contentDescription = "Ver imagen sintética de Retinografía Fondo de Ojo Derecho"
                        },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!modoTopografia) Color(0xFF006385) else Color(0xFFEFF4FF),
                        contentColor = if (!modoTopografia) Color.White else Color(0xFF0D1C2F)
                    )
                ) {
                    Text("Retina (OD)", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        modoTopografia = true
                        scale = 1f
                        offset = Offset.Zero
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .semantics {
                            contentDescription = "Ver imagen sintética de Topografía Corneal Ojo Izquierdo"
                        },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (modoTopografia) Color(0xFF006385) else Color(0xFFEFF4FF),
                        contentColor = if (modoTopografia) Color.White else Color(0xFF0D1C2F)
                    )
                ) {
                    Text("Topografía (OI)", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Contenedor de imagen renderizada con Coil AsyncImage + gesto de pellizco
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0B1520))
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            val newScale = (scale * zoom).coerceIn(1f, 3.5f)
                            scale = newScale
                            offset = if (newScale > 1f) {
                                Offset(
                                    x = (offset.x + pan.x).coerceIn(-300f, 300f),
                                    y = (offset.y + pan.y).coerceIn(-250f, 250f)
                                )
                            } else {
                                Offset.Zero
                            }
                        }
                    }
                    .semantics {
                        contentDescription = if (modoTopografia) {
                            "Mapa de elevación corneal sintético renderizado con Coil, zoom actual ${(scale * 100).toInt()} por ciento"
                        } else {
                            "Imagen de fondo de retina sintética con nervio óptico y mácula renderizada con Coil, zoom actual ${(scale * 100).toInt()} por ciento"
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageFile)
                        .crossfade(true)
                        .build(),
                    contentDescription = if (modoTopografia) {
                        "Imagen clínica sintética de topografía corneal"
                    } else {
                        "Imagen clínica sintética de retina y mácula"
                    },
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x,
                            translationY = offset.y
                        )
                )
            }

            // Controles de Zoom accesibles (>= 48dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Zoom Coil: ${(scale * 100).toInt()}% (Pellizcar o usar botones)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF334155)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            scale = (scale - 0.25f).coerceAtLeast(1f)
                            if (scale == 1f) offset = Offset.Zero
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .semantics { contentDescription = "Disminuir zoom de imagen clínica" }
                    ) {
                        Icon(Icons.Default.ZoomOut, contentDescription = "Alejar imagen", tint = Color(0xFF006385))
                    }

                    IconButton(
                        onClick = {
                            scale = (scale + 0.25f).coerceAtMost(3f)
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .semantics { contentDescription = "Aumentar zoom de imagen clínica" }
                    ) {
                        Icon(Icons.Default.ZoomIn, contentDescription = "Acercar imagen", tint = Color(0xFF006385))
                    }

                    IconButton(
                        onClick = {
                            scale = 1f
                            offset = Offset.Zero
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .semantics { contentDescription = "Restablecer zoom de imagen al 100 por ciento" }
                    ) {
                        Icon(Icons.Default.RestartAlt, contentDescription = "Restablecer zoom", tint = Color(0xFF006385))
                    }
                }
            }
        }
    }
}

/**
 * Genera un documento PDF sintético de 2 páginas en cacheDir para ser leído con PdfRenderer.
 */
private fun generarPdfClinicoSintetico(
    context: Context,
    idExamen: String,
    pacienteNombre: String,
    pacienteRut: String,
    tipoExamen: String,
    observaciones: String,
    medicoValidador: String
): File {
    val safeName = idExamen.replace(Regex("[^A-Za-z0-9_-]"), "_")
    val outFile = File(context.cacheDir, "informe_oftapp_$safeName.pdf")

    val pdfDoc = PdfDocument()
    val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = AndroidColor.rgb(0, 99, 133)
        textSize = 18f
        isFakeBoldText = true
    }
    val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = AndroidColor.rgb(13, 28, 47)
        textSize = 13f
        isFakeBoldText = true
    }
    val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = AndroidColor.rgb(51, 65, 85)
        textSize = 11.5f
    }
    val boxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = AndroidColor.rgb(239, 244, 255)
        style = Paint.Style.FILL
    }
    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = AndroidColor.rgb(0, 99, 133)
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    // --- PÁGINA 1: Resumen Diagnóstico e Índices ---
    val pageInfo1 = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page1 = pdfDoc.startPage(pageInfo1)
    val c1: Canvas = page1.canvas

    c1.drawRect(RectF(36f, 36f, 559f, 105f), boxPaint)
    c1.drawText("OFTAPP CLINICAL SYSTEM — INFORME OFTALMOLÓGICO SINTÉTICO", 52f, 65f, titlePaint)
    c1.drawText("Código Examen: $idExamen   •   Estado: VALIDADO CON FIRMA FEA", 52f, 88f, subPaint)

    c1.drawText("1. IDENTIFICACIÓN DEL PACIENTE (DATOS SINTÉTICOS)", 42f, 140f, subPaint)
    c1.drawText("Nombre Paciente: $pacienteNombre", 52f, 164f, bodyPaint)
    c1.drawText("RUT / ID Clínico: $pacienteRut", 52f, 184f, bodyPaint)
    c1.drawText("Estudio Realizado: $tipoExamen", 52f, 204f, bodyPaint)

    c1.drawText("2. ÍNDICES GLOBALES DE CAMPO VISUAL Y SENSIBILIDAD", 42f, 250f, subPaint)
    c1.drawRect(RectF(42f, 265f, 553f, 355f), boxPaint)
    c1.drawRect(RectF(42f, 265f, 553f, 355f), borderPaint)
    c1.drawText("• MD (Desviación Media): -2.14 dB (p < 5%)", 60f, 292f, bodyPaint)
    c1.drawText("• PSD (Desviación Estándar Patrón): +1.85 dB (p < 2%)", 60f, 315f, bodyPaint)
    c1.drawText("• VFI (Índice de Campo Visual): 97% (Confiabilidad Alta - Pérdidas 0/14)", 60f, 338f, bodyPaint)

    c1.drawText("3. MAPA ESQUEMÁTICO DE SENSIBILIDAD RETINIANA (OD 30-2)", 42f, 395f, subPaint)
    val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = AndroidColor.rgb(220, 242, 254)
        style = Paint.Style.FILL
    }
    c1.drawCircle(297f, 515f, 95f, circlePaint)
    c1.drawCircle(297f, 515f, 95f, borderPaint)
    c1.drawCircle(297f, 515f, 48f, borderPaint)
    c1.drawLine(202f, 515f, 392f, 515f, borderPaint)
    c1.drawLine(297f, 420f, 297f, 610f, borderPaint)
    c1.drawText("29 dB", 260f, 495f, bodyPaint)
    c1.drawText("31 dB", 310f, 495f, bodyPaint)
    c1.drawText("28 dB", 260f, 540f, bodyPaint)
    c1.drawText("30 dB", 310f, 540f, bodyPaint)

    c1.drawText("4. OBSERVACIONES CLÍNICAS DEL ESPECIALISTA", 42f, 655f, subPaint)
    val obsLimpia = observaciones.take(180)
    c1.drawText(obsLimpia.take(90), 52f, 680f, bodyPaint)
    if (obsLimpia.length > 90) {
        c1.drawText(obsLimpia.substring(90), 52f, 700f, bodyPaint)
    }

    c1.drawText("Página 1 de 2 — Continúa en la página siguiente (Certificación Criptográfica)", 140f, 800f, bodyPaint)
    pdfDoc.finishPage(page1)

    // --- PÁGINA 2: Trazabilidad, Firma Electrónica y Sello SHA-256 ---
    val pageInfo2 = PdfDocument.PageInfo.Builder(595, 842, 2).create()
    val page2 = pdfDoc.startPage(pageInfo2)
    val c2: Canvas = page2.canvas

    c2.drawRect(RectF(36f, 36f, 559f, 95f), boxPaint)
    c2.drawText("ANEXO DE TRAZABILIDAD Y FIRMA ELECTRÓNICA AVANZADA", 52f, 70f, titlePaint)

    c2.drawText("5. VALIDACIÓN PROFESIONAL Y CUMPLIMIENTO NORMATIVO", 42f, 140f, subPaint)
    c2.drawText("Profesional Validador: $medicoValidador", 52f, 168f, bodyPaint)
    c2.drawText("Normativa Aplicada: Ley N° 20.584 (Ficha Clínica) • Estándar HL7 FHIR R4", 52f, 190f, bodyPaint)
    c2.drawText("Huella Criptográfica SHA-256: e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934c", 52f, 212f, bodyPaint)

    c2.drawRect(RectF(42f, 250f, 553f, 340f), boxPaint)
    c2.drawRect(RectF(42f, 250f, 553f, 340f), borderPaint)
    c2.drawText("NOTA ÉTICA Y ACADÉMICA (OFTAPP MVP):", 60f, 280f, subPaint)
    c2.drawText("Este documento contiene únicamente datos clínicos ficticios/sintéticos generados", 60f, 304f, bodyPaint)
    c2.drawText("para demostración académica en Duoc UC (DSY1105). No sustituye juicio médico.", 60f, 322f, bodyPaint)

    c2.drawText("Página 2 de 2 — Fin del Informe Clínico", 190f, 800f, bodyPaint)
    pdfDoc.finishPage(page2)

    FileOutputStream(outFile).use { fos ->
        pdfDoc.writeTo(fos)
    }
    pdfDoc.close()
    return outFile
}

/**
 * Genera una imagen PNG sintética en cacheDir para ser cargada con Coil (AsyncImage).
 */
private fun generarImagenClinicaSinteticaCoil(
    context: Context,
    esTopografia: Boolean
): File {
    val fileName = if (esTopografia) "coil_topografia_sintetica.png" else "coil_retina_sintetica.png"
    val file = File(context.cacheDir, fileName)
    if (file.exists() && file.length() > 0) return file

    val bmp = Bitmap.createBitmap(640, 420, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Fondo oscuro clínico
    canvas.drawColor(AndroidColor.rgb(11, 21, 32))

    if (!esTopografia) {
        // Fondo de ojo retiniano sintético
        paint.style = Paint.Style.FILL
        paint.color = AndroidColor.rgb(185, 58, 32)
        canvas.drawCircle(320f, 210f, 170f, paint)

        paint.color = AndroidColor.rgb(228, 92, 45)
        canvas.drawCircle(320f, 210f, 135f, paint)

        // Disco óptico (papila)
        paint.color = AndroidColor.rgb(255, 224, 150)
        canvas.drawCircle(255f, 205f, 32f, paint)

        // Mácula / Fóvea
        paint.color = AndroidColor.rgb(140, 32, 20)
        canvas.drawCircle(375f, 215f, 26f, paint)

        // Vasos retinianos sintéticos
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4.5f
        paint.color = AndroidColor.rgb(120, 18, 18)
        canvas.drawLine(255f, 205f, 390f, 125f, paint)
        canvas.drawLine(255f, 205f, 410f, 295f, paint)
        canvas.drawLine(255f, 205f, 180f, 130f, paint)
        canvas.drawLine(255f, 205f, 185f, 285f, paint)
    } else {
        // Mapa topográfico corneal Pentacam sintético (anillos concéntricos térmicos)
        val colores = listOf(
            AndroidColor.rgb(22, 101, 52),
            AndroidColor.rgb(34, 197, 94),
            AndroidColor.rgb(234, 179, 8),
            AndroidColor.rgb(249, 115, 22),
            AndroidColor.rgb(220, 38, 38)
        )
        val radios = listOf(170f, 135f, 100f, 65f, 32f)
        paint.style = Paint.Style.FILL
        for (i in colores.indices) {
            paint.color = colores[i]
            canvas.drawCircle(320f, 210f, radios[i], paint)
        }
        // Ejes queratométricos
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = AndroidColor.WHITE
        canvas.drawLine(140f, 210f, 500f, 210f, paint)
        canvas.drawLine(320f, 35f, 320f, 385f, paint)
    }

    paint.style = Paint.Style.FILL
    paint.color = AndroidColor.WHITE
    paint.textSize = 18f
    paint.isFakeBoldText = true
    val etiqueta = if (esTopografia) "PENTACAM CORNEAL MAP • K1 43.2D / K2 44.5D" else "RETINOGRAFÍA OD • EXCAVACIÓN 0.4 • MÁCULA SANA"
    canvas.drawText(etiqueta, 24f, 398f, paint)

    FileOutputStream(file).use { fos ->
        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
    }
    return file
}
