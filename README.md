# LeadApp - Klien Android untuk ERP (REST API + MVVM)

Aplikasi Android (Java) yang terhubung ke REST API proyek **UTS_ERP** (Laravel 13 + Filament 5) untuk autentikasi dan pengelolaan data prospek pelanggan (*Lead*). Aplikasi dibangun dengan arsitektur **MVVM** yang dipisahkan secara bersih: Model (POJO, Retrofit, Repository), ViewModel (LiveData), dan View (Activity + ListView).

| | |
|---|---|
| Penulis | Galih Eka Syaputra |
| Mata kuliah | `[isi nama mata kuliah]` |
| Backend | [panda755/UTS_ERP](https://github.com/panda755/UTS_ERP) |

## Tampilan Aplikasi

Alur yang diperlihatkan: login, melihat daftar lead, menambah lead, lalu logout.

<table>
  <tr>
    <td align="center" valign="top">
      <img src="https://github.com/user-attachments/assets/4f6bb339-2c75-4c39-a44d-bab01c081563" alt="Halaman login" width="230"><br>
      <sub><b>1.</b> Halaman login</sub>
    </td>
    <td align="center" valign="top">
      <img src="https://github.com/user-attachments/assets/7e32eaba-4493-411f-a84c-aa77c4828b05" alt="Halaman login terisi" width="230"><br>
      <sub><b>2.</b> Login terisi</sub>
    </td>
    <!-- SCREENSHOT 3 (daftar lead + pesan berhasil login) belum terunggah.
         Setelah URL-nya ada, hapus tanda komentar ini dan ganti GANTI_URL_SCREENSHOT_3.
    <td align="center" valign="top">
      <img src="GANTI_URL_SCREENSHOT_3" alt="Daftar lead setelah login" width="230"><br>
      <sub><b>3.</b> Daftar lead (berhasil login)</sub>
    </td>
    -->
  </tr>
  <tr>
    <td align="center" valign="top">
      <img src="https://github.com/user-attachments/assets/0b79d5aa-003c-484a-8477-1a4d3f645178" alt="Form tambah lead terisi" width="230"><br>
      <sub><b>4.</b> Form tambah lead terisi</sub>
    </td>
    <td align="center" valign="top">
      <img src="https://github.com/user-attachments/assets/ebfc8ec5-3ed1-49ea-9829-73ff421d2c1a" alt="Daftar lead setelah menambah data" width="230"><br>
      <sub><b>5.</b> Daftar lead (berhasil ditambahkan)</sub>
    </td>
    <td align="center" valign="top">
      <img src="https://github.com/user-attachments/assets/e8a69f51-08df-49a7-ab54-395ee0999f27" alt="Halaman login setelah logout" width="230"><br>
      <sub><b>6.</b> Login (berhasil logout)</sub>
    </td>
  </tr>
</table>

## Fitur

- **Login** dengan email dan password; server menerbitkan token Sanctum yang disimpan di `SharedPreferences`, sehingga aplikasi langsung masuk ke daftar lead jika token masih ada.
- **GET**: menampilkan daftar lead pada `ListView` (nama, perusahaan, status, tanggal terakhir dihubungi).
- **POST**: menambah lead baru lewat dialog form dengan validasi nama wajib diisi.
- **Logout**: token dicabut di server dan dihapus dari perangkat.
- **Penanganan error**: pesan validasi server (422) ditampilkan, sesi berakhir (401) mengembalikan pengguna ke halaman login, dan kegagalan jaringan menampilkan pesan yang jelas.
- **Aman terhadap rotasi layar**: data tetap tampil setelah layar diputar tanpa memanggil API ulang, karena disimpan di ViewModel.

## Arsitektur MVVM

```
View (Activity)  -->  ViewModel (LiveData)  -->  Repository  -->  ApiService (Retrofit)  -->  REST API Laravel
   observe()            MutableLiveData           ApiCallback        @GET / @POST
```

| Lapisan | Kelas | Tanggung jawab |
|---|---|---|
| **Model** | `Lead`, `User`, `LoginRequest`, `LoginResponse`, `MessageResponse`, `ApiError` | POJO untuk data yang dikirim dan diterima (dipetakan dengan `@SerializedName`) |
| | `ApiService`, `ApiClient` | Interface Retrofit dan pembuatan klien (interceptor memasang `Accept` dan `Authorization: Bearer`) |
| | `AuthRepository`, `LeadRepository`, `TokenManager` | Repository Pattern: satu-satunya penghubung ViewModel ke jaringan dan penyimpanan token |
| **ViewModel** | `AuthViewModel`, `LeadViewModel`, `Resource` | Menyimpan state dengan `MutableLiveData`, hanya mengekspos `LiveData` ke View |
| **View** | `LoginActivity`, `MainActivity`, `LeadAdapter` | Meng-`observe` ViewModel dan menampilkan data pada `ListView`; tidak menyentuh jaringan secara langsung |

### Cuplikan: API Interface (Retrofit)

```java
public interface ApiService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("logout")
    Call<MessageResponse> logout();

    @GET("leads")
    Call<List<Lead>> getLeads();

    @POST("leads")
    Call<Lead> createLead(@Body Lead lead);
}
```

## Endpoint yang Digunakan

| Method | Endpoint | Autentikasi | Fungsi |
|---|---|---|---|
| POST | `/api/login` | tidak | Login, mengembalikan `token` |
| POST | `/api/logout` | Bearer token | Mencabut token |
| GET | `/api/leads` | Bearer token | Mengambil daftar lead |
| POST | `/api/leads` | Bearer token | Menambah lead baru |

## Teknologi

- Java 8, `minSdk` 24, `compileSdk` 34
- Android Gradle Plugin 8.1.4, Gradle 8.0 (Android Studio Giraffe)
- Retrofit 2.9.0 + Gson converter
- AndroidX Lifecycle (ViewModel dan LiveData) 2.5.1
- AppCompat 1.6.1, Material Components 1.11.0, ConstraintLayout 2.1.4

Versi AndroidX sengaja disesuaikan dengan AGP 8.1.4 dan `compileSdk` 34. Versi bawaan template yang lebih baru memerlukan `compileSdk` 35 dan AGP 8.6 ke atas.

## Cara Menjalankan

### 1. Jalankan backend (UTS_ERP)

```bash
git clone https://github.com/panda755/UTS_ERP.git
cd UTS_ERP
composer install
copy .env.example .env
php artisan key:generate
php artisan migrate
php artisan db:seed
php artisan serve --host=0.0.0.0 --port=8000
```

Akun contoh dari seeder: `admin@example.com` / `password123`.

### 2. Jalankan aplikasi Android

1. Buka proyek ini di Android Studio, lalu tunggu Gradle Sync selesai.
2. Ubah `BASE_URL` di `app/src/main/java/com/example/leadapp/network/ApiClient.java` menjadi IPv4 komputer yang menjalankan Laravel, diakhiri `/api/`:
   ```java
   public static final String BASE_URL = "http://192.168.x.x:8000/api/";
   ```
3. Hubungkan HP (USB debugging aktif) atau gunakan emulator, lalu klik **Run**.

### Catatan jaringan (HP fisik)

- HP dan komputer server harus berada pada jaringan yang sama (misalnya hotspot HP).
- Izinkan port 8000 pada firewall Windows.
- Uji dulu dari browser HP: buka `http://IP-SERVER:8000/up`. Jika halaman muncul, jalur jaringan sudah benar.
- Emulator Android mengakses server di komputer yang sama lewat `http://10.0.2.2:8000/api/`.
- Aplikasi memakai HTTP biasa (`usesCleartextTraffic`) untuk keperluan pengembangan lokal.

## Struktur Proyek

```
app/src/main/java/com/example/leadapp/
├── LoginActivity.java
├── MainActivity.java
├── LeadAdapter.java
├── model/        Lead, User, LoginRequest, LoginResponse, MessageResponse, ApiError
├── network/      ApiService, ApiClient
├── repository/   AuthRepository, LeadRepository, TokenManager, ApiCallback, ResultCallback
└── viewmodel/    AuthViewModel, LeadViewModel, Resource
```
