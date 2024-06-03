package com.books.app.domain.repository

import com.books.app.domain.model.LibraryModel

interface FirebaseRepository {
    fun fetchLibraryData(): LibraryModel?
}