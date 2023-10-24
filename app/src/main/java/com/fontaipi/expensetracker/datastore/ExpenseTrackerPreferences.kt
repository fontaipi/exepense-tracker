package com.fontaipi.expensetracker.datastore

import androidx.datastore.core.DataStore
import com.fontaipi.expensetracker.model.UserData
import com.fontaipi.expensetracker.proto.UserPreferences
import com.fontaipi.expensetracker.proto.copy
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseTrackerPreferences @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data.map {
        UserData(
            useDynamicColor = it.useDynamicColor,
            useDarkMode = it.useDarkMode,
        )
    }

    suspend fun toggleDynamicColors() {
        userPreferences.updateData {
            it.copy { useDynamicColor = !useDynamicColor }
        }
    }

    suspend fun toggleDarkMode() {
        userPreferences.updateData {
            it.copy { useDarkMode = !useDarkMode }
        }
    }
}