package com.fontaipi.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Immutable
import com.fontaipi.expensetracker.data.database.dao.AccountDao
import com.fontaipi.expensetracker.data.database.dao.CategoryDao
import com.fontaipi.expensetracker.data.database.entity.categories
import com.fontaipi.expensetracker.data.database.entity.sampleMainAccount
import com.fontaipi.expensetracker.data.database.entity.sampleSavingAccount
import com.fontaipi.expensetracker.ui.theme.ExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class ScaffoldViewState(
    val topAppBarTitle: String? = null,
    val onFabClick: (() -> Unit)? = null,
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var accountDao: AccountDao

    @Inject
    lateinit var categoryDao: CategoryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            if (categoryDao.countCategories() == 0) {
                accountDao.upsertAccount(sampleMainAccount)
                accountDao.upsertAccount(sampleSavingAccount)
                categoryDao.upsertCategories(categories)
            }
        }
        setContent {
            ExpenseTrackerTheme {
                ExpenseTrackerApp()
            }
        }
    }
}