package com.example.leadapp.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.leadapp.model.User;

public class TokenManager {
    private static final String PREFS = "lead_prefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_EMAIL = "user_email";

    private final SharedPreferences prefs;

    public TokenManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void saveSession(String token, User user) {
        SharedPreferences.Editor editor = prefs.edit().putString(KEY_TOKEN, token);
        if (user != null) {
            editor.putString(KEY_NAME, user.getName());
            editor.putString(KEY_EMAIL, user.getEmail());
        }
        editor.apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public boolean hasToken() {
        return getToken() != null;
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}