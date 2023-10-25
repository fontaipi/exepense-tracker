package com.fontaipi.expensetracker.ui.page.add.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fontaipi.expensetracker.data.database.entity.WalletColors
import com.fontaipi.expensetracker.data.database.entity.WalletEntity
import com.fontaipi.expensetracker.ui.component.SectionTitle
import com.fontaipi.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun AddWalletRoute(
    viewModel: AddWalletViewModel = hiltViewModel(),
    onCloseClick: () -> Unit,
) {
    AddWalletScreen(
        addWallet = viewModel::addWallet,
        onCloseClick = onCloseClick,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWalletScreen(
    addWallet: (WalletEntity) -> Unit,
    onCloseClick: () -> Unit,
) {
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
        var initialAmount by rememberSaveable { mutableStateOf("") }
        var walletName by rememberSaveable { mutableStateOf("") }
        var selectedColor by rememberSaveable { mutableIntStateOf(WalletColors.Type1.ordinal) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(paddingsValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionTitle(
                title = "New account",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.small,
                value = walletName,
                onValueChange = { walletName = it },
                placeholder = {
                    Text(
                        text = "Account name",
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.small,
                    value = initialAmount,
                    onValueChange = { initialAmount = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = {
                        Text(
                            text = "Initial amount",
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
            SectionTitle(
                title = "Customize",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            LazyRow(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(WalletColors.entries) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(color = it.primary)
                            .border(
                                width = 3.dp,
                                color = if (selectedColor == it.ordinal) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    Color.Transparent
                                },
                                shape = CircleShape
                            )
                            .clickable { selectedColor = it.ordinal }
                    )
                }
            }
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    addWallet(
                        WalletEntity(
                            name = walletName,
                            balance = initialAmount.toFloat().toBigDecimal(),
                            currency = "EUR",
                            colors = WalletColors.entries[selectedColor]
                        )
                    )
                    onCloseClick()
                },
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Create account", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview
@Composable
fun AddWalletPreview() {
    ExpenseTrackerTheme {
        AddWalletScreen(onCloseClick = {}, addWallet = {})
    }
}