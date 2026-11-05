package com.example.leadapp.repository;

import android.content.Context;

import com.example.leadapp.model.Lead;
import com.example.leadapp.network.ApiClient;
import com.example.leadapp.network.ApiService;

import java.util.List;

public class LeadRepository {
    private final ApiService api;

    public LeadRepository(Context context) {
        this.api = ApiClient.getService(context.getApplicationContext());
    }

    public void getLeads(ResultCallback<List<Lead>> callback) {
        api.getLeads().enqueue(new ApiCallback<>(callback));
    }

    public void createLead(Lead lead, ResultCallback<Lead> callback) {
        api.createLead(lead).enqueue(new ApiCallback<>(callback));
    }
}