package com.college.app.todo;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Todo {

    @Id
    long id;

    String title, description;
    Boolean isCompleted;
    int year, month, day;
    int hour, min;
    Boolean reminderStatus;


    public Todo(long id, String title, String description, boolean status, boolean reminderStatus) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isCompleted = status;
        this.reminderStatus = reminderStatus;
    }

    public Todo(long id, String title, String description, Boolean isCompleted, Boolean reminderStatus, int day, int month, int year, int hour, int min) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
        this.reminderStatus = reminderStatus;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.min = min;

    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public Boolean getReminderStatus() {
        return reminderStatus;
    }

    public void setReminderStatus(Boolean reminderStatus) {
        this.reminderStatus = reminderStatus;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
