package com.college.app.courses

import com.google.firebase.database.PropertyName
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

@Entity
class Course {
    @Id
    var id: Long = 0
    var courseName: String? = null

    @Unique
    var courseCode: String? = null

    @PropertyName("faculty")
    var faculty: String? = null

    @PropertyName("courseAbout")
    var courseAbout: String? = null

    @PropertyName("photoUrl")
    var photoUrl: String? = null

    @PropertyName("classRoomLink")
    var classroomLink: String? = null

    @PropertyName("videoPlaylistLink")
    var videoPlaylistLink: String? = null

    @PropertyName("driveLink")
    var driveLink: String? = null

    @PropertyName("facultyContact")
    var facultyContact: String? = null
    var your: Boolean = false

    constructor(id: Long, courseName: String?, courseCode: String?, your: Boolean) {
        this.id = id
        this.courseName = courseName
        this.courseCode = courseCode
        this.your = your
    }

    constructor() {}
}