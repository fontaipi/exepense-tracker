package com.fontaipi.expensetracker.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val balance: BigDecimal,
    val currency: String,
    val color: Int
)