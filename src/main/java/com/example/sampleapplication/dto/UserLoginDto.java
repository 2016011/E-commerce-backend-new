package com.example.sampleapplication.dto;

public class UserLoginDto {
    private String userName;
    private String password;

    public UserLoginDto() {
    }

    public UserLoginDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
