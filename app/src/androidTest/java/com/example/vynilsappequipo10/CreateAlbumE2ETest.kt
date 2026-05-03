package com.example.vynilsappequipo10

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateAlbumE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCreateAlbumFlow_Success() {
        // 1. Ingresar como Coleccionista
        composeTestRule.onNodeWithText("COLECCIONISTA", substring = true).performClick()

        // 2. Click en el FAB para crear álbum
        composeTestRule.onNodeWithTag("fab_create_album").performClick()

        // 3. Llenar el formulario
        composeTestRule.onNodeWithTag("album_name_field").performTextInput("Album Test Espresso")
        composeTestRule.onNodeWithTag("album_cover_field").performTextInput("https://upload.wikimedia.org/wikipedia/en/3/3b/Dark_Side_of_the_Moon.png")
        
        // Seleccionar Fecha (abre DatePickerDialog nativo)
        composeTestRule.onNodeWithTag("album_date_field").performClick()
        try {
            onView(withText("OK")).perform(click())
        } catch (_: Exception) {
            try {
                onView(withText("Aceptar")).perform(click())
            } catch (_: Exception) {}
        }

        composeTestRule.onNodeWithTag("album_description_field").performTextInput("Esta es una descripción de prueba para el álbum creado desde un test E2E.")
        
        // Seleccionar Género
        composeTestRule.onNodeWithTag("album_genre_field").performClick()
        composeTestRule.onNodeWithTag("genre_option_Rock").performClick()

        // Seleccionar Sello Discográfico
        composeTestRule.onNodeWithTag("album_record_label_field").performClick()
        composeTestRule.onNodeWithTag("record_label_option_EMI").performClick()

        // 4. Click en el botón Crear
        composeTestRule.onNodeWithTag("create_album_button").performClick()

        // 5. Verificar mensaje de éxito
        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithTag("create_album_success").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("¡Álbum creado exitosamente!").assertIsDisplayed()
    }

    @Test
    fun testCreateAlbumFlow_ValidationError() {
        // 1. Ingresar como Coleccionista
        composeTestRule.onNodeWithText("COLECCIONISTA", substring = true).performClick()

        // 2. Click en el FAB para crear álbum
        composeTestRule.onNodeWithTag("fab_create_album").performClick()

        // 3. Click en el botón Crear sin llenar nada
        composeTestRule.onNodeWithTag("create_album_button").performClick()

        // 4. Verificar mensaje de error
        composeTestRule.onNodeWithText("Todos los campos son obligatorios").assertIsDisplayed()
    }

    @Test
    fun testCreateAlbum_VisitorCannotSeeFAB() {
        // 1. Ingresar como Visitante
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()

        // 2. Verificar que el FAB NO existe
        composeTestRule.onNodeWithTag("fab_create_album").assertDoesNotExist()
    }
}
