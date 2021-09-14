package com.example.arfinance.ui.transactionList

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.data.local.TransactionDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TransactionListViewModel @ViewModelInject constructor(
    private val transactionDao: TransactionDao
) : ViewModel() {

    val transaction = transactionDao.getAllTransactions().asLiveData()

    private val transactionsEventChannel = Channel<TransactionListEvent>()
    val transactionEvent = transactionsEventChannel.receiveAsFlow()

    fun addNewTransactionClicked() = viewModelScope.launch {
        transactionsEventChannel.send(TransactionListEvent.NavigateToAddTransactionScreen)
    }

    sealed class TransactionListEvent {
        object NavigateToAddTransactionScreen : TransactionListEvent()
        data class NavigateToEditTransactionScreen(val transactions: Transactions) :
            TransactionListEvent()

    }
}