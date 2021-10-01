package com.college.app.todo


//@Entity
class Todo {
//    @Id
    var id: Long
    var title: String?
    var description: String?
    var completed: Boolean
    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var min = 0
    var reminderStatus: Boolean

    constructor(
        id: Long,
        title: String?,
        description: String?,
        status: Boolean,
        reminderStatus: Boolean
    ) {
        this.id = id
        this.title = title
        this.description = description
        completed = status
        this.reminderStatus = reminderStatus
    }

    constructor(
        id: Long,
        title: String?,
        description: String?,
        isCompleted: Boolean,
        reminderStatus: Boolean,
        day: Int,
        month: Int,
        year: Int,
        hour: Int,
        min: Int
    ) {
        this.id = id
        this.title = title
        this.description = description
        completed = isCompleted
        this.reminderStatus = reminderStatus
        this.day = day
        this.month = month
        this.year = year
        this.hour = hour
        this.min = min
    }
}