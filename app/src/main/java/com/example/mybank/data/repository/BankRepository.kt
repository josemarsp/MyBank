package com.example.mybank.data.repository

import com.example.mybank.core.model.*
import kotlinx.coroutines.flow.StateFlow

interface BankRepository {
    suspend fun login(request: LoginRequest): Result<LoginResponse>
    suspend fun getBalance(): Result<BalanceResponse>
    suspend fun getTransactions(): Result<List<TransactionResponse>>
    suspend fun transfer(request: TransferRequest): Result<TransferResponse>
//    fun observeToken(): StateFlow<String?>
}

