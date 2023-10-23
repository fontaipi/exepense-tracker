package com.fontaipi.expensetracker.model

import com.fontaipi.expensetracker.data.database.entity.TransactionEntity
import com.fontaipi.expensetracker.ui.page.home.TransactionType
import java.math.BigDecimal
import java.time.Instant


data class Transaction constructor(
    val id: Long = 0,
    val category: Category,
    val hashtags: Set<String> = emptySet(),
    val amount: BigDecimal,
    val type: TransactionType,
    val account: Account,
    val date: Instant = Instant.now()
)

fun Transaction.asEntity(): TransactionEntity {
    return TransactionEntity(
        id = 0,
        accountId = account.id,
        categoryId = category.id,
        type = type,
        amount = amount,
        date = date,
    )
}