package com.fontaipi.expensetracker.data.database.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Embedded
import androidx.room.Relation
import com.fontaipi.expensetracker.model.Transaction


data class PopulatedTransaction(
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "id"
    )
    val account: AccountEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity,
)


fun PopulatedTransaction.asExternalModel() = Transaction(
    id = transaction.id,
    account = account.asExternalModel(),
    category = category.asExternalModel(),
    type = transaction.type,
    amount = transaction.amount,
    hashtags = emptySet(),
    date = transaction.date,
)