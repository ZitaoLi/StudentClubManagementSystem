package com.example.studentclubsmanagement.gson;

import java.io.Serializable;

/**
 * Created by 李子韬 on 2018/4/9.
 */

public class ClubCreationRequest implements Serializable {

    private static final long serialVersionUID=1L;
    private int userId;
    private String clubName;
    private String title;
    private String content;
    private String imagePath;
    private String userName;
    private String userHeaderImagePath;

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
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
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
    public void setContent(String content) {
        this.content = content;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "ClubCreationRequest [userId=" + userId + ", clubName=" + clubName + ", title=" + title + ", content="
                + content + ", imagePath=" + imagePath + ", userName=" + userName + ", userHeaderImagePath="
                + userHeaderImagePath + "]";
    }
}
