package com.socialmedia.models;

import java.sql.Timestamp;

public class Like {
    private int postId;
    private int userId;
    private Timestamp createdAt;

    public Like() {
    }

    public Like(int postId, int userId, Timestamp createdAt) {
        this.postId = postId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
