package com.fontaipi.expensetracker.ui.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fontaipi.expensetracker.data.repository.TransactionRepository
import com.fontaipi.expensetracker.data.repository.WalletRepository
import com.fontaipi.expensetracker.domain.GetCategoryTotalTransactionUseCase
import com.fontaipi.expensetracker.model.CategoryTotalTransaction
import com.fontaipi.expensetracker.model.Transaction
import com.fontaipi.expensetracker.model.Wallet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val transactionRepository: TransactionRepository,
    private val getCategoryTotalTransaction: GetCategoryTotalTransactionUseCase
) : ViewModel() {
    val transactionState = transactionRepository.getTransactionForMonth(
        LocalDateTime.now().monthValue,
        LocalDateTime.now().year
    ).map<List<Transaction>, TransactionsState> {
        TransactionsState.Success(it.take(3))
    }.onStart {
        emit(TransactionsState.Loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = TransactionsState.Loading
    )

    val homePageState = combine(
        walletRepository.getAccounts(),
        getCategoryTotalTransaction(
            LocalDateTime.now().monthValue,
            LocalDateTime.now().year
        ),
        ::Pair
    ).map<Pair<List<Wallet>, List<CategoryTotalTransaction>>, HomePageState> { (accounts, transactions) ->
        HomePageState.Success(accounts, transactions)
    }.onStart {
        emit(HomePageState.Loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomePageState.Loading
    )
}

sealed class HomePageState {
    data class Success(
        val wallets: List<Wallet>,
        val categoryTotalTransaction: List<CategoryTotalTransaction>
    ) : HomePageState()

    data object Loading : HomePageState()
}

sealed class TransactionsState {
    data class Success(val transactions: List<Transaction>) : TransactionsState()
    data object Loading : TransactionsState()
}