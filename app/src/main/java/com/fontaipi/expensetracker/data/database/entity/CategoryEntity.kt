package com.fontaipi.expensetracker.data.database.entity

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fontaipi.expensetracker.model.Category
import com.fontaipi.expensetracker.model.CategoryIcon
import com.fontaipi.expensetracker.ui.theme.CategoryBlue
import com.fontaipi.expensetracker.ui.theme.CategoryGreen
import com.fontaipi.expensetracker.ui.theme.CategoryOrange
import com.fontaipi.expensetracker.ui.theme.CategoryPurple
import com.fontaipi.expensetracker.ui.theme.CategoryRed
import com.fontaipi.expensetracker.ui.theme.CategoryYellow

val categories = listOf(
    CategoryEntity(
        name = "Groceries",
        icon = CategoryIcon.GROCERIES,
        color = CategoryBlue.toArgb()
    ),
    CategoryEntity(
        name = "Transport",
        icon = CategoryIcon.TRANSPORT,
        color = CategoryRed.toArgb()
    ),
    CategoryEntity(
        name = "Shopping",
        icon = CategoryIcon.SHOPPING,
        color = CategoryOrange.toArgb()
    ),
    CategoryEntity(
        name = "Entertainment",
        icon = CategoryIcon.ENTERTAINMENT,
        color = CategoryPurple.toArgb()
    ),
    CategoryEntity(
        name = "Health",
        icon = CategoryIcon.HEALTH,
        color = CategoryGreen.toArgb()
    ),
    CategoryEntity(
        name = "Education",
        icon = CategoryIcon.EDUCATION,
        color = CategoryYellow.toArgb()
    ),
)

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: CategoryIcon,
    val color: Int
)

fun CategoryEntity.asExternalModel() = Category(
    id = id,
    name = name,
    icon = icon,
    color = Color(color)
)