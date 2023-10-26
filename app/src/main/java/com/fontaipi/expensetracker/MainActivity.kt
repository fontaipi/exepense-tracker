package com.fontaipi.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fontaipi.expensetracker.data.database.dao.CategoryDao
import com.fontaipi.expensetracker.data.database.dao.WalletDao
import com.fontaipi.expensetracker.data.database.entity.categories
import com.fontaipi.expensetracker.ui.page.settings.UserPreferencesState
import com.fontaipi.expensetracker.ui.theme.ExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class ScaffoldViewState(
    val topAppBarTitle: String? = null,
    val onFabClick: (() -> Unit)? = null,
    val fabIcon: ImageVector? = null,
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var walletDao: WalletDao

    @Inject
    lateinit var categoryDao: CategoryDao

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var userPreferencesState: UserPreferencesState by mutableStateOf(UserPreferencesState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userPreferencesState
                    .onEach {
                        userPreferencesState = it
                    }
                    .collect()
            }
        }

        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition {
            when (userPreferencesState) {
                UserPreferencesState.Loading -> true
                is UserPreferencesState.Success -> false
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            if (categoryDao.countCategories() == 0) {
                categoryDao.upsertCategories(categories)
            }
        }
        setContent {
            ExpenseTrackerTheme(
                darkTheme = shouldUseDarkTheme(userPreferencesState),
                dynamicColor = !shouldDisableDynamicTheming(userPreferencesState),
            ) {
                ExpenseTrackerApp()
            }
        }
    }
}

/**
 * Returns `true` if the dynamic color is disabled, as a function of the [uiState].
 */
@Composable
private fun shouldDisableDynamicTheming(
    userPreferencesState: UserPreferencesState,
): Boolean = when (userPreferencesState) {
    UserPreferencesState.Loading -> false
    is UserPreferencesState.Success -> !userPreferencesState.userData.useDynamicColor
}

/**
 * Returns `true` if dark theme should be used, as a function of the [uiState] and the
 * current system context.
 */
@Composable
private fun shouldUseDarkTheme(
    userPreferencesState: UserPreferencesState,
): Boolean = when (userPreferencesState) {
    UserPreferencesState.Loading -> isSystemInDarkTheme()
    is UserPreferencesState.Success -> userPreferencesState.userData.useDarkMode
}