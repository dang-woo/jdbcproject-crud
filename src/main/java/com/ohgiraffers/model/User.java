package com.ohgiraffers.model;

import java.time.LocalDateTime;

/*
 * 사용자 객체를 정의하는 모델
 * */
public class User {
    private int userId;
    private String userName;
    private String email;
    private String password;
    private int roleId;
    private LocalDateTime createdAt;


    public User(int userId, String userName, String email, String password, int roleId, LocalDateTime createdAt) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.createdAt = createdAt;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return
                "아이디" + userId +
                ", 이름'" + userName + '\'' +
                ", 이메일'" + email + '\'' +
                ", 비밀번호'" + password + '\'' +
                ", roleId=" + roleId +
                ", 생성일=" + createdAt;
    }
}