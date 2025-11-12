package com.example.datingappkmp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datingappkmp.data.QuestionRepository
import com.example.datingappkmp.data.SeedData
import com.example.datingappkmp.data.models.DeviceType
import com.example.datingappkmp.data.models.SetupMethod
import com.example.datingappkmp.database.PlayerConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed class SetupState {
    object Initial : SetupState()
    object Loading : SetupState()
    data class NFCReady(val playerName: String) : SetupState()
    data class Success(val config: PlayerConfig) : SetupState()
    data class Error(val message: String) : SetupState()
}

class SetupViewModel(
    private val repository: QuestionRepository,
    private val deviceType: DeviceType
) : ViewModel() {

    private val _state = MutableStateFlow<SetupState>(SetupState.Initial)
    val state: StateFlow<SetupState> = _state.asStateFlow()

    init {
        initializeDatabase()
    }

    private fun initializeDatabase() {
        viewModelScope.launch {
            _state.value = SetupState.Loading
            try {
                // Seed database if needed
                SeedData.seedDatabase(repository)
                _state.value = SetupState.Initial
            } catch (e: Exception) {
                _state.value = SetupState.Error("Failed to initialize database: ${e.message}")
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class, kotlin.time.ExperimentalTime::class)
    fun startManualSetup(playerName: String, partnerName: String) {
        if (playerName.isBlank()) {
            _state.value = SetupState.Error("Player name cannot be empty")
            return
        }

        if (partnerName.isBlank()) {
            _state.value = SetupState.Error("Partner name cannot be empty")
            return
        }

        viewModelScope.launch {
            _state.value = SetupState.Loading
            try {
                val playerId = Uuid.random().toString()
                val partnerId = Uuid.random().toString()

                repository.savePlayerConfig(
                    playerId = playerId,
                    playerName = playerName,
                    partnerId = partnerId,
                    partnerName = partnerName,
                    deviceType = deviceType,
                    setupMethod = SetupMethod.MANUAL,
                    setupDate = Clock.System.now().toEpochMilliseconds()
                )

                val config = repository.getPlayerConfig()
                if (config != null) {
                    _state.value = SetupState.Success(config)
                } else {
                    _state.value = SetupState.Error("Failed to save configuration")
                }
            } catch (e: Exception) {
                _state.value = SetupState.Error("Setup failed: ${e.message}")
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class, kotlin.time.ExperimentalTime::class)
    fun startSoloMode(playerName: String) {
        if (playerName.isBlank()) {
            _state.value = SetupState.Error("Player name cannot be empty")
            return
        }

        viewModelScope.launch {
            _state.value = SetupState.Loading
            try {
                val playerId = Uuid.random().toString()

                repository.savePlayerConfig(
                    playerId = playerId,
                    playerName = playerName,
                    partnerId = null,
                    partnerName = null,
                    deviceType = deviceType,
                    setupMethod = SetupMethod.SOLO,
                    setupDate = Clock.System.now().toEpochMilliseconds()
                )

                val config = repository.getPlayerConfig()
                if (config != null) {
                    _state.value = SetupState.Success(config)
                } else {
                    _state.value = SetupState.Error("Failed to save configuration")
                }
            } catch (e: Exception) {
                _state.value = SetupState.Error("Solo mode setup failed: ${e.message}")
            }
        }
    }

    fun startNFCSetup(playerName: String) {
        if (playerName.isBlank()) {
            _state.value = SetupState.Error("Player name cannot be empty")
            return
        }
        _state.value = SetupState.NFCReady(playerName)
    }

    @OptIn(ExperimentalUuidApi::class, kotlin.time.ExperimentalTime::class)
    fun onNFCDataReceived(playerName: String, partnerName: String, partnerId: String) {
        viewModelScope.launch {
            _state.value = SetupState.Loading
            try {
                val playerId = Uuid.random().toString()

                repository.savePlayerConfig(
                    playerId = playerId,
                    playerName = playerName,
                    partnerId = partnerId,
                    partnerName = partnerName,
                    deviceType = deviceType,
                    setupMethod = SetupMethod.NFC,
                    setupDate = Clock.System.now().toEpochMilliseconds()
                )

                val config = repository.getPlayerConfig()
                if (config != null) {
                    _state.value = SetupState.Success(config)
                } else {
                    _state.value = SetupState.Error("Failed to save NFC configuration")
                }
            } catch (e: Exception) {
                _state.value = SetupState.Error("NFC setup failed: ${e.message}")
            }
        }
    }

    fun clearError() {
        _state.value = SetupState.Initial
    }
}
