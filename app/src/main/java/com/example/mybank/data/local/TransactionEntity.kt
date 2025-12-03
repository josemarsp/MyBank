package com.example.mybank.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mybank.core.model.TransactionResponse
import com.example.mybank.core.model.TransactionType

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val date: String,
    val description: String,
    val amount: Double,
    val type: TransactionType,
    val category: String
)

fun TransactionResponse.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    date = date,
    description = description,
    amount = amount,
    type = type,
    category = category
)

fun TransactionEntity.toResponse(): TransactionResponse = TransactionResponse(
    id = id,
    date = date,
    description = description,
    amount = amount,
    type = type,
    category = category
)

