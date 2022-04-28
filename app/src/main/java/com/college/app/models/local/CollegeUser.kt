package com.college.app.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.college.app.data.entities.CollegeEntity

@Entity
data class CollegeUser(
    @PrimaryKey
    val userId: String,
    val email: String,
    val name: String, override val id: Long = 0,
) : CollegeEntity