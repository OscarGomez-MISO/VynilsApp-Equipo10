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
        // 1. Verificar pantalla de bienvenida y entrar como coleccionista
        composeTestRule.onNodeWithText("ENTRAR COMO COLECCIONISTA  →").performClick()

        // 2. Verificar que estamos en la lista de álbumes
        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithText("TOTAL DE LPS").fetchSemanticsNodes().isNotEmpty()
        }
        
        // 3. Verificar que el botón flotante (+) existe para el coleccionista
        composeTestRule.onNodeWithContentDescription("Agregar").assertIsDisplayed()

        // 4. Click en un álbum (basado en el texto 'Salsa' que es común en el API para el primer álbum)
        composeTestRule.onAllNodesWithText("Salsa").onFirst().performClick()

        // 5. Verificar que estamos en el detalle
        composeTestRule.onNodeWithText("Detalle del Álbum").assertIsDisplayed()
        composeTestRule.onNodeWithText("Descripción").assertIsDisplayed()
        
        // 6. Volver atrás
        composeTestRule.onNodeWithContentDescription("Regresar").performClick()
        composeTestRule.onNodeWithText("Vinilos").assertIsDisplayed()
    }

    @Test
    fun testNavigationTabs_ShowsDevelopmentMessage() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()

        // Navegar a Artistas
        composeTestRule.onNodeWithText("ARTISTAS").performClick()
        composeTestRule.onNodeWithText("Funcionalidad en desarrollo").assertIsDisplayed()

        // Navegar a Coleccionistas
        composeTestRule.onNodeWithText("COLECCIONISTAS").performClick()
        composeTestRule.onNodeWithText("Funcionalidad en desarrollo").assertIsDisplayed()
    }

    @Test
    fun testLogoutFlow_ReturnsToWelcome() {
        composeTestRule.onNodeWithText("ENTRAR COMO COLECCIONISTA  →").performClick()

        // Esperar a que el botón de Inicio aparezca (usando la descripción semántica)
        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithContentDescription("Boton Inicio").fetchSemanticsNodes().isNotEmpty()
        }

        // Click en el botón de Inicio
        composeTestRule.onNodeWithContentDescription("Boton Inicio").performClick()

        // Verificar que estamos de nuevo en la bienvenida
        composeTestRule.onNodeWithText("Tu bóveda sonora te espera").assertIsDisplayed()
    }

    @Test
    fun testVisitorFlow_DoesNotShowAddButton() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()

        // Verificar que el botón (+) NO existe para el visitante
        composeTestRule.onNodeWithContentDescription("Agregar").assertDoesNotExist()
    }
}
