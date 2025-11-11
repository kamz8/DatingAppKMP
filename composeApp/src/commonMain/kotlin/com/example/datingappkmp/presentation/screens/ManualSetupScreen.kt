package com.example.datingappkmp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.datingappkmp.presentation.theme.Blue
import com.example.datingappkmp.presentation.theme.Pink
import com.example.datingappkmp.presentation.viewmodels.SetupState
import com.example.datingappkmp.presentation.viewmodels.SetupViewModel

@Composable
fun ManualSetupScreen(
    viewModel: SetupViewModel,
    playerName: String,
    onBack: () -> Unit,
    onSetupComplete: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is SetupState.Success) {
            onSetupComplete()
        }
    }

    ManualSetupScreenContent(
        playerName = playerName,
        state = state,
        onConfirm = { partnerName ->
            viewModel.startManualSetup(playerName, partnerName)
        },
        onBack = onBack,
        onClearError = { viewModel.clearError() }
    )
}

@Composable
fun ManualSetupScreenContent(
    playerName: String,
    state: SetupState,
    onConfirm: (String) -> Unit,
    onBack: () -> Unit,
    onClearError: () -> Unit
) {
    var partnerName by remember { mutableStateOf("") }

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
                        text = "Manual Setup",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Twoje imię: $playerName",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    OutlinedTextField(
                        value = partnerName,
                        onValueChange = { partnerName = it },
                        label = { Text("Imię partnera") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = state !is SetupState.Loading,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            enabled = state !is SetupState.Loading,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Wstecz")
                        }

                        Button(
                            onClick = { onConfirm(partnerName) },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            enabled = partnerName.isNotBlank() && state !is SetupState.Loading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Pink
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Potwierdź")
                        }
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
