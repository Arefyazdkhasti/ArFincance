package com.example.arfinance.di

import android.content.Context
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
    fun provideNewsDataBse(@ApplicationContext context: Context) = TransactionDataBase.getDatabase(context)

    @Singleton
    @Provides
    fun provideNewsDao(db: TransactionDataBase) = db.transactionDao()


    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope