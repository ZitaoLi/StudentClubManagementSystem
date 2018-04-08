package com.example.studentclubsmanagement.gson;

import java.sql.Timestamp;

/**
 * Created by 李子韬 on 2018/4/8.
 */

public class ClubInternalTransaction {

    private int id;
    private int memberId;
    private int clubId;
    private int transacitonType;
    private String body;
    private Timestamp createdTime;

    public Timestamp getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getMemberId() {
        return memberId;
    }
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
    public int getClubId() {
        return clubId;
    }
    public void setClubId(int clubId) {
        this.clubId = clubId;
    }
    public int getTransacitonType() {
        return transacitonType;
    }
    public void setTransacitonType(int transacitonType) {
        this.transacitonType = transacitonType;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    @Override
    public String toString() {
        return "ClubInternalTransaction [id=" + id + ", memberId=" + memberId + ", clubId=" + clubId
                + ", transacitonType=" + transacitonType + ", body=" + body + ", createdTime=" + createdTime + "]";
    }
}
