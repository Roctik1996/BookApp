package com.books.app.domain.model

import com.google.gson.annotations.SerializedName

data class LibraryModel(
    val books: List<Book>,
    @SerializedName("top_banner_slides") val topBannerSlides: List<TopBannerSlide>,
    @SerializedName("you_will_like_section") val youWillLikeSection: List<Int>
)