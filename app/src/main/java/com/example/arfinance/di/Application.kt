package com.example.arfinance.di

import android.app.Application
import com.example.arfinance.data.repository.TransactionRepository
import com.example.arfinance.data.repository.TransactionRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class Application : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@Application))
      //  bind<TransactionDao>() with singleton { TransactionDaoImpl() }
        bind<TransactionRepository>() with singleton { TransactionRepositoryImpl(instance()) }
      //  bind() from provider { HomeViewModelFactory(instance()) }
    }
}