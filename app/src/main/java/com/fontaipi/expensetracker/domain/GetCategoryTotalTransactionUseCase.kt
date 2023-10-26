package com.fontaipi.expensetracker.domain

import android.util.Log
import com.fontaipi.expensetracker.data.repository.TransactionRepository
import com.fontaipi.expensetracker.model.CategoryTotalTransaction
import com.fontaipi.expensetracker.ui.page.add.transaction.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoryTotalTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(monthValue: Int, year: Int): Flow<List<CategoryTotalTransaction>> {
        Log.d("GetCategoryTotalTransactionUseCase", "invoke")
        return transactionRepository.getTransactionForMonth(monthValue, year).map { transactions ->
            transactions.filter { it.type == TransactionType.EXPENSE }.groupBy { it.category }
                .map { (category, transactions) ->
                    CategoryTotalTransaction(
                        category = category,
                        total = transactions.filter { it.type == TransactionType.EXPENSE }
                            .sumOf { it.amount }
                    )
                }.sortedByDescending { it.total }
        }
    }
}