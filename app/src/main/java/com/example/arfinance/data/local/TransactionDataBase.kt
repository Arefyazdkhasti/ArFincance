package com.example.arfinance.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Transactions::class], version = 2,exportSchema = false)
abstract class TransactionDataBase: RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

   /* class Callback @Inject constructor(
        private val database: Provider<TransactionDataBase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().transactionDao()

            applicationScope.launch {
                dao.insertTransaction(Transactions(0,"r","r",1,"r","r","r"))
                dao.insertTransaction(Transactions(0,"re","re",1,"re","re","re"))
            }
        }
    }*/
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
