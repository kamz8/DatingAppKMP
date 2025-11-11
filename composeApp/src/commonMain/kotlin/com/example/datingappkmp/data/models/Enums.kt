package com.example.datingappkmp.data.models

import kotlinx.serialization.Serializable

@Serializable
enum class DeviceType {
    ANDROID,
    IOS
}

@Serializable
enum class SetupMethod {
    NFC,
    MANUAL,
    SOLO
}
