package com.college.app.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "todos"
)
data class TodoItem(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "timeStamp") var timeStampMilliSeconds: Long,
    @ColumnInfo(name = "isCompleted") var isCompleted: Boolean = false,
    @ColumnInfo(name = "notify") var notify: Boolean = false,
    @PrimaryKey(autoGenerate = true) override val id: Long = 0
) : CollegeEntity {

}