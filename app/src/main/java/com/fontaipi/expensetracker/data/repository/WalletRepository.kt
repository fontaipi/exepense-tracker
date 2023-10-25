package com.fontaipi.expensetracker.data.repository

import com.fontaipi.expensetracker.data.database.entity.WalletEntity
import com.fontaipi.expensetracker.model.Wallet
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun getAccounts(): Flow<List<Wallet>>
    suspend fun addAccount(walletEntity: WalletEntity)
}