package com.college.app.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.college.app.R

private val OpenSans = FontFamily(
    Font(R.font.font_open_sans_regular),
    Font(R.font.font_open_sans_semi_bold, FontWeight.SemiBold),
    Font(R.font.font_open_sans_bold, FontWeight.Bold)
)

val CollegeAppTypography = Typography(
    defaultFontFamily = OpenSans
)