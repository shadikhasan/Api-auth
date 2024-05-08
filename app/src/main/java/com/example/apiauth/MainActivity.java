package com.example.apiauth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    Button buttonLogin;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if user is already logged in
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("AuthToken")) {
            // User is already logged in, redirect to MainActivity2
            startActivity(new Intent(MainActivity.this, MainActivity2.class));
            finish();
            return; // Skip rest of onCreate method
        }

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        buttonLogin.setOnClickListener(view -> login());
    }

    private void login() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        ApiService apiService = RetrofitClient.getClient();

        // Call login API
        Call<LoginResponse> loginCall = apiService.login(new LoginRequest(username, password));
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String authToken = "Bearer " + response.body().getToken().getAccess();
                    // Save token in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("AuthToken", authToken);
                    editor.apply();
                    // Redirect to MainActivity2
                    startActivity(new Intent(MainActivity.this, MainActivity2.class));
                    finish();
                } else {
                    Log.e("Login API Call", "Failed to login");
                    Toast.makeText(MainActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, Throwable t) {
                Log.e("Login API Call", "Failed to login", t);
                Toast.makeText(MainActivity.this, "Login failed. Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
