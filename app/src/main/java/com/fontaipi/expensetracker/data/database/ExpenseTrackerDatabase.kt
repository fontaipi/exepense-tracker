package com.fontaipi.expensetracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fontaipi.expensetracker.data.database.dao.WalletDao
import com.fontaipi.expensetracker.data.database.dao.CategoryDao
import com.fontaipi.expensetracker.data.database.dao.TransactionDao
import com.fontaipi.expensetracker.data.database.entity.WalletEntity
import com.fontaipi.expensetracker.data.database.entity.CategoryEntity
import com.fontaipi.expensetracker.data.database.entity.TransactionEntity


@Database(
    entities = [
        WalletEntity::class,
        CategoryEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class ExpenseTrackerDatabase : RoomDatabase() {
    abstract fun accountDao(): WalletDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}