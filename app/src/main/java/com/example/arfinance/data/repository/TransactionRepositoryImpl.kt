package com.example.arfinance.data.repository

import androidx.lifecycle.LiveData
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.data.local.TransactionDao
import java.util.*

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {

   override suspend fun insertTransaction(transaction: Transactions) {
        transactionDao.insertTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transactions) {
        transactionDao.deleteTransaction(transaction)

    }

    override suspend fun updateTransaction(transaction: Transactions) {
        transactionDao.updateTransaction(transaction)

    }

    override suspend fun getTransactionByDate(date: Date): LiveData<List<Transactions>> = transactionDao.getTransactionByDate(date)

}