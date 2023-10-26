package com.fontaipi.expensetracker.data.database.entity

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fontaipi.expensetracker.model.Wallet
import java.math.BigDecimal
import javax.annotation.concurrent.Immutable

val sampleMainAccount = WalletEntity(
    name = "Main account",
    balance = BigDecimal(1000),
    currency = "EUR",
    colors = WalletColors.Type1
)

val sampleSavingAccount = WalletEntity(
    name = "Saving account",
    balance = BigDecimal(1300),
    currency = "EUR",
    colors = WalletColors.Type2
)

@Immutable
enum class WalletColors(val primary: Color, val secondary: Color) {
    Type1(Color(0xFFE57373), Color(0xFFEF9A9A)),
    Type2(Color(0xFFF06292), Color(0xFFF48FB1)),
    Type3(Color(0xFFBA68C8), Color(0xFFCE93D8)),
    Type4(Color(0xFF9575CD), Color(0xFFB39DDB)),
    Type5(Color(0xFF7986CB), Color(0xFF9FA8DA)),
}

@Entity(tableName = "wallet")
data class WalletEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val balance: BigDecimal,
    val currency: String,
    val colors: WalletColors
)

data class UpdateAccountBalance(
    val id: Long,
    val balance: BigDecimal
)

fun WalletEntity.asExternalModel() = Wallet(
    id = id,
    balance = balance,
    name = name,
    colors = colors
)