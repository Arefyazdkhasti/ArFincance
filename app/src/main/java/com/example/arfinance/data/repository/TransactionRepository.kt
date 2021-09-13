package com.example.arfinance.data.repository


import androidx.lifecycle.LiveData
import com.example.arfinance.data.dataModel.Transactions
import java.util.*

interface TransactionRepository {

    suspend fun insertTransaction(transaction: Transactions)

    suspend fun deleteTransaction(transaction: Transactions)

    suspend fun updateTransaction(transaction: Transactions)

    suspend fun getTransactionByDate(date: String): LiveData<List<Transactions>>
}
