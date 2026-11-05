package com.example.leadapp.repository;

import android.content.Context;

import com.example.leadapp.model.LoginRequest;
import com.example.leadapp.model.LoginResponse;
import com.example.leadapp.model.MessageResponse;
import com.example.leadapp.network.ApiClient;
import com.example.leadapp.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final ApiService api;
    private final TokenManager tokenManager;

    public AuthRepository(Context context) {
        Context app = context.getApplicationContext();
        this.tokenManager = new TokenManager(app);
        this.api = ApiClient.getService(app);
    }

    public boolean isLoggedIn() {
        return tokenManager.hasToken();
    }

    public void clearSession() {
        tokenManager.clear();
    }

    public void login(String email, String password, final ResultCallback<LoginResponse> callback) {
        api.login(new LoginRequest(email, password)).enqueue(
                new ApiCallback<LoginResponse>(new ResultCallback<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse data) {
                        if (data == null || data.getToken() == null) {
                            callback.onError(0, "Respons login tidak valid.");
                            return;
                        }
                        tokenManager.saveSession(data.getToken(), data.getUser());
                        callback.onSuccess(data);
                    }

                    @Override
                    public void onError(int code, String message) {
                        callback.onError(code, message);
                    }
                }));
    }

    // Token lokal selalu dihapus, apa pun hasil respons server
    public void logout(final ResultCallback<Void> callback) {
        api.logout().enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                tokenManager.clear();
                callback.onSuccess(null);
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                tokenManager.clear();
                callback.onSuccess(null);
            }
        });
    }
}