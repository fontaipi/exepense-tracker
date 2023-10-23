package com.fontaipi.expensetracker.model

import com.fontaipi.expensetracker.data.database.entity.AccountColors
import java.math.BigDecimal

data class Account(
    val id: Long = 0,
    val name: String,
    val balance: BigDecimal,
    val colors: AccountColors,
)