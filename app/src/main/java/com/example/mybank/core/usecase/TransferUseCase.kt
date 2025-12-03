package com.example.mybank.core.usecase

import com.example.mybank.core.model.TransferRequest
import com.example.mybank.core.model.TransferResponse
import com.example.mybank.data.repository.BankRepository

/**
 * Centraliza as validações de negócio da transferência e mantém a separação entre UI e dados.
 */
class TransferUseCase(
    private val repository: BankRepository
) {
    suspend fun execute(request: TransferRequest): Result<TransferResponse> =
        repository.transfer(request)
}

