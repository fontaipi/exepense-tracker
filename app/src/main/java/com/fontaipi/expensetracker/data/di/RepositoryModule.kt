package com.fontaipi.expensetracker.data.di

import com.fontaipi.expensetracker.data.repository.CategoryRepository
import com.fontaipi.expensetracker.data.repository.CategoryRepositoryImpl
import com.fontaipi.expensetracker.data.repository.TransactionRepository
import com.fontaipi.expensetracker.data.repository.TransactionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsTransactionRepository(
        transactionRepository: TransactionRepositoryImpl,
    ): TransactionRepository

    @Binds
    fun bindsCategoryRepository(
        categoryRepository: CategoryRepositoryImpl,
    ): CategoryRepository
}