package com.fontaipi.expensetracker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DonutSmall
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.rounded.DonutSmall
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val titleText: String,
) {
    HOME(
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconText = "Home",
        titleText = "Home",
    ),
    ANALYTICS(
        selectedIcon = Icons.Rounded.DonutSmall,
        unselectedIcon = Icons.Outlined.DonutSmall,
        iconText = "Analytics",
        titleText = "Analytics",
    ),
    BUDGETS(
        selectedIcon = Icons.Rounded.Savings,
        unselectedIcon = Icons.Outlined.Savings,
        iconText = "Budgets",
        titleText = "Budgets",
    )
}