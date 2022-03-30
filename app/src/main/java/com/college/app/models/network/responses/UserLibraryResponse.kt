package com.college.app.models.network.responses

import com.college.app.models.local.CollegeBook
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class UserLibraryItemResponse(
    @Json(name = "book") val book: CollegeBook,
    @Json(name = "issueTimeStamp") val issueTimeStamp: Long,
    @Json(name = "returnTimeStamp") val returnTimeStamp: Long,
    @Json(name = "penalty") val penalty: Long,
)

@JsonClass(generateAdapter = true)
data class UserLibraryResponse(
    @Json(name = "id") val id: String,
    @Json(name = "userBookDataList") val userBookList: MutableList<UserLibraryItemResponse>,
    @Json(name = "totalPenalty") val totalPenalty: Int,
)