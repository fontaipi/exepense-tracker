package com.fontaipi.expensetracker.ui.page.add.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fontaipi.expensetracker.data.database.entity.WalletEntity
import com.fontaipi.expensetracker.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
) : ViewModel() {

    fun addWallet(walletEntity: WalletEntity) {
        viewModelScope.launch {
            walletRepository.addAccount(walletEntity)
        }
    }
}