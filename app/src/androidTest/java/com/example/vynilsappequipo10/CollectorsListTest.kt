package com.example.vynilsappequipo10

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectorsListTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testHU05_displayCollectorsList_showsHeaderAndTitle() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("SOCIEDAD DE BUSCADORES DE JOYAS")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("SOCIEDAD DE BUSCADORES DE JOYAS").assertIsDisplayed()
        composeTestRule.onNodeWithText("Los Coleccionistas").assertIsDisplayed()
    }

    @Test
    fun testHU05_displayCollectorsList_showsMultipleCollectors() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("SOCIEDAD DE BUSCADORES DE JOYAS")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("collectors_list").assertIsDisplayed()
    }

    @Test
    fun testHU05_displayCollectorsList_showsSearchBar() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("SOCIEDAD DE BUSCADORES DE JOYAS")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("collector_search_field").assertIsDisplayed()
    }

    @Test
    fun testHU05_displayCollectorsList_scrollable() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("SOCIEDAD DE BUSCADORES DE JOYAS")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("collectors_list")
            .assertIsDisplayed()
            .performTouchInput { swipeUp() }
    }

    @Test
    fun testHU05_navigateToCollectors_fromOtherTabs() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodes(hasText("TOTAL DE LPS", substring = true))
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("SOCIEDAD DE BUSCADORES DE JOYAS")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("SOCIEDAD DE BUSCADORES DE JOYAS").assertIsDisplayed()
    }

    // HU06: Visualizar detalle de coleccionista

    @Test
    fun testHU06_collectorDetail_showsNameAndStats() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("Manolo Bellon").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Manolo Bellon").performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithTag("collector_detail_content").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Manolo Bellon").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("ÁLBUMES").onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithText("ARTISTAS").onFirst().assertIsDisplayed()
        composeTestRule.onNodeWithText("RATING").assertIsDisplayed()
    }

    @Test
    fun testHU06_collectorDetail_showsCollection() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("Manolo Bellon").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Manolo Bellon").performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("Mi Colección").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Mi Colección").assertIsDisplayed()
    }

    @Test
    fun testHU06_collectorDetail_albumClickNavigatesToAlbumDetail() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("Manolo Bellon").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Manolo Bellon").performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("Mi Colección").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Buscando América").performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("Detalle del Álbum").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Detalle del Álbum").assertIsDisplayed()
    }

    @Test
    fun testHU06_collectorDetail_backNavigationReturnsList() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("Manolo Bellon").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Manolo Bellon").performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithTag("collector_detail_content").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Regresar").performClick()

        composeTestRule.waitUntil(15000) {
            composeTestRule.onAllNodesWithText("Los Coleccionistas").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Los Coleccionistas").assertIsDisplayed()
    }

    @Test
    fun testHU05_displayCollectorsList_persistsOnTabChange() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("SOCIEDAD DE BUSCADORES DE JOYAS")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("ÁLBUMES").performClick()
        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodes(hasText("TOTAL DE LPS", substring = true))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()

        composeTestRule.onNodeWithText("SOCIEDAD DE BUSCADORES DE JOYAS").assertIsDisplayed()
    }
}
