package com.fontaipi.expensetracker.data.transaction

import com.fontaipi.expensetracker.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactions(limit: Int? = null): Flow<List<Transaction>>
    fun addTransaction(transaction: Transaction)
}