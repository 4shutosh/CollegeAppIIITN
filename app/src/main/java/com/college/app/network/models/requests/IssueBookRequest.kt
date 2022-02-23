package com.college.app.network.models.requests

data class IssueBookRequest(
    val userId: String,
    val libraryBookNumber: Long
)