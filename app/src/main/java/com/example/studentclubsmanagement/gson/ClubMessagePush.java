package com.example.studentclubsmanagement.gson;

/**
 * Created by 李子韬 on 2018/4/8.
 */

public class ClubMessagePush {

    private int userId;
    private String userName;
    private String userHeaderImagePath;
    private String title;
    private String content;

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

    @Override
    public String toString() {
        return "ClubMessagePush [userId=" + userId + ", userName=" + userName + ", userHeaderImagePath="
                + userHeaderImagePath + ", title=" + title + ", content=" + content + "]";
    }
}
