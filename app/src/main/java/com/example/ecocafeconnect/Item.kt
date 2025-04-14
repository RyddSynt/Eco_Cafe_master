package com.example.ecocafeconnect

import androidx.annotation.DrawableRes

data class Item(
    val title: String,
    @DrawableRes val image: Int,
    val textTitle: String
)
