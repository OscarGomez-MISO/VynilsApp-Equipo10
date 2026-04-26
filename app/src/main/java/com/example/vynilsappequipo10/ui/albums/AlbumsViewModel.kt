package com.example.vynilsappequipo10.ui.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.data.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AlbumsUiState(
    val albums: List<Album> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class AlbumsViewModel(
    private val repository: AlbumRepository = AlbumRepository()
) : ViewModel() {
    private val allAlbums = mutableListOf<Album>()

    private val _uiState = MutableStateFlow(AlbumsUiState(isLoading = true))
    val uiState: StateFlow<AlbumsUiState> = _uiState.asStateFlow()

    init {
        loadAlbums()
    }

    fun loadAlbums() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val albums = repository.getAlbums()
                allAlbums.clear()
                allAlbums.addAll(albums)
                _uiState.update { it.copy(albums = albums, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error desconocido") }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                albums = if (query.isBlank()) allAlbums.toList()
                         else allAlbums.filter {
                             it.name.contains(query, ignoreCase = true) ||
                             it.genre.contains(query, ignoreCase = true)
                         }
            )
        }
    }
}
