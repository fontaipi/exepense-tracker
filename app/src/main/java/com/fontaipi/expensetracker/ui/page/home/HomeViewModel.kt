package com.fontaipi.expensetracker.ui.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fontaipi.expensetracker.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    val transactions = transactionRepository.getTransactions(3).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = emptyList()
    )
}