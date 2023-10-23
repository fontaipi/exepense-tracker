package com.fontaipi.expensetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.fontaipi.expensetracker.data.database.entity.AccountEntity
import com.fontaipi.expensetracker.data.database.entity.UpdateAccountBalance
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM account")
    fun getAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM account WHERE id = :id")
    suspend fun getAccount(id: Long): AccountEntity

    @Update(entity = AccountEntity::class)
    suspend fun updateAccountBalance(updateAccountBalance: UpdateAccountBalance)

    @Upsert
    suspend fun upsertAccount(accountEntity: AccountEntity): Long
}