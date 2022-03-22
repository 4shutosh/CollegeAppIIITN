package com.college.app.models.network.responses

import com.college.app.models.local.CollegeBook
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IssueBookResponse(
    @Json(name = "bookDetails")
    val bookDetails: CollegeBook
)