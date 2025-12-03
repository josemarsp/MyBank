package com.example.mybank.core.usecase

import com.example.mybank.core.model.LoginRequest
import com.example.mybank.core.model.LoginResponse
import com.example.mybank.data.repository.BankRepository

class LoginUseCase(
    private val repository: BankRepository
) {
    private val emailPattern =
        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z0-9]{2,6}\$")

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        if (request.email.isBlank() || request.password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email e senha devem ser preenchidos"))
        }

        if (request.password.length < 8) {
            return Result.failure(IllegalArgumentException("Senha menor que o esperado."))
        }

        if (!emailPattern.matches(request.email)) {
            return Result.failure(IllegalArgumentException("Email ou senha inválidos"))
        }
        return repository.login(request)
    }
}

