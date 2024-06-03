package com.books.app

import androidx.multidex.MultiDexApplication
import com.books.app.di.repositoryModule
import com.books.app.di.singleModule
import com.books.app.di.viewModelModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BookApp: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@BookApp)
            modules(
                singleModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}