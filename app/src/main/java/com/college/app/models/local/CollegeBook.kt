package com.college.app.models.local

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.sql.Timestamp

@JsonClass(generateAdapter = true)
data class CollegeBook(
    @Json(name = "bookId") val bookId: String,
    @Json(name = "bookName") val bookName: String,
    @Json(name = "libraryBookNumber") val libraryBookNumber: Long,
    @Json(name = "maximumDaysAllowed") val maxDaysAllowed: Int,
    @Json(name = "isAvailableToIssue") val isAvailableToIssue: Boolean,
    @Json(name = "ownerData") val ownerData: CollegeBookOwnerData? = null,
)

@JsonClass(generateAdapter = true)
data class CollegeBookOwnerData(
    @Json(name = "userId") val userId: String,
    @Json(name = "email") val email: String,
    @Json(name = "returnTimeStamp") val returnTimestamp: Long,
)