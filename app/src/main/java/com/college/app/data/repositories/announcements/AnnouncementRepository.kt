package com.college.app.data.repositories.announcements

import com.college.app.data.daos.AnnouncementsDao
import com.college.app.models.local.CollegeAnnouncement
import com.college.app.network.CollegeAppService
import com.college.base.domain.isSuccess
import com.college.base.domain.serverException
import com.college.base.logger.CollegeLogger
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

// this is for caching notifications
interface AnnouncementRepository {

    fun getAllAnnouncements(): Flow<List<CollegeAnnouncement>>
    suspend fun updateLocalAnnouncements()

    suspend fun insertNotification(collegeAnnouncement: CollegeAnnouncement): Long
}

class AnnouncementRepositoryImpl @Inject constructor(
    private val announcementsDao: AnnouncementsDao,
    private val collegeAppService: CollegeAppService,
    private val logger: CollegeLogger,
) : AnnouncementRepository {
    override fun getAllAnnouncements(): Flow<List<CollegeAnnouncement>> =
        announcementsDao.getAllAnnouncements()

    override suspend fun updateLocalAnnouncements() {
        try {
            val networkAnnouncements = collegeAppService.getAllAnnouncements()
            if (networkAnnouncements.isSuccess()) {
                networkAnnouncements.data.forEach {
                    announcementsDao.insertOrUpdateAnnouncement(it)
                }
            } else {
                logger.d("network response failed: ${networkAnnouncements.serverException()}")
            }
        } catch (e: Exception) {
            logger.d("something wrong: ${e.message}")
        }
    }

    override suspend fun insertNotification(collegeAnnouncement: CollegeAnnouncement): Long {
        return announcementsDao.insert(collegeAnnouncement)
    }
}