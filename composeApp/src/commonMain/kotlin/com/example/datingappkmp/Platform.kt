package com.example.datingappkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform