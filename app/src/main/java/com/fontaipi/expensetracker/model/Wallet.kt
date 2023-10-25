package com.fontaipi.expensetracker.model

import com.fontaipi.expensetracker.data.database.entity.WalletColors
import java.math.BigDecimal

data class Wallet(
    val id: Long = 0,
    val name: String,
    val balance: BigDecimal,
    val colors: WalletColors,
)