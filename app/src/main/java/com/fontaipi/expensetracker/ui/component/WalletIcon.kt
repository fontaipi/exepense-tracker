package com.fontaipi.expensetracker.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fontaipi.expensetracker.data.database.entity.WalletColors
import com.fontaipi.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun WalletIcon(
    modifier: Modifier = Modifier,
    walletColors: WalletColors = WalletColors.Type3,
) {
    Box(
        modifier = modifier
            .size(height = 32.dp, width = 44.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(walletColors.primary)
            .drawBehind {
                drawRoundRect(
                    color = walletColors.secondary,
                    size = Size(width = 14.dp.toPx(), height = 10.dp.toPx()),
                    cornerRadius = CornerRadius(x = 2.dp.toPx(), y = 2.dp.toPx()),
                    topLeft = Offset(x = 32.dp.toPx(), y = 11.dp.toPx()),
                )
            }
    )
}

@Preview
@Composable
fun WalletIconPreview() {
    ExpenseTrackerTheme {
        WalletIcon()
    }
}