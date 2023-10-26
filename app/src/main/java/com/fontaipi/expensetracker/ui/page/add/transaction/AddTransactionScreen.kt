package com.fontaipi.expensetracker.ui.page.add.transaction

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.automirrored.rounded.CompareArrows
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NoCell
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fontaipi.expensetracker.data.database.entity.TransactionEntity
import com.fontaipi.expensetracker.model.Category
import com.fontaipi.expensetracker.model.CategoryIcon
import com.fontaipi.expensetracker.model.Wallet
import com.fontaipi.expensetracker.model.categoryIconMap
import com.fontaipi.expensetracker.ui.component.CategoryBox
import com.fontaipi.expensetracker.ui.component.SectionTitle
import com.fontaipi.expensetracker.ui.component.SelectCategoryCard
import com.fontaipi.expensetracker.ui.component.SelectWalletCard
import com.fontaipi.expensetracker.ui.component.TimePickerDialog
import com.fontaipi.expensetracker.ui.component.WalletIcon
import com.fontaipi.expensetracker.ui.theme.CategoryBlue
import com.fontaipi.expensetracker.ui.theme.CategoryGreen
import com.fontaipi.expensetracker.ui.theme.CategoryRed
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

val sampleCategories = listOf(
    Category(
        name = "Groceries",
        color = CategoryBlue,
        icon = CategoryIcon.GROCERIES,
    ),
    Category(
        name = "Shopping",
        color = CategoryGreen,
        icon = CategoryIcon.SHOPPING,
    ),
    Category(
        name = "Transportation",
        color = CategoryRed,
        icon = CategoryIcon.TRANSPORT,
    ),
)

enum class TransactionType(val title: String, val icon: ImageVector) {
    EXPENSE(
        icon = Icons.AutoMirrored.Filled.CallReceived,
        title = "Expense"
    ),
    INCOME(
        icon = Icons.AutoMirrored.Filled.CallMade,
        title = "Income"
    ),
    TRANSFER(
        icon = Icons.AutoMirrored.Rounded.CompareArrows,
        title = "Transfer"
    )
}


