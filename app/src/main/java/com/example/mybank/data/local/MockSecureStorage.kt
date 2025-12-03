package com.example.mybank.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MockSecureStorage {
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    fun storeToken(tokenValue: String) {
        _token.value = tokenValue
    }

    fun clearToken() {
        _token.value = null
    }
}

