package com.college.app.theme

import androidx.compose.ui.graphics.Color

// add material colors here
data class CollegeAppColors(
    val iconColorSelected: Color,
    val iconColorUnSelected: Color,
    val primaryTextColor: Color,
    val secondaryTextColor: Color
)

fun getAppColorScheme(darkTheme: Boolean): CollegeAppColors {
    return if (darkTheme) {
        CollegeAppColors(
            iconColorSelected = Color.White,
            iconColorUnSelected = Color.Gray,
            primaryTextColor = PurpleLighter,
            secondaryTextColor = Grey800
        )
    } else {
        CollegeAppColors(
            iconColorSelected = PurpleDarker,
            iconColorUnSelected = Color.LightGray,
            primaryTextColor = PurpleDarker,
            secondaryTextColor = Red700
        )
    }
}