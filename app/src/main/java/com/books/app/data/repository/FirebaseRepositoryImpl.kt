package com.books.app.data.repository

import com.books.app.domain.model.LibraryModel
import com.books.app.domain.repository.FirebaseRepository
import com.books.app.util.Constants
import com.books.app.util.parseJson
import com.google.firebase.remoteconfig.FirebaseRemoteConfig


class FirebaseRepositoryImpl(private val remoteConfig: FirebaseRemoteConfig) : FirebaseRepository {

    override fun fetchLibraryData(): LibraryModel? {
        return parseJson(remoteConfig.getString(Constants.REMOTE_CONFIG_BOOK))
    }
}