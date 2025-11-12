package com.example.datingappkmp.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.datingappkmp.presentation.theme.Pink
import com.example.datingappkmp.presentation.theme.PinkLight
import com.example.datingappkmp.presentation.viewmodels.GameState
import com.example.datingappkmp.presentation.viewmodels.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onBackToHome: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    GameScreenContent(
        state = state,
        onNextQuestion = { viewModel.loadNextQuestion() },
        onRecordQuestion = { isFirstTouch ->
            viewModel.recordQuestion(isFirstTouch)
        },
        onResetSession = { viewModel.resetSession() },
        onBackToHome = onBackToHome,
        onClearError = { viewModel.clearError() }
    )
}

@Composable
fun GameScreenContent(
    state: GameState,
    onNextQuestion: () -> Unit,
    onRecordQuestion: (Boolean) -> Unit,
    onResetSession: () -> Unit,
    onBackToHome: () -> Unit,
    onClearError: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding() // Safe area dla iOS
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onBackToHome) {
                        Text("← Home")
                    }

                    state.playerConfig?.let { config ->
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = config.playerName,
                                style = MaterialTheme.typography.titleMedium
                            )
                            config.partnerName?.let {
                                Text(
                                    text = "& $it",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                // Session counter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pytania w sesji: ${state.questionsShownInSession} / ${state.totalQuestions}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )

                    if (state.questionsShownInSession > 0) {
                        TextButton(onClick = onResetSession) {
                            Text(
                                text = "🔄 Nowa sesja",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // Question Card with Animation
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = Pink
                )
            } else {
                AnimatedContent(
                    targetState = state.currentQuestion,
                    transitionSpec = {
                        // Scale + Fade animation for question card
                        (scaleIn(
                            initialScale = 0.8f,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(400))) togetherWith
                        (scaleOut(
                            targetScale = 0.8f,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        ) + fadeOut(animationSpec = tween(400)))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 32.dp),
                    label = "question_animation"
                ) { question ->
                    question?.let {
                        QuestionCard(
                            question = it.text,
                            categoryId = it.categoryId,
                            modifier = Modifier.fillMaxSize()
                        )
                    } ?: run {
                        Text(
                            text = "No questions available.\nPlease seed the database.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        onRecordQuestion(false)
                        onNextQuestion()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = state.currentQuestion != null && !state.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Pink
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "NASTĘPNE PYTANIE",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        // First Touch Animation
        if (state.showFirstTouchAnimation) {
            FirstTouchAnimation()
        }

        // Error Snackbar
        state.error?.let { error ->
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
                Text(error)
            }
        }
    }
}

@Composable
fun QuestionCard(
    question: String,
    categoryId: Long,
    modifier: Modifier = Modifier
) {
    val categoryEmoji = when (categoryId) {
        1L -> "🌟"  // Marzenia & Aspiracje
        2L -> "💕"  // Miłość & Relacje
        3L -> "😄"  // Śmieszne & Dziwne
        4L -> "🧠"  // Głębokie myśli
        5L -> "✈️"  // Przygoda & Podróże
        6L -> "😘"  // Flirt
        7L -> "🔥"  // Pikantne
        8L -> "🧩"  // Neuroatypowość
        else -> "❓"
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = categoryEmoji,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = question,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun FirstTouchAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "✨ PIERWSZE ZBLIŻENIE! ✨",
            style = MaterialTheme.typography.displaySmall,
            color = Pink,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .scale(scale)
                .padding(32.dp)
        )
    }
}
