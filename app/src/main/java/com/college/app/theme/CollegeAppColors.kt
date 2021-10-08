package com.college.app.theme

import androidx.compose.ui.graphics.Color

// add material colors here
data class CollegeAppColors(
    val primaryTextColor: Color,
    val secondaryTextColor: Color
)

fun getAppColorScheme(darkTheme: Boolean): CollegeAppColors {
    return if (darkTheme) {
        CollegeAppColors(
            primaryTextColor = PurpleLighter,
            secondaryTextColor = Grey800
        )
    } else {
        CollegeAppColors(
            primaryTextColor = PurpleDarker,
            secondaryTextColor = Red700
        )
    }
}