package com.college.app.models.network.requests

data class IssueBookRequest(
    val userId: String,
    val libraryBookNumber: Long
)