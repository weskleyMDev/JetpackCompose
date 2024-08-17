package com.weskley.cards

import androidx.annotation.DrawableRes

data class MyData(
    @DrawableRes val thumbnail: Int,
    val videoTitle: String,
    val channel: String
)
