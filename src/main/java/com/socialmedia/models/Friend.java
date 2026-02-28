package com.socialmedia.models;

import java.sql.Timestamp;

public class Friend {
    private int userId1;
    private int userId2;
    private String status;
    private Timestamp createdAt;

    public Friend() {
    }

    public Friend(int userId1, int userId2, String status, Timestamp createdAt) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getUserId1() {
        return userId1;
    }

    public void setUserId1(int userId1) {
        this.userId1 = userId1;
    }

    public int getUserId2() {
        return userId2;
    }

    public void setUserId2(int userId2) {
        this.userId2 = userId2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
