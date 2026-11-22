package com.example.oftapp.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RutValidatorTest {

    @Test
    fun `RUT valido con digito numerico`() {
        assertTrue(RutValidator.esValido("12.345.678-5"))
        assertTrue(RutValidator.esValido("123456785"))
    }

    @Test
    fun `RUT valido con digito K`() {
        assertTrue(RutValidator.esValido("15.420.895-K"))
        assertTrue(RutValidator.esValido("15420895k"))
    }

    @Test
    fun `RUT valido con digito cero`() {
        // RUT cuyo DV es 0 comprobado matemáticamente (suma = 22 -> 22 % 11 = 0 -> DV = 0)
        val cuerpo = "28000000"
        val dv = RutValidator.calcularDv(cuerpo)
        assertEquals('0', dv)
        assertTrue(RutValidator.esValido("$cuerpo$dv"))
    }

    @Test
    fun `RUT invalido por digito verificador incorrecto`() {
        assertFalse(RutValidator.esValido("15.420.891-9"))
        assertFalse(RutValidator.esValido("12.345.678-0"))
    }

    @Test
    fun `RUT invalido por longitud`() {
        assertFalse(RutValidator.esValido("123"))
        assertFalse(RutValidator.esValido("1234567890123"))
        assertFalse(RutValidator.esValido(""))
    }

    @Test
    fun `RUT invalido con caracteres no permitidos en el cuerpo`() {
        assertFalse(RutValidator.esValido("12.34A.678-5"))
        assertFalse(RutValidator.esValido("ABCDEFGH-K"))
    }

    @Test
    fun `formatear agrega puntos y guion correctamente`() {
        assertEquals("15.420.891-K", RutValidator.formatear("15420891K"))
        assertEquals("15.420.891-K", RutValidator.formatear("15.420.891-K"))
        assertEquals("12.345.678-5", RutValidator.formatear("123456785"))
    }

    @Test
    fun `formatear devuelve original si el RUT no tiene largo valido`() {
        assertEquals("123", RutValidator.formatear("123"))
        assertEquals("", RutValidator.formatear(""))
    }
}

