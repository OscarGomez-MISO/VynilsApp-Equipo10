package com.example.vynilsappequipo10.ui.albums.albumDetail

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.vynilsappequipo10.ui.theme.VynilsTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CommentSheetComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun clearSession() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences("vynils_prefs", Context.MODE_PRIVATE).edit().clear().commit()
    }

    @Test
    fun commentSheet_displaysRequiredFields() {
        composeTestRule.setContent {
            VynilsTheme {
                // We show the sheet by clicking the FAB in AlbumDetailScreen or just testing the sheet if it was standalone.
                // Since CommentSheet is internal to AlbumDetailScreen, we trigger it through the screen.
                AlbumDetailScreen(
                    albumId = 1,
                    onBackClick = {},
                    isCollector = true
                )
            }
        }

        // Open sheet
        composeTestRule.onNodeWithTag("add_comment_fab").performClick()

        // Check fields
        composeTestRule.onNodeWithText("Agregar Comentario").assertIsDisplayed()
        composeTestRule.onNodeWithTag("comment_description_field").assertIsDisplayed()
        composeTestRule.onNodeWithTag("comment_send_button").assertExists()
        
        // Check if email field or "Changing user" message is present
        // Since session might be empty in test, email field should show
        composeTestRule.onNodeWithTag("comment_email_field").assertExists()
    }

    @Test
    fun commentSheet_sendButton_disabledWhenEmpty() {
        composeTestRule.setContent {
            VynilsTheme {
                AlbumDetailScreen(
                    albumId = 1,
                    onBackClick = {},
                    isCollector = true
                )
            }
        }

        composeTestRule.onNodeWithTag("add_comment_fab").performClick()
        
        // Initially empty, button should be disabled
        composeTestRule.onNodeWithTag("comment_send_button").assertIsNotEnabled()
    }
}
