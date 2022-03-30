package com.college.app.models.network.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IssueBookRequest(
    @Json(name = "userId") val userId: String,
    @Json(name = "libraryBookNumber") val libraryBookNumber: Long,
)