package com.example.vynilsappequipo10

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VynilsE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCollectorFlow_NavigatesToListAndDetail() {
        composeTestRule.onNodeWithText("ENTRAR COMO COLECCIONISTA  →").performClick()

        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithText("TOTAL DE LPS").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Agregar").assertIsDisplayed()

        composeTestRule.onAllNodesWithText("Salsa").onFirst().performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("Descripción").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Detalle del Álbum").assertIsDisplayed()
        composeTestRule.onNodeWithText("Descripción").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Regresar").performClick()
        composeTestRule.onNodeWithText("Vinilos").assertIsDisplayed()
    }

    @Test
    fun testNavigationTabs_ShowsArtistsListAndDevelopmentMessage() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()

        composeTestRule.onNodeWithText("ARTISTAS").performClick()

        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("BIBLIOTECA CURADA").assertIsDisplayed()

        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()
        composeTestRule.onNodeWithText("Funcionalidad en desarrollo").assertIsDisplayed()
    }

    @Test
    fun testLogoutFlow_ReturnsToWelcome() {
        composeTestRule.onNodeWithText("ENTRAR COMO COLECCIONISTA  →").performClick()

        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithContentDescription("Boton Inicio").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Boton Inicio").performClick()

        composeTestRule.onNodeWithText("Tu bóveda sonora te espera").assertIsDisplayed()
    }

    @Test
    fun testVisitorFlow_DoesNotShowAddButton() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()

        composeTestRule.onNodeWithContentDescription("Agregar").assertDoesNotExist()
    }

    // HU04: Visualizar detalle de artista

    @Test
    fun testArtistDetail_ShowsMusicianInformation() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("Rubén Blades Bellido de Luna").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Rubén Blades Bellido de Luna").performClick()

        composeTestRule.onNodeWithText("Rubén Blades Bellido de Luna").assertIsDisplayed()
        composeTestRule.onNodeWithText("ARTISTA LEGENDARIO").assertIsDisplayed()
        composeTestRule.onNodeWithText("FECHA DE NACIMIENTO").assertIsDisplayed()
        composeTestRule.onNodeWithText("1948-07-16").assertIsDisplayed()
    }

    @Test
    fun testArtistDetail_ShowsAssociatedAlbums() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("Rubén Blades Bellido de Luna").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Rubén Blades Bellido de Luna").performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("FECHA DE NACIMIENTO").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Buscando América").assertExists()
        composeTestRule.onNodeWithText("Poeta del pueblo").assertExists()
    }

    @Test
    fun testArtistDetail_BackNavigationReturnsToList() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("Rubén Blades Bellido de Luna").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Rubén Blades Bellido de Luna").performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("FECHA DE NACIMIENTO").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Regresar").performClick()

        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("BIBLIOTECA CURADA").assertIsDisplayed()
    }
}