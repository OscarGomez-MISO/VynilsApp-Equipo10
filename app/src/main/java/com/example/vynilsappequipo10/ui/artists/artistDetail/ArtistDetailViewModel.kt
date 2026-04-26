package com.example.vynilsappequipo10.ui.artists.artistDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynilsappequipo10.data.ArtistRepository
import com.example.vynilsappequipo10.domain.Artist
import com.example.vynilsappequipo10.domain.ArtistType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ArtistDetailUiState(
    val artist: Artist? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ArtistDetailViewModel(
    private val repository: ArtistRepository = ArtistRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistDetailUiState())
    val uiState: StateFlow<ArtistDetailUiState> = _uiState.asStateFlow()

    fun loadArtist(id: Int, type: ArtistType) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val artist = repository.getArtistById(id, type)
                _uiState.value = _uiState.value.copy(artist = artist, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }
}
