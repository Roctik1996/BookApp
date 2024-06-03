package com.books.app.ui.view.main

import com.books.app.domain.model.Book
import com.books.app.domain.model.BookState
import com.books.app.domain.repository.FirebaseRepository
import com.books.app.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val firebaseRepository: FirebaseRepository) : BaseViewModel() {

    private val _booksStateFlow = MutableStateFlow<BookState>(BookState.Loading)
    val booksStateFlow: StateFlow<BookState> = _booksStateFlow

    private val _selectedBookFlow = MutableStateFlow<Book?>(null)
    val selectedBookFlow: StateFlow<Book?> = _selectedBookFlow

    init {
        fetchLibrary()
    }

    private fun fetchLibrary() {
        launch {
            try {
                _booksStateFlow.value = BookState.Loading
                val bookModel = firebaseRepository.fetchLibraryData()
                if (bookModel != null) {
                    _booksStateFlow.value = BookState.Success(bookModel)
                } else {
                    _booksStateFlow.value = BookState.Error(Exception("Failed to load books"))
                }
            } catch (e: Exception) {
                _booksStateFlow.value = BookState.Error(e)
            }
        }
    }

    fun selectBook(bookId: Book) {
        _selectedBookFlow.value = bookId
    }
}
