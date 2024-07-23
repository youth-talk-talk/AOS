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

val gMarketFont =
    FontFamily(
        Font(R.font.gmarket_sans_bold, FontWeight.Bold, FontStyle.Normal),
    )

val Typography =
    Typography(
        // P/24/Bold
        bodyLarge =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W700,
            fontSize = 24.sp,
            lineHeight = 24.sp,
            color = Color.Black,
        ),
        // P/18/Bold
        bodyMedium =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W700,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            color = Color.Black,
        ),
        // P/16/Bold
        bodySmall =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W700,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
        ),
        // P/16/SemiBold
        titleLarge =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W600,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = Color.Black,
        ),
        // P/16/Regular - line-height 24
        titleMedium =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = Color.Black,
        ),
        // P/16/Regular - line-height 16
        titleSmall =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
            color = Color.Black,
        ),

        // P/14/Bold
        displayLarge =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W700,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            color = Color.Black,
        ),
        // P/12/Bold
        displayMedium =
        TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W700,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Color.Black,
        ),
        // P/12/Regular
        displaySmall = TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W600,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Color.Black,
        ),
        // P/10/Regular
        labelLarge = TextStyle(
            fontFamily = preTendFont,
            fontWeight = FontWeight.W400,
            fontSize = 10.sp,
            lineHeight = 16.sp,
            color = Color.Black,
        ),
        // G/20/Bold
        labelMedium = TextStyle(
            fontFamily = gMarketFont,
            fontWeight = FontWeight.W500,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            color = Color.Black,
        ),
        // G/14/Bold
        headlineSmall = TextStyle(
            fontFamily = gMarketFont,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            color = Color.Black,
        ),

    )
