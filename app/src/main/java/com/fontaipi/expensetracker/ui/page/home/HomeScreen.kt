package com.fontaipi.expensetracker.ui.page.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fontaipi.expensetracker.R
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
import com.fontaipi.expensetracker.ui.page.add.transaction.TransactionType
import com.fontaipi.expensetracker.ui.theme.ExpenseTrackerTheme
import java.time.ZoneId

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
    navigateToWallets: () -> Unit,
) {
    val homePageState by viewModel.homePageState.collectAsStateWithLifecycle()
    val transactionsState by viewModel.transactionState.collectAsStateWithLifecycle()
    HomeScreen(
        homePageState = homePageState,
        transactionsState = transactionsState,
        updateScaffoldViewState = updateScaffoldViewState,
        navigateToAddTransaction = navigateToAddTransaction,
        navigateToWallets = navigateToWallets,
    )
}

@Composable
fun HomeScreen(
    homePageState: HomePageState,
    transactionsState: TransactionsState,
    updateScaffoldViewState: (ScaffoldViewState) -> Unit,
    navigateToAddTransaction: () -> Unit,
    navigateToWallets: () -> Unit,
) {
    val accountsTotal = shouldShowWalletsTotal(homePageState)
    val scrollingState = rememberScrollState()
    LaunchedEffect(homePageState) {
        updateScaffoldViewState(
            ScaffoldViewState(
                topAppBarTitle = "$accountsTotal€",
                onFabClick = navigateToAddTransaction,
            )
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollingState)
            .padding(top = 16.dp, bottom = 82.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        when (homePageState) {
            is HomePageState.Success -> {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(320.dp),
                    verticalItemSpacing = 16.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    columns = StaggeredGridCells.Fixed(2),
                ) {
                    item {
                        MyWallet(
                            wallets = homePageState.wallets,
                            onClick = navigateToWallets
                        )
                    }
                    item {
                        AllTransactions(categoryTotalTransactions = homePageState.categoryTotalTransaction)
                    }
                    item {
                        Debts()
                    }
                    item {
                        TopCategories(transactionsPerCategories = homePageState.categoryTotalTransaction)
                    }
                    item {
                        Subscriptions()
                    }
                }
            }

            HomePageState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SectionTitleWithButton(
                    title = "Transactions",
                    buttonText = "See all",
                    onButtonClick = {})
                when (transactionsState) {
                    is TransactionsState.Success -> {
                        val date by remember {
                            derivedStateOf {
                                transactionsState.transactions.firstOrNull()?.date?.let {
                                    val date = it.atZone(ZoneId.systemDefault()).toLocalDate()
                                    "${
                                        date.month.name.lowercase()
                                            .replaceFirstChar(Char::titlecase)
                                    } ${date.year}"
                                } ?: ""
                            }
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = date, style = MaterialTheme.typography.titleMedium)
                            Transactions(
                                modifier = Modifier.fillMaxWidth(),
                                transactions = transactionsState.transactions
                            )
                        }

                    }

                    TransactionsState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun shouldShowWalletsTotal(
    homePageState: HomePageState,
): String = when (homePageState) {
    HomePageState.Loading -> "0"
    is HomePageState.Success -> homePageState.wallets.sumOf { it.balance }.toString()
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
    modifier: Modifier = Modifier,
    transactions: List<Transaction>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (transactions.isNotEmpty()) {
            transactions.forEach { transaction ->
                val sign = when (transaction.type) {
                    TransactionType.EXPENSE -> "-"
                    TransactionType.INCOME -> "+"
                    else -> ""
                }
                TransactionCard(
                    category = transaction.category,
                    hashtags = transaction.hashtags,
                    price = "$sign ${transaction.amount}€",
                    wallet = transaction.wallet,
                )
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            TransactionsEmpty(
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}

@Composable
fun TransactionsEmpty(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            modifier = Modifier.height(120.dp),
            painter = painterResource(id = R.drawable.no_data),
            contentDescription = "no data"
        )
        Text(
            text = "No transactions yet",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "Add your first transaction to see it here",
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelMedium,
        )
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