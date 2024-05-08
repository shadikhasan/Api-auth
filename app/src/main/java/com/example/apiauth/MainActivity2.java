package com.example.apiauth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {

    TextView textViewUsers;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        textViewUsers = findViewById(R.id.textViewUsers);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Retrieve access token from SharedPreferences
        String authToken = sharedPreferences.getString("AuthToken", null);

        ApiService apiService = RetrofitClient.getClient();

        // Call API to get Users data
        Call<List<User>> usersCall = apiService.getData(authToken);
        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Display users data
                    List<User> users = response.body();
                    StringBuilder rolesString = new StringBuilder();
                    for (User user : users) {
                        rolesString.append("RoleID: ").append(user.getRole()).append("\nName: ").append(user.getFirst_name()).append("\nDescription: ").append(user.getLast_name()).append("\n\n");
                    }
                    textViewUsers.setText(rolesString.toString());
                } else {
                    textViewUsers.setText("Failed to fetch users data.");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                textViewUsers.setText("Failed to fetch users data. Please check your internet connection.");
            }
        });

    }

    private void showAddUserDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_user, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextUsername = dialogView.findViewById(R.id.editTextUsername);
        final EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
        final EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);

        dialogBuilder.setTitle("Add User");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Handle adding user here
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                // Validate input and create user
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    // Call method to create user
                    createUser(username, email, password);
                } else {
                    Toast.makeText(MainActivity2.this, "Please enter username, email, and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void createUser(String username, String email, String password) {

        ApiService apiService = RetrofitClient.getClient();

        // Retrieve access token from SharedPreferences
        String authToken = sharedPreferences.getString("AuthToken", null);

        // Create a new user request object
        CreateUserRequest createUserRequest = new CreateUserRequest(username, email, password);

        // Call API to create a new user
        Call<CreateUserResponse> createUserCall = apiService.createUser(authToken, createUserRequest);
        createUserCall.enqueue(new Callback<CreateUserResponse>() {
            @Override
            public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // User created successfully
                    Toast.makeText(MainActivity2.this, "User created successfully", Toast.LENGTH_SHORT).show();
                    // You may update the UI or perform any other action here
                } else {
                    // Error handling
                    Toast.makeText(MainActivity2.this, "Failed to create user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateUserResponse> call, Throwable t) {
                // Failure handling
                Toast.makeText(MainActivity2.this, "Failed to create user", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //menu section
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            // Handle About action
            //startActivity(new Intent(MainActivity2.this, AboutActivity.class));
            Toast.makeText(MainActivity2.this, "About menu selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_add_user) {
            // Handle About action
            showAddUserDialog();
            Toast.makeText(MainActivity2.this, "About menu selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.action_logout) {
            // Remove token from SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("AuthToken");
            editor.apply();

            // Redirect to MainActivity (or LoginActivity)
            startActivity(new Intent(MainActivity2.this, MainActivity.class));
            finish();
            Toast.makeText(MainActivity2.this, "Logout successfull", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

