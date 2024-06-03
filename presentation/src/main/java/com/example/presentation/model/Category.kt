package com.example.presentation.model

import androidx.annotation.DrawableRes

data class Category(
    @DrawableRes val icon: Int,
    val description: String,
)
