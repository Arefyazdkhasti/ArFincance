package com.example.arfinance.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.arfinance.data.dataModel.Transactions
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transactions)

    @Delete
    suspend fun deleteTransaction(transaction: Transactions)

    @Update
    suspend fun updateTransaction(transaction: Transactions)

    @Query("SELECT * FROM transaction_table")
    fun getAllTransactions(): Flow<List<Transactions>>

    @Query("SELECT * FROM transaction_table WHERE date = :date")
    fun getTransactionByDate(date: String): Flow<List<Transactions>>

 /*   @Query("SELECT * FROM transaction_table WHERE categoryID = :categoryID")
    fun getTransactionByCategoryAsync(categoryID: Int): Flow<List<Transactions>>
*/
}