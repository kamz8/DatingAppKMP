package com.example.datingappkmp

import kotlin.test.Test
import kotlin.test.assertTrue

class SimpleSmokeTest {

    @Test
    fun testBasicAssertion() {
        assertTrue(true, "Basic test should pass")
    }

    @Test
    fun testMath() {
        val result = 2 + 2
        assertTrue(result == 4, "2 + 2 should equal 4")
    }
}