package com.fontaipi.expensetracker.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fontaipi.expensetracker.model.Wallet

@Composable
fun SelectWalletCard(
    modifier: Modifier = Modifier,
    selectedWallet: Wallet?,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(96.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    selectedWallet?.name ?: "Choose wallet",
                    style = MaterialTheme.typography.titleMedium.copy(lineHeight = 18.sp),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                if (selectedWallet != null) {
                    WalletIcon(
                        walletColors = selectedWallet.colors,
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