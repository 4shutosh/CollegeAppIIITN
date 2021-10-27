package com.college.app.utils

import com.college.app.utils.CollegeBuildVariantType.*

object BuildVariantTypeMapper {

    const val collegeDebug = "college" + "debug"
    const val communityDebug = "community" + "debug"

    const val collegeRelease = "college" + "release"
    const val communityRelease = "community" + "release"
}

fun getBuildVariantType(buildName: String) = when (buildName) {
    BuildVariantTypeMapper.collegeDebug -> CollegeDebug
    BuildVariantTypeMapper.communityDebug -> CommunityDebug
    BuildVariantTypeMapper.collegeRelease -> CollegeRelease
    BuildVariantTypeMapper.communityRelease -> CommunityRelease
    else -> throw Exception("Invalid Build Type Found")
}

sealed class CollegeBuildVariantType {
    object CollegeDebug : CollegeBuildVariantType()
    object CommunityDebug : CollegeBuildVariantType()

    object CollegeRelease : CollegeBuildVariantType()
    object CommunityRelease : CollegeBuildVariantType()
}

fun CollegeBuildVariantType.isCommunityType(): Boolean {
    return this == CommunityRelease || this == CommunityDebug
}