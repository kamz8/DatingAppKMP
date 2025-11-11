package com.example.datingappkmp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datingappkmp.data.QuestionRepository
import com.example.datingappkmp.database.Category
import com.example.datingappkmp.database.QuestionHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryState(
    val entries: List<QuestionHistory> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Long? = null,
    val firstTouchEntry: QuestionHistory? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class HistoryViewModel(
    private val repository: QuestionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        loadCategories()
        loadHistory()
        loadFirstTouch()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = repository.getAllCategories()
                _state.update { it.copy(categories = categories) }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to load categories: ${e.message}") }
            }
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val categoryId = _state.value.selectedCategoryId
                val entries = if (categoryId != null) {
                    repository.getHistoryByCategory(categoryId)
                } else {
                    repository.getQuestionHistory()
                }
                _state.update {
                    it.copy(
                        entries = entries,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load history: ${e.message}"
                    )
                }
            }
        }
    }

    private fun loadFirstTouch() {
        viewModelScope.launch {
            try {
                val firstTouch = repository.getFirstTouchEntry()
                _state.update { it.copy(firstTouchEntry = firstTouch) }
            } catch (e: Exception) {
                // Non-critical, just log
                println("Failed to load first touch: ${e.message}")
            }
        }
    }

    fun filterByCategory(categoryId: Long?) {
        _state.update { it.copy(selectedCategoryId = categoryId) }
        loadHistory()
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun refresh() {
        loadHistory()
        loadFirstTouch()
    }
}
