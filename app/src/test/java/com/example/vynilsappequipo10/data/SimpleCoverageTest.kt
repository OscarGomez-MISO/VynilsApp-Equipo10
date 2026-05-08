package com.example.vynilsappequipo10.data

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Test simple para verificar que JaCoCo está reportando cobertura correctamente a Sonar.
 */
class SimpleCoverageTest {

    @Test
    fun `verificar suma simple`() {
        val suma = 2 + 2
        assertEquals(4, suma)
    }

    @Test
    fun `verificar concatenacion de strings`() {
        val texto = "Hola " + "Mundo"
        assertEquals("Hola Mundo", texto)
    }
}
