package com.fontaipi.expensetracker.data.repository

import android.util.Log
import com.fontaipi.expensetracker.data.database.dao.AccountDao
import com.fontaipi.expensetracker.data.database.entity.asExternalModel
import com.fontaipi.expensetracker.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {
    override fun getAccounts(): Flow<List<Account>> {
        Log.d("AccountRepositoryImpl", "getAccounts")
        return accountDao.getAccounts().map { accountEntities ->
            accountEntities.map { it.asExternalModel() }
        }
    }
}