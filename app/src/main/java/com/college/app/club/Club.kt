package com.college.app.club

class Club {
    var clubName: String? = null
    var head: String? = null
    var photoUrl: String? = null
    var photoUrlLandscape: String? = null
    var clubField: String? = null
    var about: String? = null
    var applicationStatus: String? = null
    var applicationLink: String? = null
    var communicationChannels: String? = null
    var usefulLinks: String? = null
    var perks: String? = null
    var upComingEvents: String? = null

    constructor(clubName: String?, head: String?, photoUrl: String?) {
        this.clubName = clubName
        this.head = head
        this.photoUrl = photoUrl
    }

    constructor(
        clubName: String?,
        head: String?,
        photoUrl: String?,
        photoUrlLandscape: String?,
        field: String?,
        about: String?,
        applicationStatus: String?
    ) {
        this.clubName = clubName
        this.head = head
        this.photoUrl = photoUrl
        this.photoUrlLandscape = photoUrlLandscape
        clubField = field
        this.about = about
        this.applicationStatus = applicationStatus
    }

    constructor() {}
}