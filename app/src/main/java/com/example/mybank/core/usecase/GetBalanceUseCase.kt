package com.example.mybank.core.usecase

import com.example.mybank.core.model.BalanceResponse
import com.example.mybank.data.repository.BankRepository

class GetBalanceUseCase(
    private val repository: BankRepository
) {
    suspend fun execute(): Result<BalanceResponse> = repository.getBalance()
}

