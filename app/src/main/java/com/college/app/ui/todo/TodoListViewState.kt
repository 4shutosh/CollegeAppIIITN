package com.college.app.ui.todo

data class TodoListViewState(
    var id : Long,
    val title: String,
    val description: String?,
    var timeLeft: String,
    var timeLeftUnit: String,
    var time: String,
    var date: String,
    var isNotifyOn: Boolean,
    var isExpanded : Boolean = false
)