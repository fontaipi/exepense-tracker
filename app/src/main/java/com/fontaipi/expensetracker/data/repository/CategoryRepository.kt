package com.fontaipi.expensetracker.data.repository

import com.fontaipi.expensetracker.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>
}