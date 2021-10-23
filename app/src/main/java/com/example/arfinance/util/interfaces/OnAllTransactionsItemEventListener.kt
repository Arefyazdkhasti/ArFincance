package com.example.arfinance.util.interfaces

import com.example.arfinance.data.dataModel.Transactions

interface OnAllTransactionsItemEventListener {

    fun onDeleteClickListener(transactions: Transactions)

    fun onEditClickListener(transactions: Transactions)
}