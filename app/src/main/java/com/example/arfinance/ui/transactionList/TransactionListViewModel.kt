package com.example.arfinance.ui.transactionList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.arfinance.data.local.TransactionDao
import com.example.armovie.utility.lazyDeferred

class TransactionListViewModel(
    private  var transactionDao: TransactionDao
) : ViewModel() {

    val transactions by lazyDeferred {
       // transactionDao.getAllTransactionsAsync()
    }

}