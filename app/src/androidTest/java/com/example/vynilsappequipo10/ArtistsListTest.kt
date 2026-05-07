package com.example.vynilsappequipo10

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArtistsListTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testHU03_displayArtistsList_showsHeaderAndTitle() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("BIBLIOTECA CURADA").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artistas").assertIsDisplayed()
    }

    @Test
    fun testHU03_displayArtistsList_showsMultipleArtists() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        // Search for "Queen" to ensure deterministic results
        composeTestRule.onNodeWithTag("artist_search_field").performTextInput("Queen")
        
        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithText("Queen").fetchSemanticsNodes().size >= 2
        }

        composeTestRule.onAllNodesWithText("Queen").onLast().assertIsDisplayed()
    }

    @Test
    fun testHU03_displayArtistsList_showsSearchBar() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithTag("artist_search_field").assertIsDisplayed()
    }

    @Test
    fun testHU03_displayArtistsList_scrollable() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithTag("artist_detail_list")
            .assertIsDisplayed()
            .performTouchInput { swipeUp() }
    }

    @Test
    fun testHU03_navigateToArtists_fromOtherTabs() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodes(hasText("TOTAL DE LPS", substring = true))
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("BIBLIOTECA CURADA").assertIsDisplayed()
    }

    @Test
    fun testHU03_displayArtistsList_persistsOnTabChange() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("ÁLBUMES").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        
        composeTestRule.onNodeWithText("BIBLIOTECA CURADA").assertIsDisplayed()
    }
}
