package com.example.mybank

import com.example.mybank.core.model.TransactionResponse
import com.example.mybank.core.model.TransactionType
import com.example.mybank.core.usecase.GetTransactionsUseCase
import com.example.mybank.core.viewmodel.TransactionsViewModel
import com.example.mybank.data.repository.BankRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsViewModelTest {
    private val repository: BankRepository = mockk()
    private val viewModel = TransactionsViewModel(GetTransactionsUseCase(repository))

    @Test
    fun `carrega transacoes e atualiza estado`() = runTest {
        val sample = listOf(
            TransactionResponse(
                id = "tx-1",
                date = "2025-11-28",
                description = "Teste",
                amount = 100.0,
                type = TransactionType.CREDIT,
                category = "PIX"
            )
        )
        coEvery { repository.getTransactions() } returns Result.success(sample)
        viewModel.loadTransactions()
        advanceUntilIdle()
        assertEquals(1, viewModel.uiState.value.transactions.size)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }
}

