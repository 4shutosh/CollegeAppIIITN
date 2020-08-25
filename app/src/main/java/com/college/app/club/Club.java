package com.college.app.club;

public class Club {
    String clubName;
    String head;
    String photoUrl;
    String photoUrlLandscape;
    String clubField;
    String about;
    String applicationStatus;
    String applicationLink;
    String communicationChannels;
    String usefulLinks;
    String perks;
    String upComingEvents;

    public String getUpComingEvents() {
        return upComingEvents;
    }

    public void setUpComingEvents(String upComingEvents) {
        this.upComingEvents = upComingEvents;
    }

    public Club(String clubName, String head, String photoUrl) {
        this.clubName = clubName;
        this.head = head;
        this.photoUrl = photoUrl;
    }

    public Club(String clubName, String head, String photoUrl, String
            photoUrlLandscape, String field, String about, String applicationStatus) {
        this.clubName = clubName;
        this.head = head;
        this.photoUrl = photoUrl;
        this.photoUrlLandscape = photoUrlLandscape;
        this.clubField = field;
        this.about = about;
        this.applicationStatus = applicationStatus;
    }

    public String getPhotoUrlLandscape() {
        return photoUrlLandscape;
    }

    public void setPhotoUrlLandscape(String photoUrlLandscape) {
        this.photoUrlLandscape = photoUrlLandscape;
    }

    public String getClubField() {
        return clubField;
    }


    public String getUsefulLinks() {
        return usefulLinks;
    }

    public void setUsefulLinks(String usefulLinks) {
        this.usefulLinks = usefulLinks;
    }

    public String getPerks() {
        return perks;
    }

    public void setPerks(String perks) {
        this.perks = perks;
    }

    public String getApplicationLink() {
        return applicationLink;
    }

    public void setApplicationLink(String applicationLink) {
        this.applicationLink = applicationLink;
    }

    public void setClubField(String clubField) {
        this.clubField = clubField;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getCommunicationChannels() {
        return communicationChannels;
    }

    public void setCommunicationChannels(String communicationChannels) {
        this.communicationChannels = communicationChannels;
    }

    public Club() {
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
