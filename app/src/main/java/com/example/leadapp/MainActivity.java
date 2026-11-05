package com.example.leadapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.leadapp.model.Lead;
import com.example.leadapp.viewmodel.AuthViewModel;
import com.example.leadapp.viewmodel.LeadViewModel;
import com.example.leadapp.viewmodel.Resource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String[] STATUSES = {"baru", "dihubungi", "negosiasi", "deal", "batal"};

    private AuthViewModel authViewModel;
    private LeadViewModel leadViewModel;
    private LeadAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        if (!authViewModel.isLoggedIn()) {
            openLogin();
            return;
        }

        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.listViewLeads);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        Button btnLogout = findViewById(R.id.btnLogout);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        adapter = new LeadAdapter(this);
        listView.setAdapter(adapter);

        leadViewModel = new ViewModelProvider(this).get(LeadViewModel.class);
        observeLeads();
        observeCreate();
        observeLogout();

        fabAdd.setOnClickListener(v -> showAddDialog());
        btnLogout.setOnClickListener(v -> authViewModel.logout());

        // Hanya muat saat pertama kali; setelah rotasi data diambil dari ViewModel
        if (savedInstanceState == null) {
            leadViewModel.loadLeads();
        }
    }

    private void observeLeads() {
        leadViewModel.getLeads().observe(this, resource -> {
            if (resource == null) return;
            switch (resource.getStatus()) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS: {
                    progressBar.setVisibility(View.GONE);
                    List<Lead> data = resource.getData();
                    adapter.setData(data);
                    tvEmpty.setVisibility(data == null || data.isEmpty() ? View.VISIBLE : View.GONE);
                    break;
                }
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    if (resource.consume()) {
                        handleError(resource.getCode(), resource.getMessage());
                    }
                    break;
            }
        });
    }

    private void observeCreate() {
        leadViewModel.getCreateResult().observe(this, resource -> {
            if (resource == null) return;
            if (resource.getStatus() == Resource.Status.SUCCESS && resource.consume()) {
                Toast.makeText(this, "Lead berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            } else if (resource.getStatus() == Resource.Status.ERROR && resource.consume()) {
                handleError(resource.getCode(), resource.getMessage());
            }
        });
    }

    private void observeLogout() {
        authViewModel.getLogoutResult().observe(this, resource -> {
            if (resource == null) return;
            if (resource.getStatus() == Resource.Status.SUCCESS && resource.consume()) {
                Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show();
                openLogin();
            }
        });
    }

    private void handleError(int code, String message) {
        if (code == 401) {
            authViewModel.clearSession();
            Toast.makeText(this, "Sesi berakhir, silakan login ulang.", Toast.LENGTH_LONG).show();
            openLogin();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_lead, null);
        EditText etName = view.findViewById(R.id.etName);
        EditText etCompany = view.findViewById(R.id.etCompany);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPhone = view.findViewById(R.id.etPhone);
        EditText etNotes = view.findViewById(R.id.etNotes);
        Spinner spStatus = view.findViewById(R.id.spStatus);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, STATUSES);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(statusAdapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Tambah Lead Baru")
                .setView(view)
                .setPositiveButton("Simpan", null)
                .setNegativeButton("Batal", null)
                .create();

        // Listener dipasang setelah show() agar dialog tidak menutup saat validasi gagal
        dialog.setOnShowListener(d ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                    String name = etName.getText().toString().trim();
                    if (name.isEmpty()) {
                        etName.setError("Nama wajib diisi");
                        return;
                    }
                    Lead lead = new Lead(
                            name,
                            emptyToNull(etCompany.getText().toString()),
                            emptyToNull(etEmail.getText().toString()),
                            emptyToNull(etPhone.getText().toString()),
                            (String) spStatus.getSelectedItem(),
                            emptyToNull(etNotes.getText().toString()));
                    leadViewModel.createLead(lead);
                    dialog.dismiss();
                }));
        dialog.show();
    }

    private String emptyToNull(String value) {
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}