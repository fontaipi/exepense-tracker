package com.fontaipi.expensetracker.data.transaction

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import com.fontaipi.expensetracker.model.Transaction
import com.fontaipi.expensetracker.ui.page.home.Account
import com.fontaipi.expensetracker.ui.page.home.TransactionCategory
import com.fontaipi.expensetracker.ui.page.home.TransactionType
import com.fontaipi.expensetracker.ui.theme.CategoryBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

val sampleMainAccount = Account(
    name = "Main account",
    color = CategoryBlue,
)

val transactions = mutableListOf(
    Transaction(
        category = TransactionCategory(
            name = "Groceries",
            color = CategoryBlue,
            icon = Icons.Outlined.ShoppingCart,
        ),
        hashtags = setOf("starbucks", "flatwhite"),
        price = BigDecimal("3.50"),
        type = TransactionType.EXPENSE,
        account = sampleMainAccount,
    ),
)

class TransactionRepositoryImpl @Inject constructor() : TransactionRepository {
    private val transactionsFlow: Flow<List<Transaction>> = flow {
        while (true) {
            emit(transactions.toList())
            delay(100)
        }
    }

    override fun getTransactions(limit: Int?): Flow<List<Transaction>> {
        return transactionsFlow.map { transactions ->
            transactions.take(limit ?: transactions.size)
        }
    }

    override fun addTransaction(transaction: Transaction) {
        transactions.add(transaction)
    }
}