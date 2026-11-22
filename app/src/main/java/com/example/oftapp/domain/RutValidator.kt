package com.example.oftapp.domain

/**
 * Validador de RUT chileno con algoritmo Módulo 11.
 * Compatible con minSdk 24 (sin java.time).
 *
 * Uso:
 *   RutValidator.esValido("15.420.891-K") // true
 *   RutValidator.formatear("15420891K")   // "15.420.891-K"
 */
object RutValidator {

    private const val MULTIPLICADORES = "234567"

    /** Limpia el RUT: quita puntos, guiones y espacios. Deja solo dígitos + K. */
    fun limpiar(rut: String): String {
        return rut.filter { it.isDigit() || it.uppercaseChar() == 'K' }
    }

    /**
     * Calcula el dígito verificador a partir del cuerpo (sin DV).
     * @param cuerpo RUT sin DV, solo dígitos. Ej: "15420891"
     * @return '0'..'9' o 'K'
     */
    fun calcularDv(cuerpo: String): Char {
        var suma = 0
        var multiplicadorIndex = 0
        for (i in cuerpo.indices.reversed()) {
            val digito = cuerpo[i].digitToInt()
            val multiplicador = MULTIPLICADORES[multiplicadorIndex % MULTIPLICADORES.length].digitToInt()
            suma += digito * multiplicador
            multiplicadorIndex++
        }
        val residuo = 11 - (suma % 11)
        return when (residuo) {
            11 -> '0'
            10 -> 'K'
            else -> residuo.digitToChar()
        }
    }

    /**
     * Valida el RUT completo (cuerpo + DV).
     * Acepta formatos: "15420891K", "15.420.891-K", "15420891-k", "15.420.891K".
     */
    fun esValido(rut: String): Boolean {
        val limpio = limpiar(rut)
        if (limpio.length !in 8..9) return false // 7 dígitos + DV = 8, 8 dígitos + DV = 9
        val cuerpo = limpio.dropLast(1)
        val dvIngresado = limpio.last().uppercaseChar()
        if (!cuerpo.all { it.isDigit() }) return false
        return calcularDv(cuerpo) == dvIngresado
    }

    /**
     * Formatea un RUT al estilo "15.420.891-K".
     * Si no es válido, devuelve el original sin modificar.
     */
    fun formatear(rut: String): String {
        val limpio = limpiar(rut)
        if (limpio.length !in 8..9) return rut
        val cuerpo = limpio.dropLast(1)
        val dv = limpio.last().uppercaseChar()
        val cuerpoConPuntos = cuerpo.reversed().chunked(3).joinToString(".").reversed()
        return "$cuerpoConPuntos-$dv"
    }
}

