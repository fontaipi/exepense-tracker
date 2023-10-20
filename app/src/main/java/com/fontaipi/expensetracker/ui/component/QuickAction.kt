package com.fontaipi.expensetracker.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fontaipi.expensetracker.ui.page.home.TransactionCategory
import com.fontaipi.expensetracker.ui.page.home.sampleCategories
import com.fontaipi.expensetracker.ui.theme.CategoryBlue
import com.fontaipi.expensetracker.ui.theme.CategoryGreen
import com.fontaipi.expensetracker.ui.theme.CategoryOrange
import com.fontaipi.expensetracker.ui.theme.CategoryPurple
import com.fontaipi.expensetracker.ui.theme.CategoryRed
import com.fontaipi.expensetracker.ui.theme.ExpenseTrackerTheme
import java.math.BigDecimal

@Composable
fun QuickAction(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    content: @Composable () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
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
fun MyWallet() {
    QuickAction(
        modifier = Modifier.height(96.dp),
        title = "My wallets",
        subtitle = "3 callets in total",
        onClick = {}
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy((-20).dp)
        ) {
            WalletIcon(
                primaryColor = CategoryGreen,
                secondaryColor = CategoryOrange,
            )
            WalletIcon(
                primaryColor = CategoryOrange,
                secondaryColor = CategoryRed,
            )
            WalletIcon(
                primaryColor = CategoryPurple,
                secondaryColor = CategoryBlue,
            )
        }
    }
}

data class TransactionsPerCategory(
    val category: TransactionCategory,
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
    transactionsPerCategories: List<TransactionsPerCategory> = sampleTransactionsPerCategories,
) {
    val total = transactionsPerCategories.sumOf { it.amount }
    QuickAction(
        modifier = Modifier.height(96.dp),
        title = "All transactions",
        subtitle = "$total€ spent in July",
        onClick = {}
    ) {
        Row(
            modifier = Modifier.clip(MaterialTheme.shapes.extraSmall)
        ) {
            transactionsPerCategories.forEach { category ->
                Box(
                    modifier = Modifier
                        .weight((category.amount / total).toFloat())
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
    transactionsPerCategories: List<TransactionsPerCategory> = sampleTransactionsPerCategories,
    ) {
    QuickAction(
        modifier = Modifier.height(208.dp),
        title = "Top categories",
        onClick = {}
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                            text = "${category.amount}€",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                }
            }
        }
        Text(text = "+ ${transactionsPerCategories.size - 3} more", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
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
        MyWallet()
    }
}

@Preview
@Composable
fun AllTransactionsPreview() {
    ExpenseTrackerTheme {
        AllTransactions()
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
        TopCategories()
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
                MyWallet()
            }
            item {
                AllTransactions()
            }
            item {
                Debts()
            }
            item {
                TopCategories()
            }
            item {
                Subscriptions()
            }
        }
    }
}