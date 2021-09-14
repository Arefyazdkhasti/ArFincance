package com.example.arfinance.di

import android.content.Context
import com.example.arfinance.data.local.CategoryDao
import com.example.arfinance.data.local.CategoryDataBase
import com.example.arfinance.data.local.TransactionDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.util.*
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTransactionDataBse(@ApplicationContext context: Context) =
        TransactionDataBase.getDatabase(context)

    @Singleton
    @Provides
    fun provideTransactionDao(db: TransactionDataBase) = db.transactionDao()

    @Singleton
    @Provides
    fun provideCategoryDataBse(@ApplicationContext context: Context) =
        CategoryDataBase.getDatabase(context)

    @Singleton
    @Provides
    fun provideCategoryDao(db: CategoryDataBase) = db.categoryDao()




    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope