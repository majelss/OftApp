package com.example.oftapp.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
//Tarjetas segun estados

fun TarjetaEstadistica(
    titulo: String,
    cantidad: Int,
    detalle: String,
    tipo: String,
    modifier: Modifier = Modifier
) {
    val colorPrincipal = when (tipo) {
        "pendientes" -> Color(0xFFDC2626)
        "completados" -> Color(0xFF087E9B)
        else -> Color(0xFF164B88)
    }

    val colorIcono = when (tipo) {
        "pendientes" -> Color(0xFFFEE2E2)
        "completados" -> Color(0xFFDBEAFE)
        else -> Color(0xFFDBEAFE)
    }

    val icono = when (tipo) {
        "pendientes" -> "◷"
        "completados" -> "✓"
        else -> "♙"
    }

    Card(
        modifier = modifier.width(180.dp),
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
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = colorIcono,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icono,
                        color = colorPrincipal,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = detalle,
                    color = colorPrincipal,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = cantidad.toString(),
                color = Color(0xFF0F172A),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = titulo,
                color = Color(0xFF334155),
                fontSize = 14.sp,
                lineHeight = 19.sp
            )
        }
    }
}