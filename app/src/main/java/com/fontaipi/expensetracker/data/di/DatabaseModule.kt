package com.fontaipi.expensetracker.data.di

import android.content.Context
import androidx.room.Room
import com.fontaipi.expensetracker.data.database.ExpenseTrackerDatabase
import com.fontaipi.expensetracker.data.database.dao.AccountDao
import com.fontaipi.expensetracker.data.database.dao.CategoryDao
import com.fontaipi.expensetracker.data.database.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun providesExpenseTrackerDatabase(
        @ApplicationContext context: Context,
    ): ExpenseTrackerDatabase = Room.databaseBuilder(
        context,
        ExpenseTrackerDatabase::class.java,
        "expense-tracker-database",
    ).build()

    @Provides
    fun providesAccountDao(
        database: ExpenseTrackerDatabase,
    ): AccountDao = database.accountDao()

    @Provides
    fun providesCategoryDao(
        database: ExpenseTrackerDatabase,
    ): CategoryDao = database.categoryDao()

    @Provides
    fun providesTransactionDao(
        database: ExpenseTrackerDatabase,
    ): TransactionDao = database.transactionDao()
}