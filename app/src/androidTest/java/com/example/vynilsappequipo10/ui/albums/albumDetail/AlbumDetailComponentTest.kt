package com.example.vynilsappequipo10.ui.albums.albumDetail

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.ui.theme.VynilsTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class AlbumDetailComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel: AlbumDetailViewModel = mockk(relaxed = true)

    private val mockAlbum = Album(
        id = 1,
        name = "Legendary Album",
        cover = "https://example.com/cover.jpg",
        releaseDate = "1970-01-01T00:00:00.000Z",
        description = "This is a legendary description.",
        genre = "Rock",
        recordLabel = "Studio X"
    )

    @Test
    fun albumDetail_displaysLoading() {
        every { viewModel.uiState } returns MutableStateFlow(AlbumDetailUiState(isLoading = true))

        composeTestRule.setContent {
            VynilsTheme {
                AlbumDetailScreen(1, false, {}, viewModel)
            }
        }

        // We can verify that the album content is NOT displayed when loading
        composeTestRule.onNodeWithText("Legendary Album").assertDoesNotExist()
    }

    @Test
    fun albumDetail_displaysError() {
        val errorMsg = "Fallo de conexión"
        every { viewModel.uiState } returns MutableStateFlow(AlbumDetailUiState(error = errorMsg))

        composeTestRule.setContent {
            VynilsTheme {
                AlbumDetailScreen(1, false, {}, viewModel)
            }
        }

        composeTestRule.onNodeWithText("Error: $errorMsg").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reintentar").assertIsDisplayed()
    }

    @Test
    fun albumDetail_displaysAlbumData() {
        every { viewModel.uiState } returns MutableStateFlow(AlbumDetailUiState(album = mockAlbum))

        composeTestRule.setContent {
            VynilsTheme {
                AlbumDetailScreen(1, false, {}, viewModel)
            }
        }

        composeTestRule.onNodeWithText("Legendary Album").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rock • Studio X").assertIsDisplayed()
    }
}
