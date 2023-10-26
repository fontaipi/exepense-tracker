package com.fontaipi.expensetracker.ui.page.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fontaipi.expensetracker.data.repository.TransactionRepository
import com.fontaipi.expensetracker.model.Transaction
import com.fontaipi.expensetracker.ui.page.home.TransactionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val _selectedPeriod =
        MutableStateFlow(LocalDateTime.now().monthValue to LocalDateTime.now().year)
    val selectedPeriod: StateFlow<Pair<Int, Int>> = _selectedPeriod

    val transactionState = _selectedPeriod.flatMapLatest { (month, year) ->
        transactionRepository.getTransactionForMonth(
            month,
            year
        ).map<List<Transaction>, TransactionsState> {
            TransactionsState.Success(it)
        }.onStart {
            emit(TransactionsState.Loading)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = TransactionsState.Loading
    )

    fun selectPeriod(month: Int, year: Int) {
        _selectedPeriod.value = month to year
    }
}