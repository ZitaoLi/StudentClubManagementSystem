package com.example.studentclubsmanagement.gson;

import java.sql.Timestamp;

/**
 * Created by 李子韬 on 2018/4/8.
 */

public class ClubManagementTransaction {

    private int id;
    private int userId;
    private int transactionType;
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
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    @Override
    public String toString() {
        return "ClubManagementTransaction [id=" + id + ", userId=" + userId + ", transactionType=" + transactionType
                + ", body=" + body + ", createdTime=" + createdTime + "]";
    }
}
