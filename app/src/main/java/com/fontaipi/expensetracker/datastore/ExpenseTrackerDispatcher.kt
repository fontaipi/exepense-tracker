package com.fontaipi.expensetracker.datastore

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val expenseTrackerDispatcher: ExpenseTrackerDispatchers)

enum class ExpenseTrackerDispatchers {
    Default,
    IO,
}