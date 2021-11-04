package com.college.app.network.models.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoogleUserModel(
    @Json(name = "name")
    val name: String?,

    @Json(name = "email")
    val email: String?
)