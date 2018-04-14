package com.example.studentclubsmanagement.gson;

import java.sql.Timestamp;

/**
 * Created by 李子韬 on 2018/4/14.
 */

public class Notice {

    private int id;
    private int userId;
    private int noticeType;
    private boolean read;
    private String body;
    private Timestamp createdTime;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getNoticeType() {
        return noticeType;
    }
    public void setNoticeType(int noticeType) {
        this.noticeType = noticeType;
    }
    public boolean isRead() {
        return read;
    }
    public void setRead(boolean read) {
        this.read = read;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public Timestamp getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
    @Override
    public String toString() {
        return "Notice [id=" + id + ", userId=" + userId + ", noticeType=" + noticeType + ", read=" + read + ", body="
                + body + ", createdTime=" + createdTime + "]";
    }
}
