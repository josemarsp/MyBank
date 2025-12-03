package com.example.mybank

import com.example.mybank.core.model.AccountResponse
import com.example.mybank.core.model.LoginRequest
import com.example.mybank.core.model.LoginResponse
import com.example.mybank.core.usecase.LoginUseCase
import com.example.mybank.data.repository.BankRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginUseCaseTest {

    private val repository: BankRepository = mockk()
    private val useCase = LoginUseCase(repository)

    @Test
    fun `login emite sucesso com token`() = runTest {
        val expected = LoginResponse(
            token = "token",
            expiresAt = "2026-01-01T00:00:00",
            account = AccountResponse("Cliente", "1234-5", "Corrente")
        )
        coEvery { repository.login(any()) } returns Result.success(expected)
        val result = useCase.login(LoginRequest("email", "senha"))
        assertTrue(result.isSuccess)
        assertEquals(expected.token, result.getOrNull()?.token)
    }

    @Test
    fun `login emite falha quando repositorio falha`() = runTest {
        val error = Exception("401")
        coEvery { repository.login(any()) } returns Result.failure(error)
        val result = useCase.login(LoginRequest("email", "senha"))
        assertTrue(result.isFailure)
    }
}

