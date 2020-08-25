package com.college.app.profile;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Profile {

    @Id
    long id;

    String name;
    String email;
    String rollNumber;
    Integer yearStudying;
    Integer semesterStudying;
    String branch;
    String imageBitmapEncoded;

    public Profile(long id, String name, String email, String rollNumber,
                   Integer yearStudying, Integer semesterStudying, String branch, String imageBitmapEncoded) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.rollNumber = rollNumber;
        this.yearStudying = yearStudying;
        this.semesterStudying = semesterStudying;
        this.branch = branch;
        this.imageBitmapEncoded = imageBitmapEncoded;
    }

    public String getImageBitmapEncoded() {
        return imageBitmapEncoded;
    }

    public void setImageBitmapEncoded(String imageBitmapEncoded) {
        this.imageBitmapEncoded = imageBitmapEncoded;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public Integer getYearStudying() {
        return yearStudying;
    }

    public void setYearStudying(Integer yearStudying) {
        this.yearStudying = yearStudying;
    }

    public Integer getSemesterStudying() {
        return semesterStudying;
    }

    public void setSemesterStudying(Integer semesterStudying) {
        this.semesterStudying = semesterStudying;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
