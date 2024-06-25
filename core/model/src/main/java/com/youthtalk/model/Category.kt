package com.youthtalk.model

import androidx.annotation.DrawableRes

data class Category(
    @DrawableRes val icon: Int,
    val description: String,
)
