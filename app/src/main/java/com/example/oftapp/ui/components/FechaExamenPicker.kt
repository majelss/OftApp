package com.example.oftapp.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oftapp.ui.theme.OftAppBorder
import com.example.oftapp.ui.theme.OftAppTealPrimary
import com.example.oftapp.ui.theme.OftAppTextMuted
import com.example.oftapp.ui.theme.OftAppTextPrimary
import com.example.oftapp.ui.theme.OftAppTextSecondary
import java.util.Calendar

/**
 * Integración con recursos del dispositivo: DatePickerDialog y TimePickerDialog nativos de Android.
 * Permite seleccionar la fecha y hora reales del examen clínico.
 * (DSY1105 - OftApp)
 */
@Composable
fun FechaExamenPicker(
    fechaSeleccionada: String,
    onFechaSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Abre el TimePickerDialog tras seleccionar el día
    val mostrarTimePicker: (Int, Int, Int) -> Unit = { anio, mes, dia ->
        TimePickerDialog(
            context,
            { _, hora, minuto ->
                val fechaFormateada = "%02d/%02d/%04d %02d:%02d".format(dia, mes + 1, anio, hora, minuto)
                onFechaSeleccionada(fechaFormateada)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // Formato de 24 horas
        ).show()
    }

    // Abre el DatePickerDialog
    val mostrarDatePicker = {
        DatePickerDialog(
            context,
            { _, anio, mes, dia ->
                mostrarTimePicker(anio, mes, dia)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis() // No permitir fechas futuras
        }.show()
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Fecha y Hora del Examen *",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = OftAppTextSecondary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable { mostrarDatePicker() },
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.dp, OftAppBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Seleccionar fecha",
                    tint = OftAppTealPrimary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = if (fechaSeleccionada.isBlank()) "Toque para seleccionar fecha y hora" else fechaSeleccionada,
                    fontSize = 13.5.sp,
                    color = if (fechaSeleccionada.isBlank()) OftAppTextMuted else OftAppTextPrimary,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = OftAppTextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Selector nativo de fecha y hora del sistema",
            fontSize = 11.5.sp,
            color = OftAppTextMuted
        )
    }
}
