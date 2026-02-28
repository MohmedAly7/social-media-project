package com.socialmedia.models;

import java.sql.Timestamp;

public class Profile {
    private int userId;
    private String bio;
    private String pictureUrl;
    private Timestamp updatedAt;

    public Profile() {
    }

    public Profile(int userId, String bio, String pictureUrl, Timestamp updatedAt) {
        this.userId = userId;
        this.bio = bio;
        this.pictureUrl = pictureUrl;
        this.updatedAt = updatedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
