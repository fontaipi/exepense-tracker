package com.fontaipi.expensetracker.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Healing
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.ui.graphics.Color

enum class CategoryIcon {
    GROCERIES,
    TRANSPORT,
    SHOPPING,
    ENTERTAINMENT,
    HEALTH,
    EDUCATION,
}

val categoryIconMap = mapOf(
    CategoryIcon.GROCERIES to Icons.Outlined.LocalCafe,
    CategoryIcon.TRANSPORT to Icons.Outlined.Train,
    CategoryIcon.SHOPPING to Icons.Outlined.ShoppingBag,
    CategoryIcon.ENTERTAINMENT to Icons.Outlined.Tv,
    CategoryIcon.HEALTH to Icons.Outlined.Healing,
    CategoryIcon.EDUCATION to Icons.Outlined.School,
)

data class Category(
    val id: Long = 0,
    val name: String,
    val icon: CategoryIcon,
    val color: Color,
)