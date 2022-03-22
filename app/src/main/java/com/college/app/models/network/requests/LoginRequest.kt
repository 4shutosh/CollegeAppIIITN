package com.college.app.models.network.requests

data class LoginRequest(
    val email: String,
    val name: String,
    val imageUrl: String?
)