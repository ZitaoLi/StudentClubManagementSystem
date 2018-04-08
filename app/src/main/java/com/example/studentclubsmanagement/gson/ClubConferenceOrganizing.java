package com.example.studentclubsmanagement.gson;

/**
 * Created by 李子韬 on 2018/4/8.
 */

public class ClubConferenceOrganizing {

    private int userId;
    private int clubId;
    private String title;
    private String content;
    private String time;
    private String place;
    private String userName;
    private String userHeaderImagePath;

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
        return "ClubConferenceOrganizing [userId=" + userId + ", clubId=" + clubId + ", title=" + title + ", content="
                + content + ", time=" + time + ", place=" + place + ", userName=" + userName + ", userHeaderImagePath="
                + userHeaderImagePath + "]";
    }
}
