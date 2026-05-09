package com.example.vynilsappequipo10

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import android.content.Context
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VynilsE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        // Limpiar SharedPreferences para asegurar independencia de tests
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences("vynils_prefs", Context.MODE_PRIVATE).edit().clear().commit()
    }

    @Test
    fun testCollectorFlow_NavigatesToListAndDetail() {
        composeTestRule.onNodeWithText("ENTRAR COMO COLECCIONISTA  →").performClick()

        // Wait for list to load
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("TOTAL DE LPS").fetchSemanticsNodes().isNotEmpty()
        }

        // Wait for FAB to be displayed
        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithTag("fab_create_album").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("fab_create_album").assertIsDisplayed()

        // Search for the album using the testTag to find a specific album to be deterministic
        composeTestRule.onNodeWithTag("album_search_field").performTextInput("Salsa")
        
        composeTestRule.waitUntil(20000) {
            // Find "Salsa" in the list results, not the search bar
            composeTestRule.onAllNodesWithText("Salsa").fetchSemanticsNodes().size >= 2
        }

        // Click the album (likely the last one found which is the card, first is the search text)
        composeTestRule.onAllNodesWithText("Salsa").onLast().performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodesWithText("Descripción").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Detalle del Álbum").assertIsDisplayed()
        composeTestRule.onNodeWithText("Descripción").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Regresar").performClick()
        
        composeTestRule.waitUntil(15000) {
            composeTestRule.onAllNodesWithText("Vinilos").fetchSemanticsNodes().isNotEmpty()
        }
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
        composeTestRule.waitUntil(15000) {
            composeTestRule.onAllNodesWithText("SOCIEDAD DE BUSCADORES DE JOYAS")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("SOCIEDAD DE BUSCADORES DE JOYAS").assertIsDisplayed()
    }

    @Test
    fun testLogoutFlow_ReturnsToWelcome() {
        composeTestRule.onNodeWithText("ENTRAR COMO COLECCIONISTA  →").performClick()

        composeTestRule.waitUntil(15000) {
            composeTestRule.onAllNodes(hasContentDescription("Boton Inicio")).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Boton Inicio").performClick()

        composeTestRule.onNodeWithText("Tu bóveda sonora te espera").assertIsDisplayed()
    }

    @Test
    fun testVisitorFlow_DoesNotShowAddButton() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()

        // 1. Verificar que en la lista NO hay botón de agregar
        composeTestRule.onNodeWithTag("fab_create_album").assertDoesNotExist()

        // 2. Navegar al detalle de un álbum
        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("Salsa").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onAllNodesWithText("Salsa").onLast().performClick()

        // 3. Verificar que en el detalle NO existe el botón de agregar comentario
        composeTestRule.onNodeWithTag("add_comment_fab").assertDoesNotExist()
    }

    @Test
    fun testCollectorCommentFlow_NewUserRegistration() {
        composeTestRule.onNodeWithText("ENTRAR COMO COLECCIONISTA  →").performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodes(hasText("Salsa")).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onAllNodesWithText("Salsa").onLast().performClick()

        composeTestRule.waitUntil(25000) {
            composeTestRule.onAllNodes(hasTestTag("add_comment_fab")).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("add_comment_fab").performClick()

        // Registro con datos dinámicos para evitar colisiones
        val timestamp = System.currentTimeMillis() % 1000000
        val testEmail = "test.user.$timestamp@gmail.com"
        
        composeTestRule.onNodeWithTag("comment_email_field").performTextInput(testEmail)
        composeTestRule.onNodeWithTag("comment_description_field").performTextInput("Excelente álbum - Test Automatizado $timestamp")
        composeTestRule.onNodeWithTag("comment_send_button").performClick()

        // Esperar detección de perfil nuevo
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodes(hasText("Perfil no encontrado", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }
        
        val randomPhone = "313${(1000000..9999999).random()}"
        composeTestRule.onNodeWithTag("comment_name_field").performTextInput("Oscar Gomez $timestamp")
        composeTestRule.onNodeWithTag("comment_phone_field").performTextInput(randomPhone)

        composeTestRule.onNodeWithTag("comment_send_button").performScrollTo().performClick()

        // VALIDACIÓN MEJORADA: Esperar éxito O error para no colgarse 45s
        composeTestRule.waitUntil(30000) {
            val hasSuccess = composeTestRule.onAllNodes(hasTestTag("comment_success_message")).fetchSemanticsNodes().isNotEmpty()
            val hasError = composeTestRule.onAllNodes(hasTestTag("comment_error_message")).fetchSemanticsNodes().isNotEmpty()
            hasSuccess || hasError
        }
        
        // Si hay error, mostrar por qué falló el test
        val errorNodes = composeTestRule.onAllNodes(hasTestTag("comment_error_message")).fetchSemanticsNodes()
        if (errorNodes.isNotEmpty()) {
            throw AssertionError("El API devolvió un error al registrar el comentario/coleccionista. Revisa los logs.")
        }

        // Verificar que realmente se cierre después del éxito
        composeTestRule.waitUntil(15000) {
            composeTestRule.onAllNodes(hasTestTag("comment_success_message")).fetchSemanticsNodes().isEmpty()
        }
    }

    @Test
    fun testCollectorCommentFlow_SwitchAccount() {
        composeTestRule.onNodeWithText("ENTRAR COMO COLECCIONISTA  →").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodes(hasText("Salsa")).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onAllNodesWithText("Salsa").onLast().performClick()

        // 1. Identificarse para crear una sesión activa
        composeTestRule.waitUntil(25000) {
            composeTestRule.onAllNodes(hasTestTag("add_comment_fab")).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("add_comment_fab").performClick()
        
        // Simular identificación con un email único
        val timestamp = System.currentTimeMillis() % 1000000
        val switchTestEmail = "tester.user$timestamp@gmail.com"
        composeTestRule.onNodeWithTag("comment_email_field").performTextInput(switchTestEmail)
        composeTestRule.onNodeWithTag("comment_description_field").performTextInput("Sesion de prueba automatizada $timestamp")
        composeTestRule.onNodeWithTag("comment_send_button").performScrollTo().performClick()
        
        // Esperar cierre o avance a perfil (usando testTag del sheet para no depender de textos que cambian con el estado)
        composeTestRule.waitUntil(45000) {
            val isSheetOpen = composeTestRule.onAllNodes(hasTestTag("comment_sheet_content")).fetchSemanticsNodes().isNotEmpty()
            val needsProfile = composeTestRule.onAllNodes(hasText("Perfil no encontrado", substring = true)).fetchSemanticsNodes().isNotEmpty()
            val hasSuccess = composeTestRule.onAllNodes(hasTestTag("comment_success_message")).fetchSemanticsNodes().isNotEmpty()
            val hasError = composeTestRule.onAllNodes(hasTestTag("comment_error_message")).fetchSemanticsNodes().isNotEmpty()

            !isSheetOpen || needsProfile || hasSuccess || hasError
        }

        // Si se quedó en perfil, completamos los datos
        if (composeTestRule.onAllNodes(hasText("Perfil no encontrado", substring = true)).fetchSemanticsNodes().isNotEmpty()) {
            val randomPhone = "313${(1000000..9999999).random()}"
            composeTestRule.onNodeWithTag("comment_name_field").performTextInput("Oscar Tester $timestamp")
            composeTestRule.onNodeWithTag("comment_phone_field").performTextInput(randomPhone)
            composeTestRule.onNodeWithTag("comment_send_button").performScrollTo().performClick()
        }

        // Si ya hay resultado del primer wait, no esperar de nuevo
        val alreadyResolved = composeTestRule.onAllNodes(hasTestTag("comment_success_message")).fetchSemanticsNodes().isNotEmpty() ||
            composeTestRule.onAllNodes(hasTestTag("comment_error_message")).fetchSemanticsNodes().isNotEmpty()

        if (!alreadyResolved) {
            composeTestRule.waitUntil(45000) {
                val hasSuccess = composeTestRule.onAllNodes(hasTestTag("comment_success_message")).fetchSemanticsNodes().isNotEmpty()
                val hasError = composeTestRule.onAllNodes(hasTestTag("comment_error_message")).fetchSemanticsNodes().isNotEmpty()
                hasSuccess || hasError
            }
        }

        if (composeTestRule.onAllNodes(hasTestTag("comment_error_message")).fetchSemanticsNodes().isNotEmpty()) {
            throw AssertionError("El API devolvió un error en SwitchAccount. Revisa los logs.")
        }
        
        // Esperar a que la hoja se cierre realmente (desaparezca el mensaje de éxito)
        composeTestRule.waitUntil(15000) {
            composeTestRule.onAllNodes(hasTestTag("comment_success_message")).fetchSemanticsNodes().isEmpty()
        }

        // 2. Re-abrir y verificar que existe la sesión persistida
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("add_comment_fab").performClick()
        
        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodes(hasText("Publicando como:", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }
        
        // 3. Probar la funcionalidad de "Cambiar cuenta"
        composeTestRule.onNodeWithText("¿No eres tú? Cambiar cuenta").performClick()
        
        composeTestRule.onNodeWithTag("comment_email_field").assertIsDisplayed()
    }

    // HU04: Visualizar detalle de artista

    @Test
    fun testArtistDetail_ShowsMusicianInformation() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()

        // Wait for list to load
        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }

        // Search for "Queen" using the testTag
        composeTestRule.onNodeWithTag("artist_search_field").performTextInput("Queen")

        // Wait for filtering and click on Queen (excluding the search field)
        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithText("Queen").fetchSemanticsNodes().size >= 2
        }
        composeTestRule.onAllNodesWithText("Queen").onLast().performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodes(hasText("FECHA DE", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Queen").assertIsDisplayed()
        composeTestRule.onNodeWithText("FECHA DE", substring = true).assertIsDisplayed()
    }

    @Test
    fun testArtistDetail_BackNavigationReturnsToList() {
        composeTestRule.onNodeWithText("EXPLORAR COMO VISITANTE").performClick()
        composeTestRule.onNodeWithText("ARTISTAS").performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }

        // Search for an artist to make the test deterministic
        composeTestRule.onNodeWithTag("artist_search_field").performTextInput("Queen")

        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithText("Queen").fetchSemanticsNodes().size >= 2
        }
        
        composeTestRule.onAllNodesWithText("Queen").onLast().performClick()

        composeTestRule.waitUntil(30000) {
            composeTestRule.onAllNodes(hasText("FECHA DE", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Regresar").performClick()

        composeTestRule.waitUntil(20000) {
            composeTestRule.onAllNodesWithText("BIBLIOTECA CURADA").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("BIBLIOTECA CURADA").assertIsDisplayed()
    }
}
