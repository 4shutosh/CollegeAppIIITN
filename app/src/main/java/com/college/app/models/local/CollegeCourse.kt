package com.college.app.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.college.app.data.entities.CollegeEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "courses"
)
@JsonClass(generateAdapter = true)
data class CollegeCourse(
    @Json(name = "id") val courseId: String,
    @Json(name = "name") val name: String,
    @Json(name = "code") val code: String,
    @Json(name = "description") val description: String,
    @Json(name = "facultyName") val facultyName: String,
    var userEnrolled: Boolean = false,
    @Json(ignore = true) @PrimaryKey(autoGenerate = true) override val id: Long = 0L,
) : CollegeEntity