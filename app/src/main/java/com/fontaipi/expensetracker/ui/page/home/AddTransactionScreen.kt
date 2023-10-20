package com.fontaipi.expensetracker.ui.page.home

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
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fontaipi.expensetracker.data.transaction.sampleMainAccount
import com.fontaipi.expensetracker.model.Transaction
import com.fontaipi.expensetracker.ui.component.CategoryBox
import com.fontaipi.expensetracker.ui.component.SectionTitle
import com.fontaipi.expensetracker.ui.component.WalletIcon
import com.fontaipi.expensetracker.ui.theme.CategoryBlue
import com.fontaipi.expensetracker.ui.theme.CategoryGreen
import com.fontaipi.expensetracker.ui.theme.CategoryPurple
import com.fontaipi.expensetracker.ui.theme.CategoryRed
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val sampleCategories = listOf(
    TransactionCategory(
        name = "Groceries",
        color = CategoryBlue,
        icon = Icons.Outlined.ShoppingCart,
    ),
    TransactionCategory(
        name = "Shopping",
        color = CategoryGreen,
        icon = Icons.Outlined.ShoppingBag,
    ),
    TransactionCategory(
        name = "Transportation",
        color = CategoryRed,
        icon = Icons.Outlined.Train,
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
    ),
    DEBT(
        icon = Icons.Rounded.RestartAlt,
        title = "Debt"
    ),
}

@Composable
fun AddTransactionRoute(
    viewModel: AddTransactionViewModel = hiltViewModel(),
    onCloseClick: () -> Unit,
) {
    AddTransactionScreen(
        addTransaction = viewModel::addTransaction,
        onCloseClick = onCloseClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    addTransaction: (Transaction) -> Unit,
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
            when (selectedTabIndex) {
                0 -> Expense(
                    addTransaction = addTransaction,
                    onCloseClick = onCloseClick
                )

                1 -> Text("Income")
                2 -> Text("Transfer")
                4 -> Text("Debt")
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Expense(
    addTransaction: (Transaction) -> Unit,
    onCloseClick: () -> Unit
) {
    var showCategoryBottomSheet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    var amount by remember { mutableStateOf("") }
    var selectedCategoryId by rememberSaveable { mutableStateOf<Int?>(null) }
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
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
                suffix = { Text(text = "€") },
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
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(96.dp),
                onClick = { showCategoryBottomSheet = true },
                shape = MaterialTheme.shapes.small,
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(
                            "Choose category",
                            style = MaterialTheme.typography.titleMedium.copy(lineHeight = 18.sp),
                            modifier = Modifier.weight(1f)
                        )
                        if (selectedCategoryId == null) {
                            val stroke = Stroke(
                                width = 4f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                            val strokeColor = MaterialTheme.colorScheme.onSurface
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .drawBehind {
                                        drawRoundRect(
                                            color = strokeColor,
                                            style = stroke,
                                            cornerRadius = CornerRadius(10.dp.toPx())
                                        )
                                    }
                            )
                        } else {
                            CategoryBox(
                                category = sampleCategories[selectedCategoryId!!],
                                modifier = Modifier.size(36.dp)
                            )
                        }

                    }
                    Text(
                        "Tap to select",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(96.dp),
                onClick = {},
                shape = MaterialTheme.shapes.small,
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(
                            "Saving account",
                            style = MaterialTheme.typography.titleMedium.copy(lineHeight = 18.sp),
                            modifier = Modifier.weight(1f)
                        )
                        WalletIcon(
                            primaryColor = CategoryPurple,
                            secondaryColor = CategoryBlue,
                        )
                    }
                    Text(
                        "Tap to select",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }

        Column {
            SectionTitle(title = "Additional information")
            Column(
            ) {
                InfoWithLabel(
                    label = "Date",
                    info = {
                        Text(
                            text = if (datePickerState.selectedDateMillis != null) convertMillisToDate(
                                datePickerState.selectedDateMillis!!
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
            enabled = amount.isNotEmpty() && selectedCategoryId != null,
            onClick = {
                addTransaction(
                    Transaction(
                        price = amount.toBigDecimal(),
                        type = TransactionType.EXPENSE,
                        category = sampleCategories[selectedCategoryId!!],
                        account = sampleMainAccount,
                        hashtags = emptySet(),
                        date = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
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
            onCategoryClick = { selectedCategoryId = it },
            onDismissRequest = {
                showCategoryBottomSheet = false
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCategoryBottomSheet(
    onDismissRequest: () -> Unit,
    onCategoryClick: (Int) -> Unit,
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
                items(sampleCategories) {
                    CategoryCard(
                        category = it,
                        onClick = { categoryId ->
                            coroutineScope.launch { bottomSheetState.hide() }
                            onCategoryClick(categoryId)
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
    category: TransactionCategory,
    onClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = { onClick(sampleCategories.indexOf(category)) })
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CategoryBox(category = category)
        Text(
            text = category.name,
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
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = Date(millis)
    return dateFormat.format(date)
}