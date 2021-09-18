package com.example.arfinance.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.arfinance.data.dataModel.Category
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.data.dataModel.TransactionsHelper
import com.example.arfinance.util.enumerian.TransactionType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.collections.HashMap

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transactions)

    @Delete
    suspend fun deleteTransaction(transaction: Transactions)

    @Update
    suspend fun updateTransaction(transaction: Transactions)

    @Query("SELECT * FROM transaction_table ORDER BY id DESC")
    fun getAllTransactions(): Flow<List<Transactions>>

    @Query("SELECT * FROM transaction_table WHERE date = :date  ORDER BY id DESC")
    fun getTransactionByDate(date: String): Flow<List<Transactions>>

    @Query("SELECT SUM(amount) FROM transaction_table WHERE type = :type AND  date BETWEEN  :startDate AND :endDate")
    fun getTransactionsSumAmountByDate(type: TransactionType, startDate: String, endDate: String): Flow<Long>

    @Query("SELECT * FROM transaction_table WHERE  type = :type AND  date BETWEEN  :startDate AND :endDate")
    fun getTransactionsByDateRange(type: TransactionType, startDate: String, endDate: String): Flow<List<Transactions>>

    @Query("SELECT SUM(amount) as amount, categoryName FROM transaction_table WHERE  type = :type AND date BETWEEN  :startDate AND :endDate GROUP BY categoryName")
    fun getTransactionsByCategoryAndDateRange(type: TransactionType, startDate: String, endDate: String): Flow<List<TransactionsHelper>>
}