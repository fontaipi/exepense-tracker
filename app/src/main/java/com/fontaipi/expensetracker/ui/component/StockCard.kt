package com.fontaipi.expensetracker.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fontaipi.expensetracker.ui.theme.ExpenseTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockCard(
    ticker: String = "APPL",
    name: String = "Apple Inc.",
    change: Float = 7.12f,
    price: String = "123,45€",
) {
    val changeColor = if (change > 0) {
        Color(0xFF37B969)
    } else {
        MaterialTheme.colorScheme.error
    }

    val changeText by remember {
        derivedStateOf {
            if (change > 0) {
                "+ $change%"
            } else {
                " $change%"
            }
        }
    }

    Card(
        onClick = { },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
    ) {
        Column(
            modifier = Modifier
                .size(120.dp)
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = ticker, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Column {
                Text(
                    text = changeText,
                    style = MaterialTheme.typography.labelLarge,
                    color = changeColor
                )
                Text(text = price, style = MaterialTheme.typography.titleMedium)
            }

        }
    }
}

@Preview
@Composable
fun StockCardPreview() {
    ExpenseTrackerTheme {
        StockCard()
    }
}