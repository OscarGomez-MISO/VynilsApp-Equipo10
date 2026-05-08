package com.example.vynilsappequipo10.data

import com.example.vynilsappequipo10.domain.Album
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Test para verificar que JaCoCo reporta cobertura sobre clases de producción.
 */
class SimpleCoverageTest {

    @Test
    fun `verificar Album domain coverage`() {
        // Usamos la clase Album para asegurar que JaCoCo detecte actividad en el código de producción
        val album = Album(
            id = 1,
            name = "Test Album",
            cover = "url",
            releaseDate = "2024",
            description = "Desc",
            genre = "Rock",
            recordLabel = "Sony"
        )
        assertEquals("Test Album", album.name)
    }

    @Test
    fun `verificar suma simple`() {
        val suma = 2 + 2
        assertEquals(4, suma)
    }
}
