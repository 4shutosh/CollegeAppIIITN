package com.college.app.theme

import androidx.compose.ui.graphics.Color

// add material colors here
data class CollegeAppColors(
    val primaryTextColor: Color,
    val secondaryTextColor: Color
)

fun getAppColorScheme(lightTheme: Boolean): CollegeAppColors {
    return if (lightTheme) {
        CollegeAppColors(
            primaryTextColor = Color.Black,
            secondaryTextColor = Grey800
        )
    } else {
        CollegeAppColors(
            primaryTextColor = Color.Black,
            secondaryTextColor = Red700
        )
    }
}