package com.example.arfinance.ui.transactionList

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.data.local.TransactionDao
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class TransactionListViewModel @ViewModelInject constructor(
    private val transactionDao: TransactionDao
) : ViewModel() {

    val transaction = transactionDao.getAllTransactionsAsync().asLiveData()

    fun OnAddNewTransactionClicked() = viewModelScope.launch {

    }


    sealed class TransactionEvent {
        object NavigateToAddTransactionScreen : TransactionEvent()
        data class NavigateToEditTransactionScreen(val transactions: Transactions) :
            TransactionEvent()

    }
}