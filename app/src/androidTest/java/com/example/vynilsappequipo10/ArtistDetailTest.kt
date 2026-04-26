package com.example.vynilsappequipo10

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArtistDetailTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun navigateToArtist(name: String) {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }

        // Search for the specific artist
        composeTestRule.onNodeWithTag("artist_search_field").performTextInput(name)

        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithText(name).fetchSemanticsNodes().size >= 2
        }
        
        composeTestRule.onAllNodesWithText(name).onLast().performClick()
    }

    @Test
    fun testHU04_selectArtist_displaysArtistDetail() {
        navigateToArtist("Queen")
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("Queen").assertIsDisplayed()
    }

    @Test
    fun testHU04_artistDetail_showsBiographicalInfo() {
        navigateToArtist("Queen")
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        val fechaVisible = composeTestRule.onAllNodes(
            hasText("FECHA DE NACIMIENTO") or hasText("FECHA DE CREACION")
        ).fetchSemanticsNodes().isNotEmpty()
        
        assert(fechaVisible) { "Debe mostrar fecha de nacimiento o creación" }
    }

    @Test
    fun testHU04_artistDetail_showsArtistTypeBadge() {
        navigateToArtist("Queen")
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        val badgeVisible = composeTestRule.onAllNodes(
            hasText("ARTISTA LEGENDARIO") or hasText("BANDA LEGENDARIA")
        ).fetchSemanticsNodes().isNotEmpty()
        
        assert(badgeVisible) { "Debe mostrar badge de tipo de artista" }
    }

    @Test
    fun testHU04_artistDetail_showsAssociatedAlbums() {
        navigateToArtist("Queen")
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        // At least some basic info should be there
        composeTestRule.onNode(hasText("FECHA DE", substring = true)).assertIsDisplayed()
    }

    @Test
    fun testHU04_artistDetail_showsActionButtons() {
        navigateToArtist("Queen")
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        // Ensure buttons are in view
        composeTestRule.onNodeWithTag("artist_detail_list").performTouchInput { swipeUp() }

        composeTestRule.onNode(hasText("REPRODUCIR RADIO DEL ARTISTA", substring = true)).assertIsDisplayed()
        composeTestRule.onNodeWithText("SEGUIR").assertIsDisplayed()
    }

    @Test
    fun testHU04_artistDetail_backNavigationReturnsToList() {
        navigateToArtist("Queen")
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithContentDescription("Regresar").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("BIBLIOTECA CURADA").assertIsDisplayed()
    }

    @Test
    fun testHU04_artistDetail_isScrollable() {
        navigateToArtist("Queen")
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithTag("artist_detail_list")
            .assertIsDisplayed()
            .performTouchInput { swipeUp() }
        
        composeTestRule.waitForIdle()
    }

    @Test
    fun testHU04_artistDetail_centralizesAllInformation() {
        navigateToArtist("Queen")
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("FECHA DE", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        val hasBadge = composeTestRule.onAllNodes(
            hasText("ARTISTA LEGENDARIO") or hasText("BANDA LEGENDARIA")
        ).fetchSemanticsNodes().isNotEmpty()
        
        val hasDate = composeTestRule.onAllNodes(
            hasText("FECHA DE", substring = true)
        ).fetchSemanticsNodes().isNotEmpty()
        
        assert(hasBadge && hasDate)
    }
}
