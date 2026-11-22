package com.example.oftapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.ui.components.OftAppUnifiedTopBar
import com.example.oftapp.ui.components.UnifiedBorderGray
import com.example.oftapp.ui.components.UnifiedDarkNavy
import com.example.oftapp.ui.components.UnifiedIconBoxBg
import com.example.oftapp.ui.components.UnifiedMutedSlate
import com.example.oftapp.ui.components.UnifiedPrimaryBlue

@Composable
fun PlaceholderScreen(
    titulo: String,
    onBack: () -> Unit = {}
) {
    val protocolos = listOf(
        Triple("Glaucoma Crónico de Ángulo Abierto", "CIE-10: H40.1 • Umbral PIO < 21 mmHg", "Campimetría 24-2 + OCT Papilar"),
        Triple("Catarata Senil Nuclear Bilateral", "CIE-10: H25.1 • Agudeza Snellen < 20/40", "Biometría Óptica + Topografía Pentacam"),
        Triple("Retinopatía Diabética No Proliferativa", "CIE-10: E11.3 • Control Macular", "Retinografía Digital + OCT Macular 3D"),
        Triple("Astigmatismo Miópico Compuesto", "CIE-10: H52.2 • Cartilla Snellen Digital", "Refracción Subjetiva + Queratometría")
    )

    Scaffold(
        containerColor = Color(0xFFF5F8FD),
        topBar = {
            OftAppUnifiedTopBar(
                title = titulo,
                badgeSubtitle = "OFTAPP CLINICAL",
                onBackClick = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, UnifiedBorderGray)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(UnifiedIconBoxBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = UnifiedPrimaryBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Catálogo de Patologías y Agudeza Visual",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = UnifiedDarkNavy
                        )
                        Text(
                            text = "Protocolos clínicos estandarizados (Datos sintéticos DSY1105)",
                            fontSize = 12.5.sp,
                            color = UnifiedMutedSlate
                        )
                    }
                }
            }

            protocolos.forEach { (nombre, codigo, estudio) ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, UnifiedBorderGray),
                    shadowElevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
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
                                Icon(
                                    imageVector = Icons.Default.MedicalServices,
                                    contentDescription = null,
                                    tint = UnifiedPrimaryBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = nombre,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UnifiedDarkNavy
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Protocolo activo",
                                tint = Color(0xFF15803D),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = codigo,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = UnifiedPrimaryBlue
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.RemoveRedEye,
                                contentDescription = null,
                                tint = UnifiedMutedSlate,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Exámenes requeridos: $estudio",
                                fontSize = 12.sp,
                                color = UnifiedMutedSlate
                            )
                        }
                    }
                }
            }
        }
    }
}
