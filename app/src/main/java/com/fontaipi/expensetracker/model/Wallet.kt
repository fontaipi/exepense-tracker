package com.fontaipi.expensetracker.model

import androidx.compose.runtime.Immutable
import com.fontaipi.expensetracker.data.database.entity.WalletColors
import java.math.BigDecimal

data class Wallet(
    val id: Long = 0,
    val name: String,
    val balance: BigDecimal,
    val colors: WalletColors,
)