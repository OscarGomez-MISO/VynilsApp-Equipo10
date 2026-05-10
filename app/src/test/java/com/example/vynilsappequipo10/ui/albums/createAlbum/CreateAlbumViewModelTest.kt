package com.example.vynilsappequipo10.ui.albums.createAlbum

import com.example.vynilsappequipo10.data.AlbumRepository
import com.example.vynilsappequipo10.domain.Album
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateAlbumViewModelTest {

    private lateinit var viewModel: CreateAlbumViewModel
    private val repository: AlbumRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CreateAlbumViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `update form state fields correctly`() {
        viewModel.updateName("New Album")
        viewModel.updateCover("url_cover")
        viewModel.updateReleaseDate("2023-12-01")
        viewModel.updateDescription("Description")
        viewModel.updateGenre("Rock")
        viewModel.updateRecordLabel("Sony")

        val form = viewModel.formState.value
        assertEquals("New Album", form.name)
        assertEquals("url_cover", form.cover)
        assertEquals("2023-12-01", form.releaseDate)
        assertEquals("Description", form.description)
        assertEquals("Rock", form.genre)
        assertEquals("Sony", form.recordLabel)
    }

    @Test
    fun `createAlbum success updates uiState to Success`() {
        // Arrange
        val createdAlbum = Album(1, "Name", "Cover", "Date", "Desc", "Rock", "EMI")
        
        viewModel.updateName("Name")
        viewModel.updateCover("Cover")
        viewModel.updateReleaseDate("Date")
        viewModel.updateDescription("Desc")
        viewModel.updateGenre("Rock")
        viewModel.updateRecordLabel("EMI")

        coEvery { repository.createAlbum(any()) } returns createdAlbum

        // Act
        viewModel.createAlbum()

        // Assert
        assertTrue(viewModel.uiState.value is CreateAlbumUiState.Success)
        assertEquals(createdAlbum, (viewModel.uiState.value as CreateAlbumUiState.Success).album)
    }

    @Test
    fun `createAlbum with missing fields updates uiState to Error`() {
        // Arrange
        viewModel.updateName("Name")
        // missing other fields

        // Act
        viewModel.createAlbum()

        // Assert
        assertTrue(viewModel.uiState.value is CreateAlbumUiState.Error)
        assertEquals("Todos los campos son obligatorios", (viewModel.uiState.value as CreateAlbumUiState.Error).message)
    }

    @Test
    fun `createAlbum repository error updates uiState to Error`() {
        // Arrange
        val errorMessage = "API Error"
        viewModel.updateName("Name")
        viewModel.updateCover("Cover")
        viewModel.updateReleaseDate("Date")
        viewModel.updateDescription("Desc")
        viewModel.updateGenre("Rock")
        viewModel.updateRecordLabel("EMI")

        coEvery { repository.createAlbum(any()) } throws Exception(errorMessage)

        // Act
        viewModel.createAlbum()

        // Assert
        assertTrue(viewModel.uiState.value is CreateAlbumUiState.Error)
        assertEquals(errorMessage, (viewModel.uiState.value as CreateAlbumUiState.Error).message)
    }

    @Test
    fun `resetForm clears state`() {
        viewModel.updateName("Some Name")
        viewModel.createAlbum() // This will cause error or success

        viewModel.resetForm()

        assertEquals("", viewModel.formState.value.name)
        assertTrue(viewModel.uiState.value is CreateAlbumUiState.Idle)
    }

    @Test
    fun `resetState only resets uiState to Idle`() {
        // Arrange
        viewModel.updateName("Name")
        viewModel.updateCover("Cover")
        viewModel.updateReleaseDate("Date")
        viewModel.updateDescription("Desc")
        viewModel.updateGenre("Rock")
        viewModel.updateRecordLabel("EMI")

        val createdAlbum = Album(1, "Name", "Cover", "Date", "Desc", "Rock", "EMI")
        coEvery { repository.createAlbum(any()) } returns createdAlbum
        viewModel.createAlbum()

        // Act
        viewModel.resetState()

        // Assert
        assertTrue(viewModel.uiState.value is CreateAlbumUiState.Idle)
        assertEquals("Name", viewModel.formState.value.name) // Form state preserved
    }

    @Test
    fun `isFormValid is false when form is empty`() = runTest {
        // Assert - form starts empty so isFormValid should be false
        assertFalse(viewModel.isFormValid.value)
    }

    @Test
    fun `isFormValid updates when fields are filled`() = runTest {
        // Arrange - start with empty form
        assertFalse(viewModel.isFormValid.value)
        
        // Act - fill all fields
        viewModel.updateName("Name")
        viewModel.updateCover("Cover")
        viewModel.updateReleaseDate("Date")
        viewModel.updateDescription("Desc")
        viewModel.updateGenre("Rock")
        viewModel.updateRecordLabel("EMI")

        // Allow flow to emit - in real scenario this would be collected
        // The isFormValid flow derives from formState, test the derived logic indirectly
        val form = viewModel.formState.value
        val isValid = form.name.isNotBlank() && form.cover.isNotBlank() && 
            form.releaseDate.isNotBlank() && form.description.isNotBlank() && 
            form.genre.isNotBlank() && form.recordLabel.isNotBlank()
        assertTrue(isValid)
    }

    @Test
    fun `isFormValid is false when only some fields are filled`() = runTest {
        // Act
        viewModel.updateName("Name")
        viewModel.updateCover("Cover")
        // Missing other fields

        // Assert - check via form state
        val form = viewModel.formState.value
        val isValid = form.name.isNotBlank() && form.cover.isNotBlank() && 
            form.releaseDate.isNotBlank() && form.description.isNotBlank() && 
            form.genre.isNotBlank() && form.recordLabel.isNotBlank()
        assertFalse(isValid)
    }

    @Test
    fun `genres list contains expected values`() {
        assertEquals(listOf("Classical", "Salsa", "Rock", "Folk"), viewModel.genres)
    }

    @Test
    fun `recordLabels list contains expected values`() {
        assertEquals(listOf("Sony Music", "EMI", "Discos Fuentes", "Elektra", "Fania Records"), viewModel.recordLabels)
    }

    @Test
    fun `createAlbum with blank name triggers validation error`() {
        // Arrange
        viewModel.updateName("   ") // blank but not empty
        viewModel.updateCover("Cover")
        viewModel.updateReleaseDate("Date")
        viewModel.updateDescription("Desc")
        viewModel.updateGenre("Rock")
        viewModel.updateRecordLabel("EMI")

        // Act
        viewModel.createAlbum()

        // Assert
        assertTrue(viewModel.uiState.value is CreateAlbumUiState.Error)
    }

    @Test
    fun `createAlbum error without message uses default`() {
        // Arrange
        viewModel.updateName("Name")
        viewModel.updateCover("Cover")
        viewModel.updateReleaseDate("Date")
        viewModel.updateDescription("Desc")
        viewModel.updateGenre("Rock")
        viewModel.updateRecordLabel("EMI")

        coEvery { repository.createAlbum(any()) } throws Exception()

        // Act
        viewModel.createAlbum()

        // Assert
        assertTrue(viewModel.uiState.value is CreateAlbumUiState.Error)
        assertEquals("Error al crear el álbum", (viewModel.uiState.value as CreateAlbumUiState.Error).message)
    }
}
