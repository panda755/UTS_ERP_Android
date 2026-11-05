package com.example.leadapp.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")
    private final String email;
    @SerializedName("password")
    private final String password;
    @SerializedName("device_name")
    private final String deviceName;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
        this.deviceName = "android";
    }
}