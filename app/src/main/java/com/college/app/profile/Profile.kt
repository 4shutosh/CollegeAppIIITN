package com.college.app.profile

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class Profile(
    @field:Id var id: Long,
    var name: String?,
    var email: String?,
    var rollNumber: String,
    var yearStudying: Int,
    var semesterStudying: Int,
    var branch: String,
    var imageBitmapEncoded: String?
)