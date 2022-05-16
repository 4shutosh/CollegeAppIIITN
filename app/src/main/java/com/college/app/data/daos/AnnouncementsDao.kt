package com.college.app.data.daos

import androidx.room.Dao
import androidx.room.Query
import com.college.app.models.local.CollegeAnnouncement
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AnnouncementsDao : EntityDao<CollegeAnnouncement>() {

    @Query("SELECT * FROM announcements ORDER BY timeStampCreation DESC")
    abstract fun getAllAnnouncements(): Flow<List<CollegeAnnouncement>>

    @Query("SELECT * FROM announcements WHERE dbId = :announcementId")
    abstract fun getAnnouncementById(announcementId: String): CollegeAnnouncement?

    suspend fun insertOrUpdateAnnouncement(announcement: CollegeAnnouncement) {
        val announcementFromDb = getAnnouncementById(announcement.dbId)
        if (announcementFromDb == null) {
            insert(announcement)
        } else {
            update(announcementFromDb.copy(
                title = announcement.title,
                message = announcement.message,
                link = announcement.link,
                timeStampCreation = announcement.timeStampCreation,
                timeStampExpiry = announcement.timeStampExpiry)
            )
        }
    }
}