package com.books.app.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    inline fun launch(
        noinline error: ((Throwable) -> Unit)? = null,
        noinline finally: (() -> Unit)? = null,
        crossinline success: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                success()
            } catch (e: Exception) {
                error?.invoke(e)

            } finally {
                finally?.invoke()
            }
        }
    }
}