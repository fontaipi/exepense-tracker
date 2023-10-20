package com.fontaipi.expensetracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fontaipi.expensetracker.data.database.dao.AccountDao
import com.fontaipi.expensetracker.data.database.dao.CategoryDao
import com.fontaipi.expensetracker.data.database.entity.AccountEntity
import com.fontaipi.expensetracker.data.database.entity.CategoryEntity

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class ExpenseTrackerDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
}