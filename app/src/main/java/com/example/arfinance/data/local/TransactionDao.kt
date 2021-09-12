package com.example.arfinance.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.arfinance.data.dataModel.Transactions
import java.util.*

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTransaction(transaction: Transactions)

    @Delete
    fun deleteTransaction(transaction: Transactions)

    @Update
    fun updateTransaction(transaction: Transactions)

    @Query("SELECT * FROM transaction_table")
    fun getAllTransactions(): LiveData<List<Transactions>>

    @Query("SELECT * FROM transaction_table WHERE date = :date")
    fun getTransactionByDate(date: Date): LiveData<List<Transactions>>

    @Query("SELECT * FROM transaction_table WHERE categoryID = :categoryID")
    fun getTransactionByCategory(categoryID: Int): LiveData<List<Transactions>>
}