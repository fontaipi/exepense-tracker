package com.fontaipi.expensetracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fontaipi.expensetracker.ui.component.BottomAppBar
import com.fontaipi.expensetracker.ui.component.TopAppBar
import com.fontaipi.expensetracker.ui.component.animatedComposableVariant
import com.fontaipi.expensetracker.ui.component.slideInVerticallyComposable
import com.fontaipi.expensetracker.ui.page.add.transaction.AddTransactionRoute
import com.fontaipi.expensetracker.ui.page.add.wallet.AddWalletRoute
import com.fontaipi.expensetracker.ui.page.home.HomeRoute
import com.fontaipi.expensetracker.ui.page.settings.SettingsRoute
import com.fontaipi.expensetracker.ui.page.transactions.TransactionsRoute
import com.fontaipi.expensetracker.ui.page.wallets.WalletsRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTrackerApp() {
    var scaffoldViewState by remember { mutableStateOf(ScaffoldViewState()) }
    val navController = rememberNavController()
    val mainBottomNavController = rememberNavController()
    NavHost(navController = navController, startDestination = "mainNav") {
        composable("mainNav") {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            scaffoldViewState.topAppBarTitle?.let {
                                Text(text = it)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                        ),
                        navigationIcon = Icons.Rounded.Search,
                        navigationIconContentDescription = "search",
                        actionIcon = Icons.Rounded.Settings,
                        actionIconContentDescription = "settings",
                        onNavigationClick = {
                        },
                        onActionClick = {
                            navController.navigate("settings")
                        }
                    )
                },
                floatingActionButton = {
                    scaffoldViewState.onFabClick?.let { action ->
                        FloatingActionButton(
                            onClick = action,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                },
                bottomBar = {
                    BottomAppBar(
                        destinations = TopLevelDestination.entries,
                        onNavigateToDestination = { destination ->
                            mainBottomNavController.navigate(destination.name)
                        },
                        currentDestination = mainBottomNavController
                            .currentBackStackEntryAsState().value?.destination,
                    )
                }
            ) { paddingValues ->
                MainBottomNav(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(paddingValues),
                    navController = mainBottomNavController,
                    updateScaffoldViewState = { scaffoldViewState = it },
                    navigateToAddTransaction = {
                        navController.navigate("addTransaction")
                    },
                    navigateToWallets = {
                        navController.navigate("wallets")
                    },
                    navigateToAnalytics = {
                        mainBottomNavController.navigate(TopLevelDestination.ANALYTICS.name)
                    },
                    navigateToTransactions = {
                        navController.navigate("transactions")
                    },
                )
            }
        }

        animatedComposableVariant("settings") {
            SettingsRoute(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        animatedComposableVariant("transactions") {
            TransactionsRoute(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        slideInVerticallyComposable("addTransaction") {
            AddTransactionRoute(
                onCloseClick = {
                    navController.popBackStack()
                }
            )
        }

        slideInVerticallyComposable("addWallet") {
            AddWalletRoute(
                onCloseClick = {
                    navController.popBackStack()
                }
            )
        }

        animatedComposableVariant("wallets") {
            WalletsRoute(
                navigateToAddWallet = {
                    navController.navigate("addWallet")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun MainBottomNav(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    updateScaffoldViewState: (ScaffoldViewState) -> Unit,
    navigateToAddTransaction: () -> Unit,
    navigateToWallets: () -> Unit,
    navigateToAnalytics: () -> Unit,
    navigateToTransactions: () -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopLevelDestination.HOME.name
    ) {
        composable(TopLevelDestination.HOME.name) {
            HomeRoute(
                updateScaffoldViewState = updateScaffoldViewState,
                navigateToAddTransaction = navigateToAddTransaction,
                navigateToWallets = navigateToWallets,
                navigateToAnalytics = navigateToAnalytics,
                navigateToTransactions = navigateToTransactions,
            )
        }

        composable(TopLevelDestination.ANALYTICS.name) {
            LaunchedEffect(Unit) {
                updateScaffoldViewState(
                    ScaffoldViewState(
                        topAppBarTitle = "Analytics",
                        onFabClick = null
                    )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Not implemented yet", style = MaterialTheme.typography.titleMedium)
            }
        }

        composable(TopLevelDestination.BUDGETS.name) {
            LaunchedEffect(Unit) {
                updateScaffoldViewState(
                    ScaffoldViewState(
                        topAppBarTitle = "Budgets",
                        onFabClick = null
                    )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Not implemented yet", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}