package com.college.app.models.local

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollegeBarcodeData(
    @Json(name = "identifier") val identifier: String,
    @Json(name = "data") val data: String,
)