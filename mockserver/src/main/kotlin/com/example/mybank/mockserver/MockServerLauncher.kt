package com.example.mybank.mockserver

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

fun main() {
    val server = MockWebServer()
    server.dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when {
                request.path?.startsWith("/auth/login") == true -> loginResponse()
                request.path?.startsWith("/account/balance") == true -> balanceResponse()
                request.path?.startsWith("/account/transactions") == true -> transactionsResponse()
                request.path?.startsWith("/account/transfer") == true -> transferResponse()
                else -> MockResponse().setResponseCode(404)
            }
        }
    }
    server.start(8080)
    println("MockWebServer rodando em ${server.url("/")}")
    println("Pressione ENTER para parar")
    readLine()
    server.shutdown()
}

private fun loginResponse() = MockResponse()
    .setResponseCode(200)
    .setBody(
        """
        {
          "token": "mock-jwt-client",
          "expiresAt": "2025-12-31T23:59:59",
          "account": {
            "holderName": "Cliente Demo",
            "accountNumber": "1234-5",
            "type": "Corrente"
          }
        }
        """.trimIndent()
    )

private fun balanceResponse() = MockResponse()
    .setResponseCode(200)
    .setBody(
        """
        {
          "available": 14500.25,
          "currency": "BRL",
          "lastUpdated": "2025-11-29T10:00:00"
        }
        """.trimIndent()
    )

private fun transactionsResponse() = MockResponse()
    .setResponseCode(200)
    .setBody(
        """
        [
          {"id":"tx-001","date":"2025-11-29","description":"Pix recebidorrrr","amount":1200.00,"type":"CREDIT","category":"PIX"},
          {"id":"tx-002","date":"2025-11-28","description":"Cartão maçã","amount":-180.50,"type":"DEBIT","category":"Compras"},
          {"id":"tx-003","date":"2025-11-27","description":"Uber","amount":-32.60,"type":"DEBIT","category":"Mobilidade"}
        ]
        """.trimIndent()
    )

private fun transferResponse() = MockResponse()
    .setResponseCode(200)
    .setBody(
        """
        {
          "transferId": "tr-0001",
          "status": "CONFIRMED",
          "executedAt": "2025-11-29T11:20:00"
        }
        """.trimIndent()
    )

