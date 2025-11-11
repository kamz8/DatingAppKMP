package com.example.datingappkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.datingappkmp.data.DatabaseDriverFactory
import com.example.datingappkmp.data.models.DeviceType

fun MainViewController() = ComposeUIViewController {
    App(
        databaseDriverFactory = DatabaseDriverFactory(),
        deviceType = DeviceType.IOS
    )
}