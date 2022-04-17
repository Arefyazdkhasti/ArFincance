package com.example.arfinance.ui.allTransactionList

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.data.local.TransactionDao
import com.example.arfinance.ui.home.HomeViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AllTransactionListViewModel @ViewModelInject constructor(
    private val transactionDao: TransactionDao
) : ViewModel() {

    val transactionsDates = transactionDao.getAllDates().asLiveData()

    val transactions = transactionDao.getAllTransactions().asLiveData()

    private val transactionsEventChannel = Channel<TransactionListEvent>()
    val transactionEvent = transactionsEventChannel.receiveAsFlow()

    fun deleteTransaction(transaction: Transactions) = viewModelScope.launch {
        transactionDao.deleteTransaction(transaction)
    }

    fun deleteAllTransaction() = viewModelScope.launch {
        transactionDao.deleteAllTransaction()
    }

    fun undoDeleteTransaction(transaction: Transactions) = viewModelScope.launch {
        transactionDao.insertTransaction(transaction)
    }

    fun onTransactionSelected(transaction: Transactions) = viewModelScope.launch {
        transactionsEventChannel.send(
            TransactionListEvent.NavigateToEditTransactionScreen(
                transaction
            )
        )

    }

    fun onTransactionLongClick(transaction: Transactions) = viewModelScope.launch {
        transactionsEventChannel.send(
            TransactionListEvent.DeleteTransaction(transaction)
        )
    }

    sealed class TransactionListEvent {
        data class NavigateToEditTransactionScreen(val transactions: Transactions) : TransactionListEvent()
        data class DeleteTransaction(val transaction: Transactions) : TransactionListEvent()
    }
}