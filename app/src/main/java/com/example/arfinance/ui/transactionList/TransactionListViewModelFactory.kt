package com.example.arfinance.ui.transactionList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arfinance.data.local.TransactionDao

class TransactionListViewModelFactory(
    private val transactionDao: TransactionDao
):ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TransactionListViewModel(
            transactionDao
        ) as T
    }

}