@Composable
fun AddTransactionRoute(
    viewModel: AddTransactionViewModel = hiltViewModel(),
    onCloseClick: () -> Unit,
) {
    val addTransactionState by viewModel.addTransactionState.collectAsState()
    AddTransactionScreen(
        addTransactionState = addTransactionState,
        addTransaction = viewModel::addTransaction,
        onCloseClick = onCloseClick,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    addTransactionState: AddTransactionState,
    addTransaction: (TransactionEntity) -> Unit,
    onCloseClick: () -> Unit,
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                actions = {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) { paddingsValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(paddingsValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        tabPositions.maxOf { it.contentWidth }
                        val width by animateDpAsState(
                            targetValue = tabPositions.maxOf { it.contentWidth },
                            label = "animateWidth"
                        )
                        TabRowDefaults.PrimaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            width = width,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            ) {
                TransactionType.entries.forEachIndexed { index, transactionType ->
                    Tab(
                        selectedContentColor = MaterialTheme.colorScheme.onSurface,
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = transactionType.title,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = transactionType.icon,
                                contentDescription = null,
                            )

                        }
                    )
                }
            }

            when (addTransactionState) {
                is AddTransactionState.Success -> {
                    when (selectedTabIndex) {
                        0 -> Expense(
                            wallets = addTransactionState.wallets,
                            categories = addTransactionState.categories,
                            addTransaction = addTransaction,
                            onCloseClick = onCloseClick
                        )

                        1 -> Income(
                            wallets = addTransactionState.wallets,
                            addTransaction = addTransaction,
                            onCloseClick = onCloseClick
                        )

                        2 -> Text("Transfer")
                    }
                }

                is AddTransactionState.Loading -> {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Expense(
    wallets: List<Wallet>,
    categories: List<Category>,
    addTransaction: (TransactionEntity) -> Unit,
    onCloseClick: () -> Unit
) {
    var showCategoryBottomSheet by remember { mutableStateOf(false) }
    var showAccountBottomSheet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var amount by remember { mutableStateOf("") }
    var selectedCategoryId by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedWalletId by rememberSaveable { mutableStateOf(wallets.firstOrNull()?.id) }
    val selectedWallet by remember { derivedStateOf { wallets.firstOrNull { it.id == selectedWalletId } } }

    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val timePickerState = rememberTimePickerState(
        initialHour = Instant.now().atZone(ZoneId.systemDefault()).hour,
        initialMinute = Instant.now().atZone(ZoneId.systemDefault()).minute
    )

    val selectedDate by remember {
        derivedStateOf {
            val selectedDate = Instant.ofEpochMilli(
                datePickerState.selectedDateMillis ?: System.currentTimeMillis()
            ).atZone(ZoneId.systemDefault()).toLocalDate()
            val combinedDateTime = LocalDateTime.of(
                selectedDate.year,
                selectedDate.month,
                selectedDate.dayOfMonth,
                timePickerState.hour,
                timePickerState.minute
            )
            combinedDateTime.atZone(ZoneId.systemDefault()).toInstant()
        }
    }

    var isRepeatPayment by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                prefix = { Text(text = "-") },
                shape = MaterialTheme.shapes.small,
                value = amount,
                onValueChange = { amount = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = {
                    Text(
                        text = "100.00",
                        color = MaterialTheme.colorScheme.outline
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                )
            )

            Surface(
                onClick = {},
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small,
            ) {
                Box(
                    modifier = Modifier.size(56.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "EUR", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SelectCategoryCard(
                modifier = Modifier.weight(1f),
                selectedCategory = categories.firstOrNull { it.id == selectedCategoryId },
                onClick = { showCategoryBottomSheet = true }
            )

            SelectWalletCard(
                modifier = Modifier.weight(1f),
                selectedWallet = selectedWallet,
                onClick = { showAccountBottomSheet = true }
            )
        }

        Column {
            SectionTitle(title = "Additional information")
            Column {
                InfoWithLabel(
                    label = "Date",
                    info = {
                        Text(
                            text = if (datePickerState.selectedDateMillis != null) convertMillisToDate(
                                selectedDate.toEpochMilli()
                            ) else "Not set", style = MaterialTheme.typography.labelLarge
                        )
                    },
                    onClick = { showDatePicker = true }
                )
                InfoWithLabel(
                    label = "Labels",
                    info = {
                        Text(
                            text = "Not specified",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    onClick = {}
                )
                InfoWithLabel(
                    label = "Commentary",
                    info = { Text(text = "None", style = MaterialTheme.typography.labelLarge) },
                    onClick = {}
                )
                InfoWithLabel(
                    label = "Repeat payment",
                    info = {
                        Switch(
                            checked = isRepeatPayment,
                            onCheckedChange = null,
                        )
                    },
                    onClick = { isRepeatPayment = !isRepeatPayment }
                )
            }

        }
        FilledTonalButton(
            enabled = amount.isNotEmpty() && selectedCategoryId != null && selectedWalletId != null,
            onClick = {
                addTransaction(
                    TransactionEntity(
                        amount = amount.toFloat().toBigDecimal(),
                        type = TransactionType.EXPENSE,
                        categoryId = selectedCategoryId!!,
                        accountId = selectedWalletId!!,
                        date = selectedDate
                    )
                )
                onCloseClick()
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Add transaction", style = MaterialTheme.typography.labelLarge)
        }
    }

    if (showCategoryBottomSheet) {
        SelectCategoryBottomSheet(
            categories = categories,
            onCategoryClick = { selectedCategoryId = it },
            onDismissRequest = {
                showCategoryBottomSheet = false
            }
        )
    }

    if (showAccountBottomSheet) {
        SelectAccountBottomSheet(
            wallets = wallets,
            onCategoryClick = { selectedWalletId = it },
            onDismissRequest = {
                showAccountBottomSheet = false
            }
        )
    }

    if (showDatePicker) {
        val confirmEnabled by remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        showTimePicker = true
                    },
                    enabled = confirmEnabled
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onCancel = {
                showTimePicker = false
            },
            onConfirm = {
                showTimePicker = false
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Income(
    wallets: List<Wallet>,
    addTransaction: (TransactionEntity) -> Unit,
    onCloseClick: () -> Unit
) {
    var showAccountBottomSheet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var amount by remember { mutableStateOf("") }
    var selectedWalletId by rememberSaveable { mutableStateOf(wallets.firstOrNull()?.id) }
    val selectedWallet by remember { derivedStateOf { wallets.firstOrNull { it.id == selectedWalletId } } }

    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val timePickerState = rememberTimePickerState(
        initialHour = Instant.now().atZone(ZoneId.systemDefault()).hour,
        initialMinute = Instant.now().atZone(ZoneId.systemDefault()).minute
    )

    val selectedDate by remember {
        derivedStateOf {
            val selectedDate = Instant.ofEpochMilli(
                datePickerState.selectedDateMillis ?: System.currentTimeMillis()
            ).atZone(ZoneId.systemDefault()).toLocalDate()
            val combinedDateTime = LocalDateTime.of(
                selectedDate.year,
                selectedDate.month,
                selectedDate.dayOfMonth,
                timePickerState.hour,
                timePickerState.minute
            )
            combinedDateTime.atZone(ZoneId.systemDefault()).toInstant()
        }
    }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                prefix = { Text(text = "+") },
                shape = MaterialTheme.shapes.small,
                value = amount,
                onValueChange = { amount = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = {
                    Text(
                        text = "100.00",
                        color = MaterialTheme.colorScheme.outline
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                )
            )

            Surface(
                onClick = {},
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small,
            ) {
                Box(
                    modifier = Modifier.size(56.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "EUR", style = MaterialTheme.typography.labelLarge)
                }
            }
        }


        SelectWalletCard(
            modifier = Modifier.fillMaxWidth(0.5f),
            selectedWallet = selectedWallet,
            onClick = { showAccountBottomSheet = true }
        )

        Column {
            SectionTitle(title = "Additional information")
            Column {
                InfoWithLabel(
                    label = "Date",
                    info = {
                        Text(
                            text = if (datePickerState.selectedDateMillis != null) convertMillisToDate(
                                selectedDate.toEpochMilli()
                            ) else "Not set", style = MaterialTheme.typography.labelLarge
                        )
                    },
                    onClick = { showDatePicker = true }
                )
                InfoWithLabel(
                    label = "Labels",
                    info = {
                        Text(
                            text = "Not specified",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    onClick = {}
                )
                InfoWithLabel(
                    label = "Commentary",
                    info = { Text(text = "None", style = MaterialTheme.typography.labelLarge) },
                    onClick = {}
                )
            }

        }
        FilledTonalButton(
            enabled = amount.isNotEmpty() && selectedWalletId != null,
            onClick = {
                addTransaction(
                    TransactionEntity(
                        amount = amount.toFloat().toBigDecimal(),
                        type = TransactionType.INCOME,
                        categoryId = 1,
                        accountId = selectedWalletId!!,
                        date = selectedDate
                    )
                )
                onCloseClick()
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Add transaction", style = MaterialTheme.typography.labelLarge)
        }
    }

    if (showAccountBottomSheet) {
        SelectAccountBottomSheet(
            wallets = wallets,
            onCategoryClick = { selectedWalletId = it },
            onDismissRequest = {
                showAccountBottomSheet = false
            }
        )
    }

    if (showDatePicker) {
        val confirmEnabled by remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        showTimePicker = true
                    },
                    enabled = confirmEnabled
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onCancel = {
                showTimePicker = false
            },
            onConfirm = {
                showTimePicker = false
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCategoryBottomSheet(
    categories: List<Category>,
    onDismissRequest: () -> Unit,
    onCategoryClick: (Long) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = {
            coroutineScope.launch { bottomSheetState.hide() }
            onDismissRequest()
        },
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Choose category",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(categories) {
                    CategoryCard(
                        category = it,
                        onClick = {
                            coroutineScope.launch { bottomSheetState.hide() }
                            onCategoryClick(it.id)
                            onDismissRequest()
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAccountBottomSheet(
    wallets: List<Wallet>,
    onDismissRequest: () -> Unit,
    onCategoryClick: (Long) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = {
            coroutineScope.launch { bottomSheetState.hide() }
            onDismissRequest()
        },
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Choose wallet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(wallets) {
                    AccountCard(
                        wallet = it,
                        onClick = {
                            coroutineScope.launch { bottomSheetState.hide() }
                            onCategoryClick(it.id)
                            onDismissRequest()
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CategoryBox(
            icon = categoryIconMap[category.icon] ?: Icons.Default.NoCell,
            containerColor = category.color,
        )
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun AccountCard(
    wallet: Wallet,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        WalletIcon(walletColors = wallet.colors)
        Text(
            text = wallet.name,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun InfoWithLabel(
    modifier: Modifier = Modifier,
    label: String,
    info: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)
        info()
    }
}

fun convertMillisToDate(millis: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val date = Date(millis)
    return dateFormat.format(date)
}