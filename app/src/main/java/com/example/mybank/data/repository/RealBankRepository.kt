package com.example.mybank.data.repository

import com.example.mybank.core.model.BalanceResponse
import com.example.mybank.core.model.LoginRequest
import com.example.mybank.core.model.LoginResponse
import com.example.mybank.core.model.TransactionResponse
import com.example.mybank.core.model.TransactionType
import com.example.mybank.core.model.TransferRequest
import com.example.mybank.core.model.TransferResponse
import com.example.mybank.data.local.MockSecureStorage
import com.example.mybank.data.local.TransactionDao
import com.example.mybank.data.local.toEntity
import com.example.mybank.data.local.toResponse
import com.example.mybank.data.remote.BankApi

class RealBankRepository(
    private val bankApi: BankApi,
    private val secureStorage: MockSecureStorage,
    private val transactionDao: TransactionDao
) : BankRepository {
    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return runCatching {
            bankApi.login(request).also {
                secureStorage.storeToken(it.token)
            }
        }
    }

    override suspend fun getBalance(): Result<BalanceResponse> {
        return runCatching {
            bankApi.getBalance()
        }
    }

    override suspend fun getTransactions(): Result<List<TransactionResponse>> {
        return runCatching {
            val transactions = bankApi.getTransactions()
            transactionDao.clear()
            transactionDao.insertAll(transactions.map { it.toEntity() })
            transactionDao.getAll().map { it.toResponse() }
        }
    }

    override suspend fun transfer(request: TransferRequest): Result<TransferResponse> {
        return runCatching {
            val response = bankApi.transfer(request)
            val created = TransactionResponse(
                id = response.transferId,
                date = response.executedAt.substringBefore("T"),
                description = "Transferência confirmada",
                amount = -request.amount,
                type = TransactionType.DEBIT,
                category = "Transferência"
            )
            transactionDao.insertAll(listOf(created.toEntity()))
            response
        }
    }
}