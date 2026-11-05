package com.example.leadapp.network;

import com.example.leadapp.model.Lead;
import com.example.leadapp.model.LoginRequest;
import com.example.leadapp.model.LoginResponse;
import com.example.leadapp.model.MessageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("logout")
    Call<MessageResponse> logout();

    @GET("leads")
    Call<List<Lead>> getLeads();

    @POST("leads")
    Call<Lead> createLead(@Body Lead lead);
}