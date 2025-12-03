package com.example.mybank.core.usecase

import com.example.mybank.core.model.TransactionResponse
import com.example.mybank.data.repository.BankRepository

class GetTransactionsUseCase(
    private val repository: BankRepository
) {
    suspend fun execute(): Result<List<TransactionResponse>> = repository.getTransactions()
}

