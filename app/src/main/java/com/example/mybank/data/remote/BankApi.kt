package com.example.mybank.data.remote

import com.example.mybank.core.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BankApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    /*
    Exemplo de resposta:
    {
      "token": "token.jwt.mock",
      "expiresAt": "2025-11-29T23:59:59",
      "account": {
          "holderName": "Cliente Demo",
          "accountNumber": "1234-5",
          "type": "Corrente"
      }
    }
    */

    @GET("account/balance")
    suspend fun getBalance(): BalanceResponse

    /*
    Exemplo de resposta:
    {
      "available": 12345.67,
      "currency": "BRL",
      "lastUpdated": "2025-11-29T10:00:00"
    }
    */

    @GET("account/transactions")
    suspend fun getTransactions(): List<TransactionResponse>

    /*
    Exemplo de resposta:
    [
      {
        "id": "tx-001",
        "date": "2025-11-28",
        "description": "Pix recebido",
        "amount": 1200.0,
        "type": "CREDIT",
        "category": "PIX"
      }
    ]
    */

    @POST("account/transfer")
    suspend fun transfer(@Body request: TransferRequest): TransferResponse

    /*
    Exemplo de resposta:
    {
      "transferId": "tr-001",
      "status": "CONFIRMED",
      "executedAt": "2025-11-29T10:05:00"
    }
    */
}

