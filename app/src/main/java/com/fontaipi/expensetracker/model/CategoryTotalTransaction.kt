package com.fontaipi.expensetracker.model

import com.fontaipi.expensetracker.model.Category
import java.math.BigDecimal

data class CategoryTotalTransaction(
    val category: Category,
    val total: BigDecimal
)