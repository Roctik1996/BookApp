package com.books.app.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> parseJson(jsonString: String?): T? {
    return if (jsonString.isNullOrEmpty()) {
        null
    } else {
        val gson = Gson()
        val type = object : TypeToken<T>() {}.type
        gson.fromJson<T>(jsonString, type)
    }
}