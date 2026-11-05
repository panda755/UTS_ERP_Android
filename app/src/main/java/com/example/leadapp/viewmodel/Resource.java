package com.example.leadapp.viewmodel;

// Pembungkus status data: LOADING / SUCCESS / ERROR
public class Resource<T> {
    public enum Status { LOADING, SUCCESS, ERROR }

    private final Status status;
    private final T data;
    private final String message;
    private final int code;
    private boolean handled = false;

    private Resource(Status status, T data, String message, int code) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null, 0);
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null, 200);
    }

    public static <T> Resource<T> error(int code, String message) {
        return new Resource<>(Status.ERROR, null, message, code);
    }

    public Status getStatus() { return status; }
    public T getData() { return data; }
    public String getMessage() { return message; }
    public int getCode() { return code; }

    // true hanya sekali, agar Toast/navigasi tidak terulang saat layar diputar
    public boolean consume() {
        if (handled) return false;
        handled = true;
        return true;
    }
}