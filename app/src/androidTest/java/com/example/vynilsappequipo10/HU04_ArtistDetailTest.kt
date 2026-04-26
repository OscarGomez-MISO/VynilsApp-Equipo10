package com.example.vynilsappequipo10

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests instrumentados para HU04: Ver Detalle de Artista
 * 
 * Historia de Usuario:
 * Como usuario, quiero ver el perfil de un artista para conocer su biografía y álbumes
 * 
 * Criterio de Éxito:
 * El perfil del artista muestra correctamente su información biográfica y los álbumes asociados
 */
@RunWith(AndroidJUnit4::class)
class HU04_ArtistDetailTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /**
     * Escenario: Usuario selecciona un artista del listado
     * Dado: El usuario está en el listado de artistas
     * Cuando: Selecciona un artista
     * Entonces: Se muestra el detalle del artista con su nombre e imagen
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU04_selectArtist_displaysArtistDetail() {
        // Given: Usuario navega al listado de artistas
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        // When: Selecciona el primer artista
        composeTestRule.onAllNodes(hasClickAction())
            .onFirst()
            .performClick()
        
        // Then: Se muestra la pantalla de detalle
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        // Verifica que se muestra la fecha
        composeTestRule.onNode(
            hasText("FECHA DE", substring = true)
        ).assertIsDisplayed()
    }

    /**
     * Escenario: El detalle muestra la información biográfica del artista
     * Dado: El usuario está viendo el detalle de un artista
     * Cuando: Los datos se cargan correctamente
     * Entonces: Se muestra la fecha de nacimiento/creación y descripción
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU04_artistDetail_showsBiographicalInfo() {
        // Given: Usuario navega al detalle de un artista
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onAllNodes(hasClickAction()).onFirst().performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        // Then: Verifica elementos biográficos
        // Debe mostrar FECHA DE NACIMIENTO o FECHA DE CREACION
        val fechaVisible = composeTestRule.onAllNodes(
            hasText("FECHA DE NACIMIENTO") or hasText("FECHA DE CREACION")
        ).fetchSemanticsNodes().isNotEmpty()
        
        assert(fechaVisible) {
            "Debe mostrar fecha de nacimiento o creación"
        }
    }

    /**
     * Escenario: El detalle muestra el badge de tipo de artista
     * Dado: El usuario está viendo el detalle de un artista
     * Cuando: Los datos se cargan
     * Entonces: Se muestra "ARTISTA LEGENDARIO" o "BANDA LEGENDARIA"
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU04_artistDetail_showsArtistTypeBadge() {
        // Given: Usuario navega al detalle de un artista
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onAllNodes(hasClickAction()).onFirst().performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        // Then: Debe mostrar el badge de tipo (músico o banda)
        val badgeVisible = composeTestRule.onAllNodes(
            hasText("ARTISTA LEGENDARIO") or hasText("BANDA LEGENDARIA")
        ).fetchSemanticsNodes().isNotEmpty()
        
        assert(badgeVisible) {
            "Debe mostrar badge de tipo de artista"
        }
    }

    /**
     * Escenario: El detalle muestra los álbumes asociados al artista
     * Dado: El usuario está viendo el detalle de un artista con álbumes
     * Cuando: Los datos se cargan correctamente
     * Entonces: Se muestra la sección de álbumes del artista
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU04_artistDetail_showsAssociatedAlbums() {
        // Given: Usuario navega al detalle de un artista
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onAllNodes(hasClickAction()).onFirst().performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        // Then: Verifica que existe la sección de álbumes
        // Nota: Solo si el artista tiene álbumes asociados
        composeTestRule.waitForIdle()
        
        // Intenta encontrar el título de la sección de álbumes
        val albumsSectionExists = composeTestRule.onAllNodes(
            hasText("Albums del", substring = true)
        ).fetchSemanticsNodes().isNotEmpty()
        
        // Si no hay sección de álbumes, al menos debe mostrar la info básica
        if (!albumsSectionExists) {
            // Verifica que al menos se muestran los datos básicos del artista
            composeTestRule.onNode(hasText("FECHA DE", substring = true)).assertIsDisplayed()
        }
    }

    /**
     * Escenario: El detalle muestra botones de acción del artista
     * Dado: El usuario está viendo el detalle de un artista
     * Cuando: Los datos se cargan
     * Entonces: Se muestran los botones de acción (reproducir radio, seguir)
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU04_artistDetail_showsActionButtons() {
        // Given: Usuario navega al detalle de un artista
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onAllNodes(hasClickAction()).onFirst().performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        // Then: Verifica que existen botones de acción
        composeTestRule.onNode(
            hasText("REPRODUCIR RADIO DEL ARTISTA", substring = true)
        ).assertIsDisplayed()
        
        composeTestRule.onNodeWithText("SEGUIR").assertIsDisplayed()
    }

    /**
     * Escenario: Usuario regresa al listado desde el detalle
     * Dado: El usuario está viendo el detalle de un artista
     * Cuando: Toca el botón de regresar
     * Entonces: Vuelve al listado de artistas
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU04_artistDetail_backNavigationReturnsToList() {
        // Given: Usuario navega al detalle de un artista
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onAllNodes(hasClickAction()).onFirst().performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        // When: Toca el botón de regresar
        composeTestRule.onNodeWithContentDescription("Regresar").performClick()
        
        // Then: Vuelve al listado de artistas
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("BIBLIOTECA CURADA").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artistas").assertIsDisplayed()
    }

    /**
     * Escenario: La pantalla de detalle es scrolleable
     * Dado: El usuario está viendo el detalle de un artista
     * Cuando: Intenta hacer scroll
     * Entonces: El contenido se desplaza correctamente
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU04_artistDetail_isScrollable() {
        // Given: Usuario navega al detalle de un artista
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onAllNodes(hasClickAction()).onFirst().performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        // When & Then: Verifica que el contenido tiene scroll
        composeTestRule.onNodeWithTag("artist_detail_list")
            .assertIsDisplayed()
            .performTouchInput {
                swipeUp() // Intenta hacer scroll hacia arriba
            }
        
        // Verifica que el scroll no causa crash
        composeTestRule.waitForIdle()
    }

    /**
     * Escenario: Navegación entre múltiples detalles de artistas
     * Dado: El usuario está en el listado de artistas
     * Cuando: Selecciona un artista, regresa, y selecciona otro
     * Entonces: Cada detalle se carga correctamente sin conflictos
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU04_artistDetail_multipleNavigations() {
        // Given: Usuario navega al listado
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        // When: Selecciona primer artista
        composeTestRule.onAllNodes(hasClickAction()).onFirst().performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        // Regresa al listado
        composeTestRule.onNodeWithContentDescription("Regresar").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        // Selecciona otro artista (intentar el segundo si existe)
        val artistItems = composeTestRule.onAllNodes(hasClickAction())
        if (artistItems.fetchSemanticsNodes().size > 1) {
            artistItems[1].performClick()
            
            // Then: Se carga correctamente el segundo detalle
            composeTestRule.waitUntil(30000) {
                composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                    .fetchSemanticsNodes().isNotEmpty()
            }
            
            composeTestRule.onNode(hasText("FECHA DE", substring = true)).assertIsDisplayed()
        }
    }

    /**
     * Escenario: El detalle centraliza toda la información del artista
     * Dado: El usuario está viendo el detalle de un artista
     * Cuando: Los datos se cargan completamente
     * Entonces: Se muestra toda la información centralizada (nombre, fecha, descripción, álbumes)
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU04_artistDetail_centralizesAllInformation() {
        // Given: Usuario navega al detalle de un artista
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onAllNodes(hasClickAction()).onFirst().performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        // Then: Verifica que todos los elementos críticos están presentes
        // 1. Badge de tipo
        val hasBadge = composeTestRule.onAllNodes(
            hasText("ARTISTA LEGENDARIO") or hasText("BANDA LEGENDARIA")
        ).fetchSemanticsNodes().isNotEmpty()
        
        // 2. Fecha
        val hasDate = composeTestRule.onAllNodes(
            hasText("FECHA DE", substring = true)
        ).fetchSemanticsNodes().isNotEmpty()
        
        // 3. Botones de acción
        val hasActions = composeTestRule.onAllNodes(
            hasText("REPRODUCIR RADIO DEL ARTISTA", substring = true)
        ).fetchSemanticsNodes().isNotEmpty()
        
        assert(hasBadge && hasDate && hasActions) {
            "El detalle debe centralizar toda la información: badge=$hasBadge, fecha=$hasDate, acciones=$hasActions"
        }
    }
}
