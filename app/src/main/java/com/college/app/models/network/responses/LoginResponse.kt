package com.college.app.models.network.responses

import com.squareup.moshi.Json

data class LoginResponse(

    @Json(name = "userId")
    val userId: Long,

    @Json(name = "accessToken")
    val accessToken: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "name")
    val name: String?,

    @Json(name = "imageUrl")
    val imageUrl: String?
)