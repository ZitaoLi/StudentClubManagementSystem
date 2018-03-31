package com.example.studentclubsmanagement.gson;

import java.sql.Timestamp;

/**
 * Created by 李子韬 on 2018/3/30.
 */

public class Club {

    private int id;
    private String clubName;
    private String clubInfo;
    private String clubBgImagePath;
    private Timestamp createdTime;
    private int lifeTime;
    private int memberNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubInfo() {
        return clubInfo;
    }

    public void setClubInfo(String clubInfo) {
        this.clubInfo = clubInfo;
    }

    public String getClubBgImagePath() {
        return clubBgImagePath;
    }

    public void setClubBgImagePath(String clubBgImagePath) {
        this.clubBgImagePath = clubBgImagePath;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    @Override
    public String toString() {
        return "Club{" +
                "id=" + id +
                ", clubName='" + clubName + '\'' +
                ", clubInfo='" + clubInfo + '\'' +
                ", clubBgImagePath='" + clubBgImagePath + '\'' +
                ", createdTime=" + createdTime +
                ", lifeTime=" + lifeTime +
                ", memberNum=" + memberNum +
                '}';
    }
}
