package com.college.base.domain

class ServerException(val errCode: Int? = 0, val msg: String? = "") : Exception()
