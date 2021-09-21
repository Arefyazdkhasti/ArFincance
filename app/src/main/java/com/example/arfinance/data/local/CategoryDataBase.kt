package com.example.arfinance.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Category
import com.example.arfinance.di.ApplicationScope
import com.example.arfinance.util.enumerian.CategoryType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Category::class], version = 1, exportSchema = false)
abstract class CategoryDataBase(
) : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao



    companion object {
        @Volatile
        private var instance: CategoryDataBase? = null

        fun getDatabase(context: Context): CategoryDataBase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, CategoryDataBase::class.java, "category")
                .fallbackToDestructiveMigration()
                //.addCallback(getCallback)
                .build()


    }
}
