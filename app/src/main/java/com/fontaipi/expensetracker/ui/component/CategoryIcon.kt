package com.fontaipi.expensetracker.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fontaipi.expensetracker.model.Category
import com.fontaipi.expensetracker.model.categoryIconMap

@Composable
fun CategoryBox(
    modifier: Modifier = Modifier,
    category: Category,
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(category.color),
        contentAlignment = Alignment.Center,
    ) {
        categoryIconMap[category.icon]?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Color.White,
            )
        }
    }
}

