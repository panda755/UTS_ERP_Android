package com.example.leadapp.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("user")
    private User user;
    @SerializedName("token")
    private String token;

    public String getMessage() { return message; }
    public User getUser() { return user; }
    public String getToken() { return token; }
}