package com.college.app.data.entities

data class TodoItem(
    override val id: Long,
    val name: String,
    var timeStamp: Long
) : CollegeEntity {

}