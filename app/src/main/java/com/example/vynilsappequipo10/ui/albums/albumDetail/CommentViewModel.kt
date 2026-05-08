package com.example.vynilsappequipo10.ui.albums.albumDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynilsappequipo10.data.AlbumRepository
import com.example.vynilsappequipo10.data.CollectorRepository
import com.example.vynilsappequipo10.domain.CollectorRequest
import com.example.vynilsappequipo10.domain.CollectorIdRequest
import com.example.vynilsappequipo10.domain.CommentRequest
import com.example.vynilsappequipo10.ui.main.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CommentUiState {
    object Idle : CommentUiState()
    object Loading : CommentUiState()
    data class Success(val message: String) : CommentUiState()
    data class Error(val message: String) : CommentUiState()
    data class NeedProfile(val email: String) : CommentUiState()
}

class CommentViewModel(
    private val albumRepository: AlbumRepository = AlbumRepository(),
    private val collectorRepository: CollectorRepository = CollectorRepository(),
    private val userSession: UserSession
) : ViewModel() {

    private val _state = MutableStateFlow<CommentUiState>(CommentUiState.Idle)
    val state = _state.asStateFlow()

    fun postComment(albumId: Int, description: String, rating: Int, email: String, name: String? = null, telephone: String? = null) {
        viewModelScope.launch {
            _state.value = CommentUiState.Loading
            try {
                var collectorId = userSession.getCollectorId()
                Log.d("CommentViewModel", "Initial Collector ID: $collectorId for email: $email")

                if (collectorId == -1 || !userSession.getCollectorEmail().equals(email, ignoreCase = true)) {
                    Log.d("CommentViewModel", "Searching for existing collector: $email")
                    val collectors = collectorRepository.getCollectors()
                    var existing = collectors.find { it.email.trim().equals(email.trim(), ignoreCase = true) }
                    
                    if (existing == null && !name.isNullOrBlank() && !telephone.isNullOrBlank()) {
                        Log.d("CommentViewModel", "Creating new collector: $email")
                        try {
                            existing = collectorRepository.createCollector(CollectorRequest(name = name, telephone = telephone, email = email))
                        } catch (e: Exception) {
                            Log.w("CommentViewModel", "Create failed, checking list one last time", e)
                            // Re-fetch in case of race condition or existing email not found in first fetch
                            val updatedCollectors = collectorRepository.getCollectors()
                            existing = updatedCollectors.find { it.email.trim().equals(email.trim(), ignoreCase = true) }
                            if (existing == null) throw e
                        }
                    }

                    if (existing != null) {
                        collectorId = existing.id ?: -1
                        userSession.saveCollector(collectorId, email)
                        Log.d("CommentViewModel", "Session saved for collector: $collectorId")
                    } else if (name.isNullOrBlank()) {
                        _state.value = CommentUiState.NeedProfile(email)
                        return@launch
                    } else {
                        throw Exception("No se pudo registrar o encontrar al coleccionista")
                    }
                }

                albumRepository.addComment(albumId, CommentRequest(description, rating, CollectorIdRequest(collectorId)))
                _state.value = CommentUiState.Success("Comentario agregado con éxito")
            } catch (e: Exception) {
                Log.e("CommentViewModel", "Error posting comment", e)
                _state.value = CommentUiState.Error(e.message ?: "Error al procesar")
            }
        }
    }

    fun resetState() {
        _state.value = CommentUiState.Idle
    }
}
