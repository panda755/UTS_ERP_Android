package com.example.leadapp.repository;

public interface ResultCallback<T> {
    void onSuccess(T data);

    // code: kode HTTP (0 jika gagal terhubung ke server)
    void onError(int code, String message);
}