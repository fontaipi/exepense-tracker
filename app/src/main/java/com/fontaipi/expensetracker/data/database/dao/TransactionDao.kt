package com.fontaipi.expensetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.fontaipi.expensetracker.data.database.entity.PopulatedTransaction
import com.fontaipi.expensetracker.data.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Transaction
    @Query("SELECT * FROM `transaction` ORDER BY date DESC")
    fun getTransactions(): Flow<List<PopulatedTransaction>>

    @Transaction
    @Query("SELECT * FROM `transaction` ORDER BY date DESC LIMIT :limit")
    fun getTransactions(limit: Int): Flow<List<PopulatedTransaction>>

    @Transaction
    @Query("SELECT * FROM `transaction` WHERE accountId = :accountId ORDER BY date DESC")
    fun getTransactionsByAccountId(accountId: Long): Flow<List<PopulatedTransaction>>

    @Upsert
    fun upsertTransaction(transactionEntity: TransactionEntity)
}