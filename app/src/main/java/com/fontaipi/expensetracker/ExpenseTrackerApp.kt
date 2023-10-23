package com.fontaipi.expensetracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fontaipi.expensetracker.ui.component.BottomAppBar
import com.fontaipi.expensetracker.ui.component.TopAppBar
import com.fontaipi.expensetracker.ui.component.animatedComposable
import com.fontaipi.expensetracker.ui.component.animatedComposableVariant
import com.fontaipi.expensetracker.ui.component.slideInVerticallyComposable
import com.fontaipi.expensetracker.ui.page.home.AddTransactionRoute
import com.fontaipi.expensetracker.ui.page.home.HomeRoute
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
                            Column {
                                Text(
                                    text = "Total balance",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.outline
                                )
                                Text(text = "4 307, 00€")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                        ),
                        navigationIcon = Icons.Default.Person,
                        navigationIconContentDescription = "Menu",
                        actionIcon = Icons.Default.Search,
                        actionIconContentDescription = "Add",
                        onNavigationClick = {
                        },
                        onActionClick = {
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
                    }
                )
            }
        }
        slideInVerticallyComposable("addTransaction") {
            AddTransactionRoute(
                onCloseClick = {
                    navController.popBackStack()
                }
            )
        }

        animatedComposableVariant("wallets") {
            WalletsRoute(
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
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopLevelDestination.HOME.name
    ) {
        animatedComposable(TopLevelDestination.HOME.name) {
            HomeRoute(
                updateScaffoldViewState = updateScaffoldViewState,
                navigateToAddTransaction = navigateToAddTransaction,
                navigateToWallets = navigateToWallets
            )
        }

        composable(TopLevelDestination.ANALYTICS.name) {
            Column {
                Text("Analytics")
            }
        }

        composable(TopLevelDestination.BUDGETS.name) {
            Column {
                Text("Budgets")
            }
        }

        composable(TopLevelDestination.SETTINGS.name) {
            Column {
                Text("Settings")
            }
        }
    }
}