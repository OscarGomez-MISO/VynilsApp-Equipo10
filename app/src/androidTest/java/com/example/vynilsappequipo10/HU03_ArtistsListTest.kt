package com.example.vynilsappequipo10

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests instrumentados para HU03: Listar Artistas
 * 
 * Historia de Usuario:
 * Como usuario, quiero ver el listado de artistas para explorar los músicos disponibles
 * 
 * Criterio de Éxito:
 * El listado de artistas se despliega correctamente permitiendo la exploración del catálogo
 */
@RunWith(AndroidJUnit4::class)
class HU03_ArtistsListTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /**
     * Escenario: Usuario navega a la sección de artistas como visitante
     * Dado: El usuario está en la pantalla de bienvenida
     * Cuando: Selecciona "EXPLORAR COMO VISITANTE" y navega a "ARTISTAS"
     * Entonces: Se muestra el listado de artistas con título y encabezado
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU03_displayArtistsList_showsHeaderAndTitle() {
        // Given: Usuario en pantalla de bienvenida
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        
        // When: Navega a la pestaña de artistas
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        // Wait for list to load
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        // Then: Verifica elementos de la UI del listado
        composeTestRule.onNodeWithText("BIBLIOTECA CURADA").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artistas").assertIsDisplayed()
        
        // Verifica que existe el texto descriptivo con el contador
        composeTestRule.onNode(
            hasText("Un índice editorial de tu paisaje sonoro", substring = true)
        ).assertIsDisplayed()
    }

    /**
     * Escenario: El listado muestra múltiples artistas
     * Dado: El usuario está en el listado de artistas
     * Cuando: Los datos se cargan correctamente
     * Entonces: Se muestran múltiples artistas en la lista
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU03_displayArtistsList_showsMultipleArtists() {
        // Given: Usuario navega al listado
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        // Wait for list to load
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        // Then: Verifica que hay múltiples elementos clickeables (artistas)
        val clickableItems = composeTestRule.onAllNodes(hasClickAction())
        
        // Debe haber al menos 2 artistas más las tabs de navegación
        assert(clickableItems.fetchSemanticsNodes().size >= 3) {
            "Se esperan al menos 2 artistas en el listado"
        }
    }

    /**
     * Escenario: Verificar que la búsqueda de artistas está disponible
     * Dado: El usuario está en el listado de artistas
     * Cuando: Se carga la pantalla
     * Entonces: Se muestra el campo de búsqueda
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU03_displayArtistsList_showsSearchBar() {
        // Given: Usuario navega al listado
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        // Wait for list to load
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        // Then: Verifica que existe el campo de búsqueda
        composeTestRule.onNode(
            hasText("Buscar artistas, bandas...", substring = true)
        ).assertIsDisplayed()
    }

    /**
     * Escenario: Usuario puede hacer scroll en el listado
     * Dado: El usuario está en el listado de artistas con varios elementos
     * Cuando: Intenta hacer scroll
     * Entonces: La lista se desplaza correctamente
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU03_displayArtistsList_scrollable() {
        // Given: Usuario navega al listado
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        // Wait for list to load
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        // When & Then: Verifica que la lista tiene testTag y es scrolleable
        composeTestRule.onNodeWithTag("artist_detail_list")
            .assertIsDisplayed()
            .performScrollToIndex(0) // Verifica que el scroll funciona
    }

    /**
     * Escenario: Navegación desde otras pestañas a artistas
     * Dado: El usuario está en la pestaña de álbumes
     * Cuando: Toca la pestaña de artistas
     * Entonces: Navega correctamente al listado de artistas
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU03_navigateToArtists_fromOtherTabs() {
        // Given: Usuario entra como visitante (inicia en VINILOS)
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        
        // Espera que cargue la vista de álbumes
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("TOTAL DE LPS", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        // When: Navega a ARTISTAS
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        // Then: Se muestra el listado de artistas
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("BIBLIOTECA CURADA").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artistas").assertIsDisplayed()
    }

    /**
     * Escenario: Listado persiste al cambiar de pestaña y volver
     * Dado: El usuario ha cargado el listado de artistas
     * Cuando: Cambia a otra pestaña y vuelve a artistas
     * Entonces: Los datos siguen estando disponibles sin recargar
     */
    @Ignore("Requiere conectividad de red al backend")
    @Test
    fun testHU03_displayArtistsList_persistsOnTabChange() {
        // Given: Usuario navega a artistas
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        // When: Cambia a VINILOS y vuelve a ARTISTAS
        composeTestRule.onNodeWithText("VINILOS").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        // Then: Los datos siguen disponibles (sin loading prolongado)
        composeTestRule.onNodeWithText("BIBLIOTECA CURADA").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artistas").assertIsDisplayed()
    }
}
