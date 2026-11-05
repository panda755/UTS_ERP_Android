package com.example.leadapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.leadapp.model.LoginResponse;
import com.example.leadapp.repository.AuthRepository;
import com.example.leadapp.repository.ResultCallback;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository repository;
    private final MutableLiveData<Resource<LoginResponse>> loginResult = new MutableLiveData<>();
    private final MutableLiveData<Resource<Void>> logoutResult = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
    }

    public LiveData<Resource<LoginResponse>> getLoginResult() {
        return loginResult;
    }

    public LiveData<Resource<Void>> getLogoutResult() {
        return logoutResult;
    }

    public boolean isLoggedIn() {
        return repository.isLoggedIn();
    }

    public void clearSession() {
        repository.clearSession();
    }

    public void login(String email, String password) {
        loginResult.setValue(Resource.<LoginResponse>loading());
        repository.login(email, password, new ResultCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse data) {
                loginResult.setValue(Resource.success(data));
            }

            @Override
            public void onError(int code, String message) {
                loginResult.setValue(Resource.<LoginResponse>error(code, message));
            }
        });
    }

    public void logout() {
        logoutResult.setValue(Resource.<Void>loading());
        repository.logout(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                logoutResult.setValue(Resource.<Void>success(null));
            }

            @Override
            public void onError(int code, String message) {
                logoutResult.setValue(Resource.<Void>error(code, message));
            }
        });
    }
}