package com.example.vynilsappequipo10.ui.albums.createAlbum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynilsappequipo10.data.AlbumRepository
import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.domain.AlbumRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class CreateAlbumUiState {
    object Idle : CreateAlbumUiState()
    object Loading : CreateAlbumUiState()
    data class Success(val album: Album) : CreateAlbumUiState()
    data class Error(val message: String) : CreateAlbumUiState()
}

data class AlbumFormState(
    val name: String = "",
    val cover: String = "",
    val releaseDate: String = "",
    val description: String = "",
    val genre: String = "",
    val recordLabel: String = ""
)

class CreateAlbumViewModel(
    private val repository: AlbumRepository = AlbumRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateAlbumUiState>(CreateAlbumUiState.Idle)
    val uiState: StateFlow<CreateAlbumUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(AlbumFormState())
    val formState: StateFlow<AlbumFormState> = _formState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = _formState
        .map { form ->
            form.name.isNotBlank() && form.cover.isNotBlank() && form.releaseDate.isNotBlank() &&
                form.description.isNotBlank() && form.genre.isNotBlank() && form.recordLabel.isNotBlank()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val genres = listOf("Classical", "Salsa", "Rock", "Folk")
    val recordLabels = listOf("Sony Music", "EMI", "Discos Fuentes", "Elektra", "Fania Records")

    private var isSubmitting = false

    fun updateName(name: String) {
        _formState.value = _formState.value.copy(name = name)
    }

    fun updateCover(cover: String) {
        _formState.value = _formState.value.copy(cover = cover)
    }

    fun updateReleaseDate(releaseDate: String) {
        _formState.value = _formState.value.copy(releaseDate = releaseDate)
    }

    fun updateDescription(description: String) {
        _formState.value = _formState.value.copy(description = description)
    }

    fun updateGenre(genre: String) {
        _formState.value = _formState.value.copy(genre = genre)
    }

    fun updateRecordLabel(recordLabel: String) {
        _formState.value = _formState.value.copy(recordLabel = recordLabel)
    }

    fun createAlbum() {
        if (isSubmitting) return
        val form = _formState.value

        if (form.name.isBlank() || form.cover.isBlank() || form.releaseDate.isBlank() ||
            form.description.isBlank() || form.genre.isBlank() || form.recordLabel.isBlank()) {
            _uiState.value = CreateAlbumUiState.Error("Todos los campos son obligatorios")
            return
        }

        isSubmitting = true
        viewModelScope.launch {
            _uiState.value = CreateAlbumUiState.Loading
            try {
                val albumRequest = AlbumRequest(
                    name = form.name,
                    cover = form.cover,
                    releaseDate = form.releaseDate,
                    description = form.description,
                    genre = form.genre,
                    recordLabel = form.recordLabel
                )
                val createdAlbum = repository.createAlbum(albumRequest)
                _uiState.value = CreateAlbumUiState.Success(createdAlbum)
            } catch (e: Exception) {
                _uiState.value = CreateAlbumUiState.Error(e.message ?: "Error al crear el álbum")
            } finally {
                isSubmitting = false
            }
        }
    }

    fun resetState() {
        _uiState.value = CreateAlbumUiState.Idle
    }

    fun resetForm() {
        _formState.value = AlbumFormState()
        _uiState.value = CreateAlbumUiState.Idle
    }
}
