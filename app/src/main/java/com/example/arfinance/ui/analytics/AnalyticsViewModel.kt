package com.example.arfinance.ui.analytics

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.example.arfinance.data.local.TransactionDao
import com.example.arfinance.util.enumerian.TransactionType
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

class AnalyticsViewModel @ViewModelInject constructor(
    private val transactionDao: TransactionDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    //income
    val incomeStartDate = state.getLiveData("incomeStartDate", "")
    val incomeEndDate = state.getLiveData("incomeEndDate", "")


    private val incomeListDateRangeFlow = combine(
        incomeStartDate.asFlow(),
        incomeEndDate.asFlow()
    )
    { start, end ->
        Pair(start, end)
    }.flatMapLatest { (startDate, endDate) ->
        transactionDao.getTransactionsByDateRange(TransactionType.Income ,startDate, endDate)
    }
    val incomeListDateRange = incomeListDateRangeFlow.asLiveData()


    //expenses
    val expenseStartDate = state.getLiveData("expenseStartDate", "")
    val expenseEndDate = state.getLiveData("expenseEndDate", "")


    private val expenseListDateRangeFlow = combine(
        expenseStartDate.asFlow(),
        expenseEndDate.asFlow()
    )
    { start, end ->
        Pair(start, end)
    }.flatMapLatest { (startDate, endDate) ->
        transactionDao.getTransactionsByDateRange(TransactionType.Expense,startDate, endDate)
    }
    val expenseListDateRange = expenseListDateRangeFlow.asLiveData()

    //bat chart
    val barChartStartDate = state.getLiveData("barChartStartDate", "")
    val barChartEndDate = state.getLiveData("barChartEndDate", "")


    private val  barCharListDateRangeFlow = combine(
        barChartStartDate.asFlow(),
        barChartEndDate.asFlow()
    )
    { start, end ->
        Pair(start, end)
    }.flatMapLatest { (startDate, endDate) ->
        transactionDao.getTransactionsByCategoryAndDateRange(TransactionType.Expense,startDate, endDate)
    }
    val batChartListDateRange = barCharListDateRangeFlow.asLiveData()
}