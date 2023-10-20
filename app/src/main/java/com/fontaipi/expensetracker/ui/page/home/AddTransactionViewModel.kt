package com.fontaipi.expensetracker.ui.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fontaipi.expensetracker.data.repository.CategoryRepository
import com.fontaipi.expensetracker.data.repository.TransactionRepository
import com.fontaipi.expensetracker.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    val categoriesState = categoryRepository.getCategories().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = emptyList()
    )

    fun addTransaction(transaction: Transaction) {
        transactionRepository.addTransaction(transaction)
    }
}