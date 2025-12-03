package com.example.mybank.core.network

import com.example.mybank.data.local.MockSecureStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(
    private val secureStorage: MockSecureStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = secureStorage.token.value
        val updated = request.newBuilder()
            .apply {
                token?.let { header("Authorization", "Bearer $it") }
            }
            .build()
        return chain.proceed(updated)
    }
}

