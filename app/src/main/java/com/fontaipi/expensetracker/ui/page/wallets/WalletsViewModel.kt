package com.fontaipi.expensetracker.ui.page.wallets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fontaipi.expensetracker.data.repository.WalletRepository
import com.fontaipi.expensetracker.data.repository.TransactionRepository
import com.fontaipi.expensetracker.model.Wallet
import com.fontaipi.expensetracker.model.Transaction
import com.fontaipi.expensetracker.ui.page.home.TransactionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WalletsViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val transactionRepository: TransactionRepository,
) : ViewModel() {
    private val _selectedAccountId = MutableStateFlow(-1L)

    val walletState = walletRepository.getAccounts()
        .map<List<Wallet>, AccountState> {
            AccountState.Success(it)
        }
        .onStart {
            emit(AccountState.Loading)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AccountState.Loading
        )

    val transactionsState = _selectedAccountId.flatMapLatest { accountId ->
        transactionRepository.getTransactionsByAccountId(accountId = accountId)
            .map<List<Transaction>, TransactionsState> {
                TransactionsState.Success(it)
            }.onStart { emit(TransactionsState.Loading) }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = TransactionsState.Loading
    )

    fun selectAccount(accountId: Long) {
        _selectedAccountId.value = accountId
    }
}

sealed class AccountState {
    data class Success(val wallets: List<Wallet>) : AccountState()
    data object Loading : AccountState()
}