package com.example.studentclubsmanagement.gson;

import java.sql.Timestamp;

/**
 * Created by 李子韬 on 2018/4/5.
 */

public class News {

    private int id;
    private int administratorId;
    private int userId;
    private int clubId;
    private String userName;
    private String clubName;
    private String title;
    private String content;
    private String imagePath;
    private int startNum;
    private Timestamp createdTime;
    private String userHeaderImagePath;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAdministratorId() {
        return administratorId;
    }
    public void setAdministratorId(int administratorId) {
        this.administratorId = administratorId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getClubId() {
        return clubId;
    }
    public void setClubId(int clubId) {
        this.clubId = clubId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public int getStartNum() {
        return startNum;
    }
    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }
    public Timestamp getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
    public String getClubName() {
        return clubName;
    }
    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserHeaderImagePath() {
        return userHeaderImagePath;
    }
    public void setUserHeaderImagePath(String userHeaderImagePath) {
        this.userHeaderImagePath = userHeaderImagePath;
    }
    @Override
    public String toString() {
        return "News [id=" + id + ", administratorId=" + administratorId + ", userId=" + userId + ", clubId=" + clubId
                + ", userName=" + userName + ", clubName=" + clubName + ", title=" + title + ", content=" + content
                + ", imagePath=" + imagePath + ", startNum=" + startNum + ", createdTime=" + createdTime
                + ", userHeaderImagePath=" + userHeaderImagePath + "]";
    }
}
