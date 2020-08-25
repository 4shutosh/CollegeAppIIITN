package com.college.app.courses;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Unique;

@Entity
public class Course {

    @Id
    long id;

    String courseName;

    @Unique
    String courseCode;
    String faculty;
    String courseAbout;
    String photoUrl;
    String classroomLink, videoPlaylistLink, driveLink;
    String facultyContact;
    Boolean your;

    public Course(long id, String courseName, String courseCode, Boolean your) {
        this.id = id;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.your = your;
    }

    public Course() {
    }


    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourseAbout() {
        return courseAbout;
    }

    public void setCourseAbout(String courseAbout) {
        this.courseAbout = courseAbout;
    }

    public String getClassroomLink() {
        return classroomLink;
    }

    public void setClassroomLink(String classroomLink) {
        this.classroomLink = classroomLink;
    }

    public String getVideoPlaylistLink() {
        return videoPlaylistLink;
    }

    public void setVideoPlaylistLink(String videoPlaylistLink) {
        this.videoPlaylistLink = videoPlaylistLink;
    }

    public String getDriveLink() {
        return driveLink;
    }

    public void setDriveLink(String driveLink) {
        this.driveLink = driveLink;
    }

    public String getFacultyContact() {
        return facultyContact;
    }

    public void setFacultyContact(String facultyContact) {
        this.facultyContact = facultyContact;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Boolean getYour() {
        return your;
    }

    public void setYour(Boolean your) {
        this.your = your;
    }
}
