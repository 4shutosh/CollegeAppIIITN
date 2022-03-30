package com.college.app.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.college.app.R

private val OpenSans = FontFamily(
    Font(R.font.google_sans_regular),
    Font(R.font.google_sans_bold, FontWeight.Bold),
    Font(R.font.google_sans_medium, FontWeight.Medium)
)

val CollegeAppTypography = Typography(
    defaultFontFamily = OpenSans
)