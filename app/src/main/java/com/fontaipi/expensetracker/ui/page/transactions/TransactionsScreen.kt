package com.fontaipi.expensetracker.ui.page.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fontaipi.expensetracker.ui.page.home.TransactionsState
import com.fontaipi.expensetracker.ui.page.wallets.transactionsByDay
import java.text.DateFormatSymbols
import java.time.LocalDate

@Composable
fun TransactionsRoute(
    onBackClick: () -> Unit,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val selectedPeriod by viewModel.selectedPeriod.collectAsStateWithLifecycle()
    val transactionsState by viewModel.transactionState.collectAsStateWithLifecycle()
    TransactionsScreen(
        selectedPeriod = selectedPeriod,
        selectPeriod = viewModel::selectPeriod,
        transactionsState = transactionsState,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    selectedPeriod: Pair<Int, Int>,
    selectPeriod: (month: Int, year: Int) -> Unit,
    transactionsState: TransactionsState,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Transactions")
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
    ) { paddingsValues ->
        val listState = rememberLazyListState()

        LaunchedEffect(Unit) {
            listState.scrollToItem(11)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(paddingsValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(12) {
                    val targetDate by remember {
                        derivedStateOf {
                            LocalDate.now().minusMonths((11 - it).toLong())
                        }
                    }
                    val monthName by remember { derivedStateOf { DateFormatSymbols().months[targetDate.monthValue - 1] } }
                    AssistChip(
                        trailingIcon = {
                            if (targetDate.monthValue == selectedPeriod.first &&
                                targetDate.year == selectedPeriod.second
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null
                                )
                            }
                        },
                        label = { Text(monthName) },
                        onClick = {
                            selectPeriod(
                                targetDate.monthValue,
                                targetDate.year
                            )
                        }
                    )
                }
            }
            when (transactionsState) {

                is TransactionsState.Success -> {


                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                    ) {
                        transactionsByDay(transactions = transactionsState.transactions)
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