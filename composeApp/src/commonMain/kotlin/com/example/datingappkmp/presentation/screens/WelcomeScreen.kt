package com.example.datingappkmp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.datingappkmp.presentation.theme.Blue
import com.example.datingappkmp.presentation.theme.Pink
import com.example.datingappkmp.presentation.viewmodels.SetupState
import com.example.datingappkmp.presentation.viewmodels.SetupViewModel

@Composable
fun WelcomeScreen(
    viewModel: SetupViewModel,
    onSetupComplete: () -> Unit,
    onNavigateToManualSetup: (String) -> Unit,
    onNavigateToNFCSetup: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is SetupState.Success) {
            onSetupComplete()
        }
    }

    WelcomeScreenContent(
        state = state,
        onSoloMode = { playerName ->
            viewModel.startSoloMode(playerName)
        },
        onManualSetup = { playerName ->
            onNavigateToManualSetup(playerName)
        },
        onNFCSetup = { playerName ->
            onNavigateToNFCSetup(playerName)
        },
        onClearError = { viewModel.clearError() }
    )
}

@Composable
fun WelcomeScreenContent(
    state: SetupState,
    onSoloMode: (String) -> Unit,
    onManualSetup: (String) -> Unit,
    onNFCSetup: (String) -> Unit,
    onClearError: () -> Unit
) {
    var playerName by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Pink,
                        Blue
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo/Title
            Text(
                text = "💕",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Pary Talk",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Pytania dla par",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Input Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Witaj! 👋",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    OutlinedTextField(
                        value = playerName,
                        onValueChange = { playerName = it },
                        label = { Text("Twoje imię") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = state !is SetupState.Loading,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Wybierz tryb:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    // Manual Setup Button
                    Button(
                        onClick = { onManualSetup(playerName) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = playerName.isNotBlank() && state !is SetupState.Loading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Pink
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "👫 Setup z partnerem (Manual)",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    // NFC Setup Button
                    OutlinedButton(
                        onClick = { onNFCSetup(playerName) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = playerName.isNotBlank() && state !is SetupState.Loading,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "📱 Setup przez NFC",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    // Solo Mode Button
                    OutlinedButton(
                        onClick = { onSoloMode(playerName) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = playerName.isNotBlank() && state !is SetupState.Loading,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "🎯 Tryb Solo",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    if (state is SetupState.Loading) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = Pink
                        )
                    }
                }
            }
        }

        // Error Snackbar
        if (state is SetupState.Error) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = onClearError) {
                        Text("OK")
                    }
                }
            ) {
                Text(state.message)
            }
        }
    }
}
