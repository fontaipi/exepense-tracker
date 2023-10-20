package com.fontaipi.expensetracker.data.repository

import com.fontaipi.expensetracker.data.database.dao.CategoryDao
import com.fontaipi.expensetracker.data.database.entity.asExternalModel
import com.fontaipi.expensetracker.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,

    ) : CategoryRepository {
    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getCategories()
            .map { categoryEntities -> categoryEntities.map { it.asExternalModel() } }
    }
}