package com.example.arfinance.data.repository

import androidx.lifecycle.LiveData
import com.example.arfinance.data.datamodel.Transaction
import java.util.*

interface TransactionRepository {

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun getTransactionByDate(date: Date): LiveData<List<Transaction>>
}