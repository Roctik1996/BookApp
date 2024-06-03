package com.books.app.domain.model

sealed class BookState {
    data object Loading : BookState()
    data class Success(val libraryModel: LibraryModel) : BookState()
    data class Error(val exception: Exception) : BookState()
}