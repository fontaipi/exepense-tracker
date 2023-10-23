package com.fontaipi.expensetracker.data.repository

import com.fontaipi.expensetracker.data.database.entity.TransactionEntity
import com.fontaipi.expensetracker.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactions(limit: Int? = null): Flow<List<Transaction>>

    fun getTransactionsByAccountId(accountId: Long): Flow<List<Transaction>>
    fun getTransactionForMonth(month: Int, year: Int): Flow<List<Transaction>>
    suspend fun addTransaction(transaction: TransactionEntity)
}