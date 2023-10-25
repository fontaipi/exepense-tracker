package com.fontaipi.expensetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.fontaipi.expensetracker.data.database.entity.UpdateAccountBalance
import com.fontaipi.expensetracker.data.database.entity.WalletEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {

    @Query("SELECT * FROM wallet")
    fun getWallets(): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallet WHERE id = :id")
    suspend fun getWallet(id: Long): WalletEntity

    @Update(entity = WalletEntity::class)
    suspend fun updateWalletBalance(updateAccountBalance: UpdateAccountBalance)

    @Upsert
    suspend fun upsertWallet(walletEntity: WalletEntity): Long
}