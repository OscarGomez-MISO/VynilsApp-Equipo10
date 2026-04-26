package com.example.vynilsappequipo10.ui.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynilsappequipo10.data.ArtistRepository
import com.example.vynilsappequipo10.domain.Artist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ArtistsUiState(
    val artists: List<Artist> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class ArtistsViewModel : ViewModel() {
    private val repository = ArtistRepository()
    private val allArtists = mutableListOf<Artist>()

    private val _uiState = MutableStateFlow(ArtistsUiState(isLoading = true))
    val uiState: StateFlow<ArtistsUiState> = _uiState.asStateFlow()

    init {
        loadArtists()
    }

    fun loadArtists() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val artists = repository.getArtists()
                allArtists.clear()
                allArtists.addAll(artists)
                _uiState.update { it.copy(artists = artists, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error desconocido") }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                artists = if (query.isBlank()) allArtists.toList()
                          else allArtists.filter {
                              it.name.contains(query, ignoreCase = true)
                          }
            )
        }
    }
}
