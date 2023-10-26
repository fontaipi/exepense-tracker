package com.fontaipi.expensetracker.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoCell
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fontaipi.expensetracker.model.Category
import com.fontaipi.expensetracker.model.categoryIconMap

@Composable
fun SelectCategoryCard(
    modifier: Modifier = Modifier,
    selectedCategory: Category?,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.height(96.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "Choose category",
                    style = MaterialTheme.typography.titleMedium.copy(lineHeight = 18.sp),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                if (selectedCategory == null) {
                    val stroke = Stroke(
                        width = 4f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                    val strokeColor = MaterialTheme.colorScheme.onSurface
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .drawBehind {
                                drawRoundRect(
                                    color = strokeColor,
                                    style = stroke,
                                    cornerRadius = CornerRadius(10.dp.toPx())
                                )
                            }
                    )
                } else {
                    CategoryBox(
                        icon = categoryIconMap[selectedCategory.icon] ?: Icons.Default.NoCell,
                        containerColor = selectedCategory.color,
                        modifier = Modifier.size(36.dp)
                    )
                }

            }
            Text(
                "Tap to select",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}