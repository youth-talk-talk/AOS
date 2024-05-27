package com.example.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.presentation.R

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
            ),
        bodyMedium =
            TextStyle(
                fontFamily = preTendFont,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.5.sp,
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
                fontWeight = FontWeight.Normal,
                fontSize = 36.sp,
                lineHeight = 44.sp,
                letterSpacing = 0.5.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = preTendFont,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.5.sp,
            ),
        titleSmall =
            TextStyle(
                fontFamily = preTendFont,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.5.sp,
            ),
        displayLarge =
            TextStyle(
                fontFamily = preTendFont,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        displayMedium =
            TextStyle(
                fontFamily = preTendFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
    )
