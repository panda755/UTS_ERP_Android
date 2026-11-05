package com.example.leadapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.leadapp.model.Lead;
import com.example.leadapp.repository.LeadRepository;
import com.example.leadapp.repository.ResultCallback;

import java.util.List;

public class LeadViewModel extends AndroidViewModel {
    private final LeadRepository repository;
    private final MutableLiveData<Resource<List<Lead>>> leads = new MutableLiveData<>();
    private final MutableLiveData<Resource<Lead>> createResult = new MutableLiveData<>();

    public LeadViewModel(@NonNull Application application) {
        super(application);
        repository = new LeadRepository(application);
    }

    public LiveData<Resource<List<Lead>>> getLeads() {
        return leads;
    }

    public LiveData<Resource<Lead>> getCreateResult() {
        return createResult;
    }

    public void loadLeads() {
        leads.setValue(Resource.<List<Lead>>loading());
        repository.getLeads(new ResultCallback<List<Lead>>() {
            @Override
            public void onSuccess(List<Lead> data) {
                leads.setValue(Resource.success(data));
            }

            @Override
            public void onError(int code, String message) {
                leads.setValue(Resource.<List<Lead>>error(code, message));
            }
        });
    }

    public void createLead(Lead lead) {
        createResult.setValue(Resource.<Lead>loading());
        repository.createLead(lead, new ResultCallback<Lead>() {
            @Override
            public void onSuccess(Lead data) {
                createResult.setValue(Resource.success(data));
                loadLeads(); // muat ulang daftar setelah berhasil menambah
            }

            @Override
            public void onError(int code, String message) {
                createResult.setValue(Resource.<Lead>error(code, message));
            }
        });
    }
}