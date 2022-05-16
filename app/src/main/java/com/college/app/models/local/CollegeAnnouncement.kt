package com.college.app.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.college.app.data.entities.CollegeEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "announcements"
)
@JsonClass(generateAdapter = true)
data class CollegeAnnouncement(
    override val id: Long = 0L,
    @PrimaryKey(autoGenerate = false)
    @Json(name = "announcementId") val dbId: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val message: String,
    @Json(name = "link") val link: String? = null,
    @Json(name = "timeStampExpiry") val timeStampExpiry: Long,
    @Json(name = "timeStampCreation") val timeStampCreation: Long,
) : CollegeEntity