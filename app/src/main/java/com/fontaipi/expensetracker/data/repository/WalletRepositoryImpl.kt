package com.fontaipi.expensetracker.data.repository

import android.util.Log
import com.fontaipi.expensetracker.data.database.dao.WalletDao
import com.fontaipi.expensetracker.data.database.entity.WalletEntity
import com.fontaipi.expensetracker.data.database.entity.asExternalModel
import com.fontaipi.expensetracker.model.Wallet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val walletDao: WalletDao
) : WalletRepository {
    override fun getAccounts(): Flow<List<Wallet>> {
        Log.d("AccountRepositoryImpl", "getAccounts")
        return walletDao.getWallets().map { accountEntities ->
            accountEntities.map { it.asExternalModel() }
        }
    }

    override suspend fun addAccount(walletEntity: WalletEntity) {
        walletDao.upsertWallet(walletEntity)
    }
}