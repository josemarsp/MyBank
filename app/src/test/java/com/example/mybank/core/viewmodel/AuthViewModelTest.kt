package com.example.mybank.core.viewmodel

import com.example.mybank.core.model.AccountResponse
import com.example.mybank.core.model.LoginResponse
import com.example.mybank.core.model.LoginRequest
import com.example.mybank.core.usecase.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `login success updates ui state to success`() = runTest {
        val loginUseCase: LoginUseCase = mockk()
        val account = AccountResponse("Cliente Demo", "1234-5", "Corrente")
        val response = LoginResponse("token", account, "2099-12-31T23:59:59")

        coEvery {
            loginUseCase.login(LoginRequest("cliente@mybank.com", "senhaSegura"))
        } returns Result.success(response)

        val viewModel = AuthViewModel(loginUseCase)
        viewModel.login("cliente@mybank.com", "senhaSegura")

        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value is AuthUiState.Success)
        assertEquals(account, (viewModel.uiState.value as AuthUiState.Success).account)
    }

    @Test
    fun `login failure updates ui state to error`() = runTest {
        val loginUseCase: LoginUseCase = mockk()

        coEvery {
            loginUseCase.login(LoginRequest("cliente@mybank.com", "senhaSegura"))
        } returns Result.failure(Exception("Credenciais inválidas"))

        val viewModel = AuthViewModel(loginUseCase)
        viewModel.login("cliente@mybank.com", "senhaSegura")

        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value is AuthUiState.Error)
        assertEquals("Credenciais inválidas", (viewModel.uiState.value as AuthUiState.Error).message)
    }

    inner class MainDispatcherRule(
        val dispatcher: TestDispatcher = StandardTestDispatcher()
    ) : TestWatcher() {

        override fun starting(description: Description) {
            Dispatchers.setMain(dispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
        }
    }
}

