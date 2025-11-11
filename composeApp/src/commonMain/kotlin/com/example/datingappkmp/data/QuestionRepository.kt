package com.example.datingappkmp.data

import com.example.datingappkmp.data.models.DeviceType
import com.example.datingappkmp.data.models.SetupMethod
import com.example.datingappkmp.database.AppDatabase
import com.example.datingappkmp.database.Category
import com.example.datingappkmp.database.PlayerConfig
import com.example.datingappkmp.database.Question
import com.example.datingappkmp.database.QuestionHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class QuestionRepository(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.databaseQueries

    // Category operations
    suspend fun getAllCategories(): List<Category> = withContext(Dispatchers.IO) {
        try {
            dbQuery.getAllCategories().executeAsList()
        } catch (e: Exception) {
            println("Error getting categories: ${e.message}")
            emptyList()
        }
    }

    suspend fun insertCategory(name: String, emoji: String) = withContext(Dispatchers.IO) {
        try {
            dbQuery.insertCategory(name, emoji)
        } catch (e: Exception) {
            println("Error inserting category: ${e.message}")
        }
    }

    suspend fun deleteAllCategories() = withContext(Dispatchers.IO) {
        try {
            dbQuery.deleteAllCategories()
        } catch (e: Exception) {
            println("Error deleting categories: ${e.message}")
        }
    }

    // Question operations
    suspend fun getAllQuestions(): List<Question> = withContext(Dispatchers.IO) {
        try {
            dbQuery.getAllQuestions().executeAsList()
        } catch (e: Exception) {
            println("Error getting questions: ${e.message}")
            emptyList()
        }
    }

    suspend fun getRandomQuestion(): Question? = withContext(Dispatchers.IO) {
        try {
            dbQuery.getRandomQuestion().executeAsOneOrNull()
        } catch (e: Exception) {
            println("Error getting random question: ${e.message}")
            null
        }
    }

    suspend fun insertQuestion(categoryId: Long, text: String, createdAt: Long) = withContext(Dispatchers.IO) {
        try {
            dbQuery.insertQuestion(categoryId, text, createdAt)
        } catch (e: Exception) {
            println("Error inserting question: ${e.message}")
        }
    }

    suspend fun deleteAllQuestions() = withContext(Dispatchers.IO) {
        try {
            dbQuery.deleteAllQuestions()
        } catch (e: Exception) {
            println("Error deleting questions: ${e.message}")
        }
    }

    // PlayerConfig operations
    suspend fun getPlayerConfig(): PlayerConfig? = withContext(Dispatchers.IO) {
        try {
            dbQuery.getPlayerConfig().executeAsOneOrNull()
        } catch (e: Exception) {
            println("Error getting player config: ${e.message}")
            null
        }
    }

    suspend fun savePlayerConfig(
        playerId: String,
        playerName: String,
        partnerId: String?,
        partnerName: String?,
        deviceType: DeviceType,
        setupMethod: SetupMethod,
        setupDate: Long
    ) = withContext(Dispatchers.IO) {
        try {
            // Delete existing config first
            dbQuery.deletePlayerConfig()
            // Insert new config
            dbQuery.insertPlayerConfig(
                playerId = playerId,
                playerName = playerName,
                partnerId = partnerId,
                partnerName = partnerName,
                deviceType = deviceType.name,
                setupMethod = setupMethod.name,
                setupDate = setupDate
            )
        } catch (e: Exception) {
            println("Error saving player config: ${e.message}")
        }
    }

    suspend fun deletePlayerConfig() = withContext(Dispatchers.IO) {
        try {
            dbQuery.deletePlayerConfig()
        } catch (e: Exception) {
            println("Error deleting player config: ${e.message}")
        }
    }

    // QuestionHistory operations
    suspend fun getQuestionHistory(limit: Int = 100): List<QuestionHistory> = withContext(Dispatchers.IO) {
        try {
            dbQuery.getHistoryLimit(limit.toLong()).executeAsList()
        } catch (e: Exception) {
            println("Error getting question history: ${e.message}")
            emptyList()
        }
    }

    suspend fun insertQuestionHistory(
        questionId: Long,
        questionText: String,
        categoryId: Long,
        categoryName: String,
        categoryEmoji: String,
        askedAt: Long,
        yourTurn: Boolean,
        isFirstTouch: Boolean
    ) = withContext(Dispatchers.IO) {
        try {
            dbQuery.insertQuestionHistory(
                questionId = questionId,
                questionText = questionText,
                categoryId = categoryId,
                categoryName = categoryName,
                categoryEmoji = categoryEmoji,
                askedAt = askedAt,
                yourTurn = if (yourTurn) 1L else 0L,
                isFirstTouch = if (isFirstTouch) 1L else 0L
            )
        } catch (e: Exception) {
            println("Error inserting question history: ${e.message}")
        }
    }

    suspend fun getHistoryByCategory(categoryId: Long): List<QuestionHistory> = withContext(Dispatchers.IO) {
        try {
            dbQuery.getHistoryByCategory(categoryId).executeAsList()
        } catch (e: Exception) {
            println("Error getting history by category: ${e.message}")
            emptyList()
        }
    }

    suspend fun getFirstTouchEntry(): QuestionHistory? = withContext(Dispatchers.IO) {
        try {
            dbQuery.getFirstTouchEntry().executeAsOneOrNull()
        } catch (e: Exception) {
            println("Error getting first touch entry: ${e.message}")
            null
        }
    }

    suspend fun deleteHistory() = withContext(Dispatchers.IO) {
        try {
            dbQuery.deleteAllHistory()
        } catch (e: Exception) {
            println("Error deleting history: ${e.message}")
        }
    }
}
