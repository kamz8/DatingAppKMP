package com.example.datingappkmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.datingappkmp.data.DatabaseDriverFactory
import com.example.datingappkmp.data.models.DeviceType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(
                databaseDriverFactory = DatabaseDriverFactory(applicationContext),
                deviceType = DeviceType.ANDROID
            )
        }
    }
}