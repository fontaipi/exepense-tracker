package com.fontaipi.expensetracker.ui.page.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fontaipi.expensetracker.ScaffoldViewState
import com.fontaipi.expensetracker.model.UserData
import com.fontaipi.expensetracker.ui.component.SectionTitle
import com.fontaipi.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel(),
    updateScaffoldViewState: (ScaffoldViewState) -> Unit,
) {
    val userPreferencesState by viewModel.userPreferencesState.collectAsStateWithLifecycle()
    SettingsScreen(
        userPreferencesState = userPreferencesState,
        updateScaffoldViewState = updateScaffoldViewState,
        toggleDarkTheme = viewModel::toggleDarkTheme,
        toggleDynamicColor = viewModel::toggleDynamicColor,
    )
}

@Composable
fun SettingsScreen(
    userPreferencesState: UserPreferencesState,
    updateScaffoldViewState: (ScaffoldViewState) -> Unit,
    toggleDarkTheme: () -> Unit,
    toggleDynamicColor: () -> Unit,
) {
    LaunchedEffect(Unit) {
        updateScaffoldViewState(
            ScaffoldViewState(topAppBarTitle = "Settings")
        )
    }

    when (userPreferencesState) {
        is UserPreferencesState.Success -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SectionTitle(title = "Appearance", modifier = Modifier.padding(horizontal = 16.dp))
                SettingsRow(
                    icon = { Icon(imageVector = Icons.Filled.Palette, contentDescription = null) },
                    title = "Dynamic color",
                    onClick = toggleDynamicColor,
                ) {
                    Switch(
                        checked = userPreferencesState.userData.useDynamicColor,
                        onCheckedChange = { toggleDynamicColor() })
                }
                SettingsRow(
                    icon = { Icon(imageVector = Icons.Filled.DarkMode, contentDescription = null) },
                    title = "Dark theme",
                    subTitle = "Use dark theme automatically",
                    onClick = toggleDarkTheme,
                ) {
                    Switch(
                        checked = userPreferencesState.userData.useDarkMode,
                        onCheckedChange = { toggleDarkTheme() })
                }
            }
        }

        UserPreferencesState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

}

@Composable
fun SettingsRow(
    icon: @Composable () -> Unit,
    title: String,
    subTitle: String? = null,
    onClick: () -> Unit,
    content: @Composable (() -> Unit)? = null,
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            icon()
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                subTitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            if (content != null) {
                Spacer(modifier = Modifier.weight(1f))
                content()
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

}

@Preview
@Composable
fun SettingsScreenPreview() {
    ExpenseTrackerTheme {
        SettingsScreen(
            userPreferencesState = UserPreferencesState.Success(
                userData = UserData(
                    useDynamicColor = true,
                    useDarkMode = false,
                )
            ),
            updateScaffoldViewState = {},
            toggleDarkTheme = {},
            toggleDynamicColor = {},
        )
    }
}