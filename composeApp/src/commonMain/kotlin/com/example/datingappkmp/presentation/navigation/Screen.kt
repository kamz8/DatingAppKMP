package com.example.datingappkmp.presentation.navigation

sealed class Screen {
    object Welcome : Screen()
    data class ManualSetup(val playerName: String) : Screen()
    object Home : Screen()
    object Game : Screen()
    object History : Screen()
}
