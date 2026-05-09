package com.example.vynilsappequipo10.ui.collectors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynilsappequipo10.data.CollectorRepository
import com.example.vynilsappequipo10.domain.Collector
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CollectorsUiState(
    val collectors: List<Collector> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class CollectorsViewModel(
    private val repository: CollectorRepository = CollectorRepository()
) : ViewModel() {
    private val allCollectors = mutableListOf<Collector>()

    private val _uiState = MutableStateFlow(CollectorsUiState(isLoading = true))
    val uiState: StateFlow<CollectorsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCollectors()
    }

    fun loadCollectors() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val collectors = repository.getCollectors()
                allCollectors.clear()
                allCollectors.addAll(collectors)
                _uiState.update { it.copy(collectors = collectors, isLoading = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error desconocido") }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                collectors = if (query.isBlank()) allCollectors.toList()
                             else allCollectors.filter {
                                 it.name.contains(query, ignoreCase = true)
                             }
            )
        }
    }
}
