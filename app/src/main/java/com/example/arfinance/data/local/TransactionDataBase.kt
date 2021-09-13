package com.example.arfinance.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.arfinance.data.dataModel.Transactions

@Database(entities = [Transactions::class], version = 1,exportSchema = false)
abstract class TransactionDataBase: RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var instance: TransactionDataBase? = null

        fun getDatabase(context: Context): TransactionDataBase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, TransactionDataBase::class.java, "transaction")
                .fallbackToDestructiveMigration()
                .build()

    }

}
