package com.fontaipi.expensetracker.data.di

import com.fontaipi.expensetracker.data.transaction.TransactionRepository
import com.fontaipi.expensetracker.data.transaction.TransactionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsTransactionRepository(
        transactionRepository: TransactionRepositoryImpl,
    ): TransactionRepository
}