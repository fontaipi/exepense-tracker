package com.fontaipi.expensetracker.ui.page.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fontaipi.expensetracker.ScaffoldViewState
import com.fontaipi.expensetracker.model.Transaction
import com.fontaipi.expensetracker.ui.component.AllTransactions
import com.fontaipi.expensetracker.ui.component.Debts
import com.fontaipi.expensetracker.ui.component.MyWallet
import com.fontaipi.expensetracker.ui.component.SectionTitleWithButton
import com.fontaipi.expensetracker.ui.component.StockCard
import com.fontaipi.expensetracker.ui.component.Subscriptions
import com.fontaipi.expensetracker.ui.component.TopCategories
import com.fontaipi.expensetracker.ui.component.TransactionCard
import com.fontaipi.expensetracker.ui.theme.ExpenseTrackerTheme

data class Account(
    val name: String,
    val color: Color,
)

data class TransactionCategory(
    val name: String,
    val color: Color,
    val icon: ImageVector
)

data class Stock(
    val ticker: String,
    val name: String,
    val change: Float,
    val price: String,
)

val sampleStocks = listOf(
    Stock(
        ticker = "APPL",
        name = "Apple Inc.",
        change = 7.12f,
        price = "123,45€",
    ),
    Stock(
        ticker = "APPL",
        name = "Apple Inc.",
        change = 7.12f,
        price = "123,45€",
    ),
    Stock(
        ticker = "APPL",
        name = "Apple Inc.",
        change = 7.12f,
        price = "123,45€",
    ),
)

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    updateScaffoldViewState: (ScaffoldViewState) -> Unit,
    navigateToAddTransaction: () -> Unit,
) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    HomeScreen(
        updateScaffoldViewState = updateScaffoldViewState,
        navigateToAddTransaction = navigateToAddTransaction,
        transactions = transactions,
    )
}

@Composable
fun HomeScreen(
    updateScaffoldViewState: (ScaffoldViewState) -> Unit,
    navigateToAddTransaction: () -> Unit,
    transactions: List<Transaction>,
) {
    LaunchedEffect(Unit) {
        updateScaffoldViewState(
            ScaffoldViewState(
                onFabClick = navigateToAddTransaction,
            )
        )
    }

    // get context in compose
    val context = LocalContext.current
    context.resources.getIdentifier("myIconName", "drawable", context.packageName)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 82.dp),
    ) {

        item {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(320.dp),
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


        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                StockWatchlist()

                Transactions(
                    transactions = transactions
                )
            }
        }
    }
}

@Composable
fun StockWatchlist() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SectionTitleWithButton(
            title = "My watchlist", buttonText = "See all", onButtonClick = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(sampleStocks) {
                StockCard(
                    ticker = it.ticker,
                    name = it.name,
                    change = it.change,
                    price = it.price,
                )
            }
        }
    }
}

@Composable
fun Transactions(
    transactions: List<Transaction>
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SectionTitleWithButton(title = "Transactions", buttonText = "See all", onButtonClick = {})
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            transactions.forEach { transaction ->
                val sign = when (transaction.type) {
                    TransactionType.EXPENSE -> "-"
                    TransactionType.INCOME -> "+"
                    else -> ""
                }
                TransactionCard(
                    category = transaction.category,
                    hashtags = transaction.hashtags,
                    price = "$sign ${transaction.price}€",
                    account = transaction.account,
                )
            }
        }
    }
}

@Preview
@Composable
fun SectionTitleWithButtonPreview() {
    ExpenseTrackerTheme {
        SectionTitleWithButton(
            title = "My watchlist",
            buttonText = "See all",
            onButtonClick = {},
        )
    }
}