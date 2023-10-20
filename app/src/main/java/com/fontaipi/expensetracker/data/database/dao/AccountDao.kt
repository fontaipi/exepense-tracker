package com.fontaipi.expensetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.fontaipi.expensetracker.data.database.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM account")
    fun getAccountEntities(): Flow<List<AccountEntity>>
}