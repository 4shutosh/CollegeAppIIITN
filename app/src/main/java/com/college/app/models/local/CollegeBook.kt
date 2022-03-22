package com.college.app.models.local

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollegeBook(
    @Json(name = "bookId") val bookId: String,
    @Json(name = "bookName") val bookName: String,
    @Json(name = "libraryBookNumber") val libraryBookNumber: Long,
    @Json(name = "maximumDaysAllowed") val maxDaysAllowed: Int,
    @Json(name = "isAvailableToIssue") val isAvailableToIssue: Boolean,
    @Json(name = "ownerUserId") val ownerUserId: String? = null,
)