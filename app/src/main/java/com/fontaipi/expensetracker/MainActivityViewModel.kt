package com.fontaipi.expensetracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fontaipi.expensetracker.datastore.ExpenseTrackerPreferences
import com.fontaipi.expensetracker.model.UserData
import com.fontaipi.expensetracker.ui.page.settings.UserPreferencesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val preferences: ExpenseTrackerPreferences,
) : ViewModel() {

    val userPreferencesState = preferences.userData.map<UserData, UserPreferencesState> {
        UserPreferencesState.Success(it)
    }.onStart {
        emit(UserPreferencesState.Loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = UserPreferencesState.Loading
    )
}