package com.example.oftapp.ui.screems.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oftapp.ui.componentes.TarjetaEstadistica
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    modelo: DashboardViewModel = viewModel(),
    onNavegar: (String) -> Unit = {}
) {
    val estadoMenu = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val alcanceCorrutina = rememberCoroutineScope()

    var pestanaSeleccionada by remember {
        mutableIntStateOf(0)
    }

    ModalNavigationDrawer(
        drawerState = estadoMenu,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerShape = RoundedCornerShape(
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                )
            ) {
                MainMenuScreen(
                    opcionSeleccionada = "Dashboard / Inicio",
                    onSeleccionar = { opcion ->
                        alcanceCorrutina.launch {
                            estadoMenu.close()
                        }
                        if (opcion != "Dashboard / Inicio") {
                            onNavegar(opcion)
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            containerColor = Color(0xFFF5F8FD),
            bottomBar = {
                BarraInferiorDashboard(
                    seleccionada = pestanaSeleccionada,
                    onSeleccionar = { indice ->
                        pestanaSeleccionada = indice
                        if (indice != 0) {
                            val destino = when (indice) {
                                1 -> "Lista de Atenciones"
                                2 -> "Registrar Examen"
                                else -> "Historial Clínico"
                            }
                            onNavegar(destino)
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onNavegar("Registrar Examen")
                    },
                    containerColor = Color(0xFF087E9B),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = "+",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        ) { espacioInterno ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(espacioInterno),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 12.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    EncabezadoDashboard(
                        alAbrirMenu = {
                            alcanceCorrutina.launch {
                                estadoMenu.open()
                            }
                        }
                    )
                }

                item {
                    TarjetaBienvenida()
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        modelo.estadisticas.forEach { estadistica ->
                            TarjetaEstadistica(
                                titulo = estadistica.titulo,
                                cantidad = estadistica.cantidad,
                                detalle = estadistica.detalle,
                                tipo = estadistica.tipo
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Acciones Rápidas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF10243D)
                    )
                }

                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TarjetaAccion(
                                titulo = "Nuevo Examen",
                                descripcion = "Fondo de ojo, tonometría y más",
                                simbolo = "▣",
                                colorIcono = Color(0xFFE2EDFF),
                                modifier = Modifier.weight(1f),
                                alPulsar = {
                                    onNavegar("Registrar Examen")
                                }
                            )

                            TarjetaAccion(
                                titulo = "Buscar Paciente",
                                descripcion = "Historial y cédula",
                                simbolo = "⌕",
                                colorIcono = Color(0xFFD2EDFF),
                                modifier = Modifier.weight(1f),
                                alPulsar = {
                                    onNavegar("Buscar Paciente")
                                }
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TarjetaAccion(
                                titulo = "Revisión OD / OI",
                                descripcion = "Agudeza visual rápida",
                                simbolo = "◎",
                                colorIcono = Color(0xFFD8E8FF),
                                modifier = Modifier.weight(1f),
                                alPulsar = {
                                    onNavegar("Revisión OD / OI")
                                }
                            )

                            TarjetaAccion(
                                titulo = "Recetas & Lentes",
                                descripcion = "Fórmulas y cristales",
                                simbolo = "⌑",
                                colorIcono = Color(0xFFB8F6FA),
                                modifier = Modifier.weight(1f),
                                alPulsar = {
                                    onNavegar("Recetas y Lentes")
                                }
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Próximos en Espera",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF10243D)
                        )

                        Text(
                            text = "Ver todos (${modelo.proximasAtenciones.size})  ›",
                            color = Color(0xFF087E9B),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable {
                                onNavegar("Lista de Atenciones")
                            }
                        )
                    }
                }

                items(modelo.proximasAtenciones) { atencion ->
                    TarjetaAtencion(
                        atencion = atencion,
                        alPulsar = {
                            onNavegar("Detalle de atención")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EncabezadoDashboard(
    alAbrirMenu: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFEAF4FF)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "◎",
                color = Color(0xFF087E9B),
                fontSize = 30.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "OFTAPP CLINICAL",
                color = Color(0xFF087E9B),
                fontSize = 13.sp,
                letterSpacing = 2.sp
            )
            Text(
                text = "Dashboard",
                color = Color(0xFF102E56),
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "♧",
            color = Color(0xFF334155),
            fontSize = 25.sp,
            modifier = Modifier.padding(horizontal = 10.dp)
        )

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF064477))
                .clickable(onClick = alAbrirMenu),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "♙",
                color = Color.White,
                fontSize = 25.sp
            )
        }
    }
}

@Composable
private fun TarjetaBienvenida() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF103F80),
                            Color(0xFF087E9B)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "▦  Clínica Oftalmológica Central",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x443D91C0))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )

                Text(
                    text = "Hoy, 24 Oct",
                    color = Color(0xFFB8D8F4),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Buenos días, Dr. Usuario",
                color = Color.White,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Turno matutino • Gabinete 3 - Lámpara de Hendidura",
                color = Color(0xFFD1E5F8),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun TarjetaAccion(
    titulo: String,
    descripcion: String,
    simbolo: String,
    colorIcono: Color,
    modifier: Modifier = Modifier,
    alPulsar: () -> Unit
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .clickable(onClick = alPulsar),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(colorIcono),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = simbolo,
                    color = Color(0xFF075985),
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = titulo,
                color = Color(0xFF10243D),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = descripcion,
                color = Color(0xFF475569),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TarjetaAtencion(
    atencion: AtencionResumen,
    alPulsar: () -> Unit
) {
    val colorEstado = when (atencion.estado) {
        "Urgente" -> Color(0xFFFEE2E2)
        "En sala" -> Color(0xFFDBEAFE)
        else -> Color(0xFFE0EAFF)
    }

    val textoEstado = when (atencion.estado) {
        "Urgente" -> Color(0xFFB91C1C)
        "En sala" -> Color(0xFF075985)
        else -> Color(0xFF334155)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = alPulsar),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE4EEFF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = atencion.iniciales,
                    color = Color(0xFF164477),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = atencion.nombrePaciente,
                        color = Color(0xFF10243D),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Text(
                        text = atencion.estado,
                        color = textoEstado,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(colorEstado)
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "${atencion.hora}  •  ${atencion.examen}",
                    color = Color(0xFF475569),
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE7F0FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "→",
                    color = Color(0xFF064477),
                    fontSize = 23.sp
                )
            }
        }
    }
}

@Composable
private fun BarraInferiorDashboard(
    seleccionada: Int,
    onSeleccionar: (Int) -> Unit
) {
    val elementos = listOf(
        "Dashboard" to "▦",
        "Atenciones" to "▤",
        "Registro" to "⊞",
        "Historial" to "◴"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        elementos.forEachIndexed { indice, elemento ->
            val activo = seleccionada == indice

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        onSeleccionar(indice)
                    }
                    .padding(vertical = 3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (activo) Color(0xFFC6E8FF)
                            else Color.Transparent
                        )
                        .padding(horizontal = 24.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = elemento.second,
                        color = if (activo) {
                            Color(0xFF064477)
                        } else {
                            Color(0xFF475569)
                        },
                        fontSize = 23.sp
                    )
                }

                Text(
                    text = elemento.first,
                    color = if (activo) {
                        Color(0xFF064477)
                    } else {
                        Color(0xFF475569)
                    },
                    fontSize = 12.sp,
                    fontWeight = if (activo) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    }
                )
            }
        }
    }
}