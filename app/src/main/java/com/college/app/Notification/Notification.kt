package com.college.app.Notification

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class Notification(@field:Id var id: Long, var title: String?, var description: String?)