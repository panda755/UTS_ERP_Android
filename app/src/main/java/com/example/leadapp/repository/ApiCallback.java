package com.example.leadapp.repository;

import com.example.leadapp.model.ApiError;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Penghubung Callback Retrofit ke ResultCallback milik aplikasi
class ApiCallback<T> implements Callback<T> {
    private final ResultCallback<T> callback;

    ApiCallback(ResultCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            callback.onSuccess(response.body());
            return;
        }
        String message = "Terjadi kesalahan (kode " + response.code() + ")";
        try {
            if (response.errorBody() != null) {
                ApiError error = new Gson().fromJson(response.errorBody().string(), ApiError.class);
                if (error != null && error.toDisplayMessage() != null) {
                    message = error.toDisplayMessage();
                }
            }
        } catch (Exception ignored) {
            // pakai pesan bawaan
        }
        callback.onError(response.code(), message);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        callback.onError(0, "Tidak dapat terhubung ke server. Periksa IP, Wi-Fi, dan server Laravel.\n("
                + t.getMessage() + ")");
    }
}