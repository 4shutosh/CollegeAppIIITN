package com.college.app.Notification;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Notification {

    @Id
    long id;

    String title, description;

    public Notification(long id, String title, String description) {
        this.id = id;
        this.description = description;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
