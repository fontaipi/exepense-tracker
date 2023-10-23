package com.fontaipi.expensetracker.data.repository

import com.fontaipi.expensetracker.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAccounts(): Flow<List<Account>>
}