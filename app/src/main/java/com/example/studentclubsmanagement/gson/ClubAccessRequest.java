package com.example.studentclubsmanagement.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 李子韬 on 2018/4/14.
 */

public class ClubAccessRequest {

    private int userId;
    private int clubId;
    private String clubName;
    private String title;
    private String content;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("user_header_image_path")
    private String userImageHeader;
    
    public String getUserImageHeader() {
        return userImageHeader;
    }
    public void setUserImageHeader(String userImageHeader) {
        this.userImageHeader = userImageHeader;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
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

    @Override
    public String toString() {
        return "ClubAccessRequest [userId=" + userId + ", clubId=" + clubId + ", clubName=" + clubName + ", title="
                + title + ", content=" + content + ", userName=" + userName + ", userImageHeader=" + userImageHeader
                + "]";
    }
}
