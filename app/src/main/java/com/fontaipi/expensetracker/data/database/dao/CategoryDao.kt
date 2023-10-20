package com.fontaipi.expensetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.fontaipi.expensetracker.data.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT COUNT(*) FROM category")
    fun countCategories(): Int

    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Upsert
    suspend fun upsertCategories(categories: List<CategoryEntity>)
}