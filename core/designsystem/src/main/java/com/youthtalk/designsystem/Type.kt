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

// Set of Material typography styles to start with
val Typography =
    Typography(
        bodyLarge =
            TextStyle(
                fontFamily = preTendFont,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
                color = Color.Black,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = preTendFont,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.5.sp,
                color = Color.Black,
            ),
        bodySmall =
            TextStyle(
                fontFamily = preTendFont,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = preTendFont,
                fontWeight = FontWeight.W400,
                fontSize = 28.sp,
                lineHeight = 44.sp,
                letterSpacing = 0.5.sp,
                color = Color.Black,
            ),
        titleMedium =
            TextStyle(
                fontFamily = preTendFont,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.5.sp,
                color = Color.Black,
            ),
        titleSmall =
            TextStyle(
                fontFamily = preTendFont,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.5.sp,
                color = Color.Black,
            ),
        displayLarge =
            TextStyle(
                fontFamily = preTendFont,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
                color = Color.Black,
            ),
        displayMedium =
            TextStyle(
                fontFamily = preTendFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
                color = Color.Black,
            ),
    )
