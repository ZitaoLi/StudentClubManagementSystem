package com.example.studentclubsmanagement.gson;

import java.io.Serializable;

/**
 * Created by 李子韬 on 2018/4/9.
 */

public class ClubActivityRequest implements Serializable {

    private static final long serialVersionUID=1L;
    private int userId;
    private String userName;
    private String userHeaderImagePath;
    private int clubId;
    private String clubName;
    private String title;
    private String content;
    private String time;
    private String place;
    private String imagePath;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
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

    public void setContent(String conten) {
        this.content = conten;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "ClubActivityRequest [userId=" + userId + ", userName=" + userName + ", userHeaderImagePath="
                + userHeaderImagePath + ", clubId=" + clubId + ", clubName=" + clubName + ", title=" + title
                + ", content=" + content + ", time=" + time + ", place=" + place + ", imagePath=" + imagePath + "]";
    }
}
