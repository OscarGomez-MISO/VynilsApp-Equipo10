package com.example.vynilsappequipo10.ui.collectors.collectorDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynilsappequipo10.data.CollectorRepository
import com.example.vynilsappequipo10.domain.CollectorAlbumWithAlbum
import com.example.vynilsappequipo10.domain.CollectorDetail
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CollectorDetailUiState(
    val collector: CollectorDetail? = null,
    val collectorAlbums: List<CollectorAlbumWithAlbum> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class CollectorDetailViewModel(
    private val repository: CollectorRepository = CollectorRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectorDetailUiState())
    val uiState: StateFlow<CollectorDetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    fun loadCollector(id: Int) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = CollectorDetailUiState(isLoading = true)
            try {
                val detailDeferred = async { repository.getCollectorById(id) }
                val albumsDeferred = async { repository.getCollectorAlbums(id) }
                _uiState.value = CollectorDetailUiState(
                    collector = detailDeferred.await(),
                    collectorAlbums = albumsDeferred.await(),
                    isLoading = false
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = CollectorDetailUiState(
                    isLoading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }
}
