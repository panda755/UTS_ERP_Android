package com.example.leadapp.network;

import android.content.Context;

import com.example.leadapp.repository.TokenManager;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // GANTI dengan IPv4 laptop (hasil "ipconfig"), tetap diakhiri "/api/"
    public static final String BASE_URL = "http://10.230.72.113:8000/api/";

    private static ApiService service;

    public static synchronized ApiService getService(Context context) {
        if (service == null) {
            final TokenManager tokenManager = new TokenManager(context.getApplicationContext());

            // Otomatis menambah header Accept dan Bearer token di setiap request
            Interceptor interceptor = chain -> {
                Request.Builder builder = chain.request().newBuilder()
                        .header("Accept", "application/json");
                String token = tokenManager.getToken();
                if (token != null) {
                    builder.header("Authorization", "Bearer " + token);
                }
                return chain.proceed(builder.build());
            };

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();

            service = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService.class);
        }
        return service;
    }
}