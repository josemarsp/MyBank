package com.example.mybank.core.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

/*
Example request JSON:
{
  "email": "cliente@mybank.com",
  "password": "senhaSegura"
}
*/

@Serializable
data class LoginResponse(
    val token: String,
    val account: AccountResponse,
    val expiresAt: String
)

/*
Example response JSON:
{
//  "token": "eyJhbGciOi...",
  "expiresAt": "2025-12-31T23:59:59",
  "account": {
    "holderName": "Cliente Demo",
    "accountNumber": "1234-5",
    "type": "Corrente"
  }
}
*/

@Serializable
data class BalanceResponse(
    val available: Double,
    val currency: String,
    val lastUpdated: String
)

/*
Example response JSON:
{
  "available": 12500.74,
  "currency": "BRL",
  "lastUpdated": "2025-11-29T09:12:00"
}
*/

@Serializable
data class TransactionResponse(
    val id: String,
    val date: String,
    val description: String,
    val amount: Double,
    val type: TransactionType,
    val category: String
)

enum class TransactionType {
    CREDIT,
    DEBIT
}

/*
Example response JSON array:
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

@Serializable
data class TransferRequest(
    val fromAccount: String,
    val toAccount: String,
    val amount: Double,
    val message: String? = null
)

@Serializable
data class TransferResponse(
    val transferId: String,
    val status: String,
    val executedAt: String
)

/*
Example request JSON:
{
  "fromAccount": "1234-5",
  "toAccount": "4321-9",
  "amount": 250.0,
  "message": "Pagamento aluguel"
}
*/

@Serializable
data class AccountResponse(
    val holderName: String,
    val accountNumber: String,
    val type: String
)

/*
Example response JSON:
{
  "holderName": "Cliente Demo",
  "accountNumber": "1234-5",
  "type": "Conta Corrente"
}
*/

fun balanceTimestampNow(): String = LocalDateTime.now().toString()

