package com.example.leadapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ApiError {
    @SerializedName("message")
    private String message;
    @SerializedName("errors")
    private Map<String, List<String>> errors;

    public String toDisplayMessage() {
        if (errors != null && !errors.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (List<String> messages : errors.values()) {
                if (messages != null && !messages.isEmpty()) {
                    if (sb.length() > 0) sb.append("\n");
                    sb.append(messages.get(0));
                }
            }
            if (sb.length() > 0) return sb.toString();
        }
        return message;
    }
}