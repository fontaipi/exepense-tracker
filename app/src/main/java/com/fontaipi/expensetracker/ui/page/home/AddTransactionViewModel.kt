package com.fontaipi.expensetracker.ui.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fontaipi.expensetracker.data.database.entity.TransactionEntity
import com.fontaipi.expensetracker.data.repository.AccountRepository
import com.fontaipi.expensetracker.data.repository.CategoryRepository
import com.fontaipi.expensetracker.data.repository.TransactionRepository
import com.fontaipi.expensetracker.model.Account
import com.fontaipi.expensetracker.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    val addTransactionState: StateFlow<AddTransactionState> = combine(
        accountRepository.getAccounts(),
        categoryRepository.getCategories(),
        ::Pair
    ).map { (accounts, categories) ->
        AddTransactionState.Success(accounts, categories)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AddTransactionState.Loading

    )

    fun addTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            transactionRepository.addTransaction(transaction)
        }
    }
}

sealed class AddTransactionState {
    data class Success(val accounts: List<Account>, val categories: List<Category>) :
        AddTransactionState()

    data object Loading : AddTransactionState()
}