package com.college.app.data.entities

import androidx.room.Entity


@Entity(
    tableName = "todos"
)
data class TodoItem(
    override val id: Long,
    val name: String,
    var timeStamp: Long,
    var isCompleted: Boolean = false
) : CollegeEntity {

}