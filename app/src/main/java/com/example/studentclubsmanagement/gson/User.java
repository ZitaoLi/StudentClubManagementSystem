package com.example.studentclubsmanagement.gson;

/**
 * Created by 李子韬 on 2018/4/3.
 */

import java.sql.Timestamp;

public class User {

    private int id;
    private String studentCode;
    private String password;
    private String email;
    private String phoneNumber;
    private String name;
    private String selfIntroduction;
    private String headerImagePath;
    private Timestamp createdTime;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getStudentCode() {
        return studentCode;
    }
    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSelfIntroduction() {
        return selfIntroduction;
    }
    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }
    public String getHeaderImagePath() {
        return headerImagePath;
    }
    public void setHeaderImagePath(String headerImagePath) {
        this.headerImagePath = headerImagePath;
    }
    public Timestamp getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", studentCode=" + studentCode + ", password=" + password + ", email=" + email
                + ", phoneNumber=" + phoneNumber + ", name=" + name + ", selfIntroduction=" + selfIntroduction
                + ", headerImagePath=" + headerImagePath + ", createdTime=" + createdTime + "]";
    }
}
