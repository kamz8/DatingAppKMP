package com.example.datingappkmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.datingappkmp.data.DatabaseDriverFactory
import com.example.datingappkmp.data.QuestionRepository
import com.example.datingappkmp.data.models.DeviceType
import com.example.datingappkmp.database.PlayerConfig
import com.example.datingappkmp.presentation.navigation.Screen
import com.example.datingappkmp.presentation.screens.*
import com.example.datingappkmp.presentation.theme.ParyTalkTheme
import com.example.datingappkmp.presentation.viewmodels.GameViewModel
import com.example.datingappkmp.presentation.viewmodels.HistoryViewModel
import com.example.datingappkmp.presentation.viewmodels.SetupViewModel
import kotlinx.coroutines.launch

@Composable
fun App(databaseDriverFactory: DatabaseDriverFactory, deviceType: DeviceType) {
    // Initialize repository
    val repository = remember { QuestionRepository(databaseDriverFactory) }
    val scope = rememberCoroutineScope()

    // Check if user has already completed setup
    var hasCompletedSetup by remember { mutableStateOf(false) }
    var isCheckingSetup by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // Check if player config exists
        val config = repository.getPlayerConfig()
        hasCompletedSetup = config != null
        isCheckingSetup = false
    }

    ParyTalkTheme {
        if (isCheckingSetup) {
            // Show loading while checking setup status
            Surface {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            // Navigation state
            var currentScreen by remember {
                mutableStateOf<Screen>(
                    if (hasCompletedSetup) Screen.Home else Screen.Welcome
                )
            }
            var playerConfig by remember { mutableStateOf<PlayerConfig?>(null) }

            LaunchedEffect(Unit) {
                playerConfig = repository.getPlayerConfig()
            }

            when (val screen = currentScreen) {
                Screen.Welcome -> {
                    val setupViewModel = remember { SetupViewModel(repository, deviceType) }
                    WelcomeScreen(
                        viewModel = setupViewModel,
                        onSetupComplete = {
                            scope.launch {
                                playerConfig = repository.getPlayerConfig()
                            }
                            currentScreen = Screen.Home
                        },
                        onNavigateToManualSetup = { playerName ->
                            currentScreen = Screen.ManualSetup(playerName)
                        },
                        onNavigateToNFCSetup = { playerName ->
                            // TODO: Implement NFC setup screen
                            // For now, just show manual setup
                            currentScreen = Screen.ManualSetup(playerName)
                        }
                    )
                }

                is Screen.ManualSetup -> {
                    val setupViewModel = remember { SetupViewModel(repository, deviceType) }
                    ManualSetupScreen(
                        viewModel = setupViewModel,
                        playerName = screen.playerName,
                        onBack = {
                            currentScreen = Screen.Welcome
                        },
                        onSetupComplete = {
                            scope.launch {
                                playerConfig = repository.getPlayerConfig()
                            }
                            currentScreen = Screen.Home
                        }
                    )
                }

                Screen.Home -> {
                    LaunchedEffect(currentScreen) {
                        playerConfig = repository.getPlayerConfig()
                    }

                    HomeScreen(
                        playerConfig = playerConfig,
                        onNavigateToGame = {
                            currentScreen = Screen.Game
                        },
                        onNavigateToHistory = {
                            currentScreen = Screen.History
                        }
                    )
                }

                Screen.Game -> {
                    val gameViewModel = remember { GameViewModel(repository) }
                    GameScreen(
                        viewModel = gameViewModel,
                        onBackToHome = {
                            currentScreen = Screen.Home
                        }
                    )
                }

                Screen.History -> {
                    val historyViewModel = remember { HistoryViewModel(repository) }
                    HistoryScreen(
                        viewModel = historyViewModel,
                        onBack = {
                            currentScreen = Screen.Home
                        }
                    )
                }
            }
        }
    }
}
