package com.fontaipi.expensetracker.ui.page.home

import androidx.lifecycle.ViewModel
import com.fontaipi.expensetracker.data.transaction.TransactionRepository
import com.fontaipi.expensetracker.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    fun addTransaction(transaction: Transaction) {
        transactionRepository.addTransaction(transaction)
    }
}