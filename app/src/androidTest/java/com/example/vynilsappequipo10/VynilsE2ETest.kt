package com.example.vynilsappequipo10

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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

        // Wait for list to load
        composeTestRule.waitUntil(15000) {
            composeTestRule.onAllNodesWithText("TOTAL DE LPS").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Agregar").assertIsDisplayed()

        // Click on first album card
        // We use a matcher that looks for any node with a click action that likely represents an album
        composeTestRule.onAllNodes(hasClickAction())
            .filter(hasContentDescription("", substring = false).not()) // Filter out nodes with content description if any
            .onFirst()
            .performClick()

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

        composeTestRule.waitUntil(15000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("BIBLIOTECA CURADA").assertIsDisplayed()

        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()
        composeTestRule.onNodeWithText("Funcionalidad en desarrollo").assertIsDisplayed()
    }

    @Test
    fun testLogoutFlow_ReturnsToWelcome() {
        composeTestRule.onNodeWithText("ENTRAR COMO COLECCIONISTA  →").performClick()

        composeTestRule.waitUntil(15000) {
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

        // Wait for list to load and click the first artist row
        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }

        // Click first artist available in the list
        composeTestRule.onAllNodes(hasClickAction())
            .onFirst()
            .performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("FECHA DE", substring = true).assertIsDisplayed()
    }

    @Test
    fun testArtistDetail_BackNavigationReturnsToList() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodes(hasClickAction())
            .onFirst()
            .performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Regresar").performClick()

        composeTestRule.waitUntil(15000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("BIBLIOTECA CURADA").assertIsDisplayed()
    }
}
