package com.college.app.network.models.requests

data class LoginRequest(
    val email: String,
    val name: String,
    val imageUrl: String?
)