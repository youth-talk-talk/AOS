package com.youthtalk.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.youth.app.core.designsystem.R

val preTendFont =
    FontFamily(
        Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
        Font(R.font.pretendard_bold, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.pretendard_semibold, FontWeight.SemiBold, FontStyle.Normal),
    )

val Typography =
    Typography(
        // P/20/SemiBold
        bodyLarge =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W600,
            fontSize = 20.sp,
            color = Color.Black,
        ),
        // P/18/SemiBold
        bodyMedium =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W600,
            fontSize = 18.sp,
            color = Color.Black,
        ),
        // P/18/Medium
        bodySmall =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W500,
            fontSize = 18.sp,
            color = Color.Black,
        ),
        // P/16/SemiBold
        titleLarge =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W600,
            fontSize = 16.sp,
            color = Color.Black,
        ),
        // P/16/Medium
        titleMedium =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            color = Color.Black,
        ),
        // P/16/Regular
        titleSmall =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            color = Color.Black,
        ),
        // P/14/Semibold
        displayLarge = TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W600,
            fontSize = 14.sp,
            color = Color.Black,
        ),
        // P/14/Medium
        displayMedium =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            color = Color.Black,
        ),
        // P/14/Regular
        displaySmall = TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            color = Color.Black,
        ),
        // P/12/Medium
        labelMedium = TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            color = Color.Black,
        ),
        // P/12/Regular
        labelSmall = TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            color = Color.Black,
        ),
        // P/10/SemiBold
        headlineLarge = TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W600,
            fontSize = 10.sp,
            color = Color.Black,
        ),
        // P/10/Medium
        headlineMedium = TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W500,
            fontSize = 10.sp,
            color = Color.Black,
        ),
    )
