package com.fontaipi.expensetracker.ui.page.wallets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fontaipi.expensetracker.data.database.entity.asExternalModel
import com.fontaipi.expensetracker.data.database.entity.sampleMainAccount
import com.fontaipi.expensetracker.model.Wallet
import com.fontaipi.expensetracker.ui.component.SectionTitle
import com.fontaipi.expensetracker.ui.page.home.Transactions
import com.fontaipi.expensetracker.ui.page.home.TransactionsState
import com.fontaipi.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun WalletsRoute(
    viewModel: WalletsViewModel = hiltViewModel(),
    navigateToAddWallet: () -> Unit,
    onBackClick: () -> Unit,
) {
    val accountsState by viewModel.walletState.collectAsStateWithLifecycle()
    val transactionsState by viewModel.transactionsState.collectAsStateWithLifecycle()
    WalletsScreen(
        navigateToAddWallet = navigateToAddWallet,
        accountsState = accountsState,
        transactionsState = transactionsState,
        selectAccount = viewModel::selectAccount,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WalletsScreen(
    navigateToAddWallet: () -> Unit,
    accountsState: AccountState,
    transactionsState: TransactionsState,
    selectAccount: (Long) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("All wallets")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        },
    ) { paddingsValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(paddingsValues),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                when (accountsState) {
                    is AccountState.Success -> {
                        val pagerState =
                            rememberPagerState(pageCount = { accountsState.wallets.size })

                        LaunchedEffect(pagerState.currentPage) {
                            val selectedAccount = accountsState.wallets[pagerState.currentPage]
                            selectAccount(selectedAccount.id)
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            FilledTonalButton(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(horizontal = 16.dp),
                                onClick = navigateToAddWallet
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(text = "Add wallet")
                            }
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) { page ->
                                WalletCard(
                                    modifier = Modifier.padding(16.dp),
                                    wallet = accountsState.wallets[page]
                                )
                            }

                            Indicators(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                size = accountsState.wallets.size,
                                index = pagerState.currentPage
                            )
                        }
                    }

                    AccountState.Loading -> CircularProgressIndicator()
                }

            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SectionTitle(title = "Transactions")
                        when (transactionsState) {
                            is TransactionsState.Success -> {
                                Transactions(
                                    modifier = Modifier.fillMaxWidth(),
                                    transactions = transactionsState.transactions
                                )
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
    }
}

@Composable
fun WalletCard(
    modifier: Modifier = Modifier,
    wallet: Wallet
) {
    Surface(
        modifier = modifier,
        color = wallet.colors.primary,
        contentColor = Color.White,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(236.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                SectionTitle(title = wallet.name)
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { },
                )
            }

            Column {
                Text(text = "Balance", style = MaterialTheme.typography.bodyLarge)
                Text(text = "${wallet.balance}€", style = MaterialTheme.typography.displaySmall)
            }
        }
    }
}

@Composable
fun Indicators(modifier: Modifier = Modifier, size: Int, index: Int) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
            )
    ) {

    }
}

@Preview
@Composable
fun WalletCardPreview() {
    ExpenseTrackerTheme {
        WalletCard(wallet = sampleMainAccount.asExternalModel())
    }
}