package com.fontaipi.expensetracker.model

import com.fontaipi.expensetracker.ui.page.home.Account
import com.fontaipi.expensetracker.ui.page.home.TransactionType
import java.math.BigDecimal

data class Transaction(
    val category: Category,
    val hashtags: Set<String> = emptySet(),
    val price: BigDecimal,
    val type: TransactionType,
    val account: Account,
    val date: Long = System.currentTimeMillis(),
)