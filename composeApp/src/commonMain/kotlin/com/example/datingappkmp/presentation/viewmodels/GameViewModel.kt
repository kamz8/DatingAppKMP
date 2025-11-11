package com.example.datingappkmp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datingappkmp.data.QuestionRepository
import com.example.datingappkmp.database.PlayerConfig
import com.example.datingappkmp.database.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameState(
    val currentQuestion: Question? = null,
    val playerConfig: PlayerConfig? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showFirstTouchAnimation: Boolean = false
)

class GameViewModel(
    private val repository: QuestionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    init {
        loadPlayerConfig()
        loadNextQuestion()
    }

    private fun loadPlayerConfig() {
        viewModelScope.launch {
            try {
                val config = repository.getPlayerConfig()
                _state.update { it.copy(playerConfig = config) }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to load player config: ${e.message}") }
            }
        }
    }

    fun loadNextQuestion() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val question = repository.getRandomQuestion()
                _state.update {
                    it.copy(
                        currentQuestion = question,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load question: ${e.message}"
                    )
                }
            }
        }
    }

    fun recordQuestion(isFirstTouch: Boolean = false) {
        viewModelScope.launch {
            val question = _state.value.currentQuestion ?: return@launch

            try {
                // Get category info
                val categories = repository.getAllCategories()
                val category = categories.find { it.id == question.categoryId }

                if (category != null) {
                    repository.insertQuestionHistory(
                        questionId = question.id,
                        questionText = question.text,
                        categoryId = category.id,
                        categoryName = category.name,
                        categoryEmoji = category.emoji,
                        askedAt = System.currentTimeMillis(),
                        yourTurn = true,
                        isFirstTouch = isFirstTouch
                    )

                    if (isFirstTouch) {
                        _state.update { it.copy(showFirstTouchAnimation = true) }
                        // Auto-hide animation after showing
                        kotlinx.coroutines.delay(3000)
                        _state.update { it.copy(showFirstTouchAnimation = false) }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to record question: ${e.message}") }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
