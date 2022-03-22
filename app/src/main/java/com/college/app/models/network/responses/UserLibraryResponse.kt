package com.college.app.models.network.responses

import com.college.app.models.local.CollegeBook
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class UserLibraryItemResponse(
    @Json(name = "book") val book: CollegeBook,
    @Json(name = "issueTimeStamp") val issueTimeStamp: Long,
    @Json(name = "returnTimeStamp") val returnTimeStamp: Long,
)