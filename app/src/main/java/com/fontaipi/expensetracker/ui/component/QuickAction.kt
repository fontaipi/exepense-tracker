package com.fontaipi.expensetracker.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fontaipi.expensetracker.data.database.entity.WalletColors
import com.fontaipi.expensetracker.model.Wallet
import com.fontaipi.expensetracker.model.Category
import com.fontaipi.expensetracker.model.CategoryTotalTransaction
import com.fontaipi.expensetracker.ui.page.add.transaction.sampleCategories
import com.fontaipi.expensetracker.ui.theme.ExpenseTrackerTheme
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

val sampleWallets = listOf(
    Wallet(name = "Main account", balance = BigDecimal(100), colors = WalletColors.Type1),
    Wallet(name = "Savings", balance = BigDecimal(100), colors = WalletColors.Type2),
    Wallet(name = "Credit card", balance = BigDecimal(100), colors = WalletColors.Type3),
)

@Composable
fun QuickAction(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            content()
        }
    }
}

@Composable
fun MyWallet(
    wallets: List<Wallet>,
    onClick: () -> Unit
) {
    QuickAction(
        modifier = Modifier.height(96.dp),
        title = "My wallets",
        subtitle = "${wallets.size} wallets in total",
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy((-20).dp)
        ) {
            wallets.forEach {
                WalletIcon(
                    walletColors = it.colors,
                )
            }
        }
    }
}

data class TransactionsPerCategory(
    val category: Category,
    val amount: BigDecimal,
)

val sampleTransactionsPerCategories = listOf(
    TransactionsPerCategory(
        category = sampleCategories[0],
        amount = BigDecimal("100.0"),
    ),
    TransactionsPerCategory(
        category = sampleCategories[1],
        amount = BigDecimal("200.0"),
    ),
    TransactionsPerCategory(
        category = sampleCategories[2],
        amount = BigDecimal("32.50"),
    ),
)

@Composable
fun AllTransactions(
    categoryTotalTransactions: List<CategoryTotalTransaction>,
) {
    val month = LocalDateTime.now().month.name
    var animationPlayed by remember { mutableStateOf(false) }
    val total by remember { derivedStateOf { categoryTotalTransactions.sumOf { it.total } } }

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    QuickAction(
        modifier = Modifier.height(96.dp),
        title = "All transactions",
        subtitle = "$total€ spent in ${
            SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
        }",
        onClick = {}
    ) {
        Row(
            modifier = Modifier.clip(MaterialTheme.shapes.extraSmall)
        ) {
            categoryTotalTransactions.forEach { category ->
                val targetWeight by remember {
                    derivedStateOf {
                        category.total.divide(
                            total,
                            10,
                            RoundingMode.HALF_EVEN
                        ).toFloat()
                    }
                }

                Box(
                    modifier = Modifier
                        .widthIn(min = 2.dp)
                        .weight(targetWeight)
                        .height(14.dp)
                        .background(category.category.color)
                )
            }
        }
    }
}

@Composable
fun Debts() {
    QuickAction(
        modifier = Modifier.height(96.dp),
        title = "Debts",
        subtitle = "No active debts for now",
        onClick = {}
    )
}

@Composable
fun TopCategories(
    transactionsPerCategories: List<CategoryTotalTransaction>,
) {
    QuickAction(
        modifier = Modifier.height(208.dp),
        title = "Top categories",
        onClick = {}
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            transactionsPerCategories.take(3).forEach { category ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryBox(
                        modifier = Modifier.padding(end = 4.dp),
                        category = category.category,
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = category.category.name,
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Text(
                            text = "${category.total}€",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                }
            }
        }
        if (transactionsPerCategories.size > 3) {
            Text(
                text = "+ ${transactionsPerCategories.size - 3} more",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun Subscriptions() {
    QuickAction(
        modifier = Modifier.height(96.dp),
        title = "Subscriptions",
        subtitle = "You have 3 active subscriptions",
        onClick = {}
    )
}

@Preview
@Composable
fun MyWalletPreview() {
    ExpenseTrackerTheme {
        MyWallet(wallets = sampleWallets, onClick = {})
    }
}

@Preview
@Composable
fun AllTransactionsPreview() {
    ExpenseTrackerTheme {
        AllTransactions(listOf())
    }
}

@Preview
@Composable
fun DebtsPreview() {
    ExpenseTrackerTheme {
        Debts()
    }
}

@Preview
@Composable
fun TopCategoriesPreview() {
    ExpenseTrackerTheme {
        TopCategories(listOf())
    }
}

@Preview
@Composable
fun SubscriptionsPreview() {
    ExpenseTrackerTheme {
        Subscriptions()
    }
}

@Preview
@Composable
fun QuickActionGridPreview() {
    ExpenseTrackerTheme {
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            columns = StaggeredGridCells.Fixed(2),
        ) {
            item {
                MyWallet(wallets = sampleWallets, onClick = {})
            }
            item {
                AllTransactions(listOf())
            }
            item {
                Debts()
            }
            item {
                TopCategories(listOf())
            }
            item {
                Subscriptions()
            }
        }
    }
}