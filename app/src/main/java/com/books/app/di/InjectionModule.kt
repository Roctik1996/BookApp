package com.books.app.di

import com.books.app.data.repository.FirebaseRepositoryImpl
import com.books.app.domain.repository.FirebaseRepository
import com.books.app.ui.view.main.MainViewModel
import com.google.firebase.FirebaseApp
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val singleModule = module {
    single { FirebaseApp.initializeApp(get()) }
    single { FirebaseConfigManager().initFirebaseRemoteConfig() }
}

val repositoryModule = module {
    single { FirebaseRepositoryImpl(get()) } bind FirebaseRepository::class
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}