package com.example.mybank.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    suspend fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE type = 'DEBIT' ORDER BY date DESC")
    suspend fun getDebits(): List<TransactionEntity>
    @Query("SELECT * FROM transactions WHERE type = 'CREDIT' ORDER BY date DESC")
    suspend fun getCredits(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Query("DELETE FROM transactions")
    suspend fun clear()
}

