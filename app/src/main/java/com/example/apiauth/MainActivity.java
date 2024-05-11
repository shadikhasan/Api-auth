package com.example.apiauth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    Button buttonLogin;
    private Button buttonRegister;
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
        buttonRegister = findViewById(R.id.buttonRegister);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        buttonLogin.setOnClickListener(view -> login());

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });
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

    private void showRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Register User");

        View view = getLayoutInflater().inflate(R.layout.dialog_register, null);
        final EditText editTextUsername = view.findViewById(R.id.editTextUsername);
        final EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        final EditText editTextPassword = view.findViewById(R.id.editTextPassword);

        builder.setView(view);

        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get user input from dialog
                String username = editTextUsername.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                // Make a POST request to register the user
                registerUser(username, email, password);

                // Show a toast indicating the registration process
                Toast.makeText(MainActivity.this, "Registering user...", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Method to make a POST request to register the user
    private void registerUser(String username, String email, String password) {
        // Create an instance of the Retrofit service interface
        ApiService apiService = RetrofitClient.getClient();

        // Create a RegisterRequest object with the user's registration information
        // Call API to create a new issue report
        UserRequest userRequest = new UserRequest(username, email, password);

        Call<UserResponse> createIssueReportCall = apiService.registerUser(userRequest);

        createIssueReportCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Toast.makeText(MainActivity.this, "Register successfully, Please Login", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable throwable) {

            }
        });
    }
}
