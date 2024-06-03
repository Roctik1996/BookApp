package com.books.app.domain.model

import com.google.gson.annotations.SerializedName

data class TopBannerSlide(
    val id: Int,
    @SerializedName("book_id") val bookId: Int,
    val cover: String
)