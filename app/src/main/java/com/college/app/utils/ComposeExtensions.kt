package com.college.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.res.ResourcesCompat

fun getImageBitmapFromDrawable(
    context: Context,
    drawableId: Int,
): ImageBitmap? {

    val drawable = ResourcesCompat.getDrawable(
        context.resources,
        drawableId, context.theme
    )
    return if (drawable != null) {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap.asImageBitmap()
    } else null
}