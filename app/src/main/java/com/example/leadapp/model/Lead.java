package com.example.leadapp.model;

import com.google.gson.annotations.SerializedName;

public class Lead {
    // Integer (bukan int) agar tidak ikut terkirim saat POST
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("company_name")
    private String companyName;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("source")
    private String source;
    @SerializedName("status")
    private String status;
    @SerializedName("last_contacted_at")
    private String lastContactedAt;
    @SerializedName("notes")
    private String notes;

    // Konstruktor untuk POST; field bernilai null tidak dikirim oleh Gson
    public Lead(String name, String companyName, String email, String phone,
                String status, String notes) {
        this.name = name;
        this.companyName = companyName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.notes = notes;
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getCompanyName() { return companyName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getSource() { return source; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }

    // Server mengirim "2026-10-20T00:00:00.000000Z"; ambil tanggalnya saja
    public String getLastContactedDate() {
        if (lastContactedAt == null || lastContactedAt.length() < 10) return "-";
        return lastContactedAt.substring(0, 10);
    }
}