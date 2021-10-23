package com.college.app.nav

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CollegeNavigator {

    private val _sharedFlow = MutableSharedFlow<CollegeNavRoute>(extraBufferCapacity = 1)
    val sharedFlow = _sharedFlow.asSharedFlow()

    data class CollegeNavRoute(val route: String)
}