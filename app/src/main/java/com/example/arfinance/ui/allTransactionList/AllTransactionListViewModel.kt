package com.example.arfinance.ui.allTransactionList

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.arfinance.data.local.TransactionDao

class AllTransactionListViewModel @ViewModelInject constructor(
    private val transactionDao: TransactionDao
) : ViewModel() {

    val transactionsDates = transactionDao.getAllDates().asLiveData()

    val transactions = transactionDao.getAllTransactions().asLiveData()

}