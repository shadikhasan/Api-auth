package com.example.apiauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {

    private Button buttonSubmit;
    private EditText editTextLocation, editTextIssueType, editTextDescription, editTextSts; // Added editTextSts
    private CheckBox checkBoxAnonymous;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Initialize views
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextIssueType = findViewById(R.id.editTextIssueType);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextSts = findViewById(R.id.editTextSts); // Initialize editTextSts
        checkBoxAnonymous = findViewById(R.id.checkBoxAnonymous);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract data from EditText and CheckBox views
                String location = editTextLocation.getText().toString().trim();
                String issueType = editTextIssueType.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String stsWardNumber = editTextSts.getText().toString().trim(); // Get sts ward number
                boolean anonymous = checkBoxAnonymous.isChecked();

                createIssueReport(location, issueType, description, stsWardNumber, anonymous); // Pass sts ward number
            }
        });
    }

    private void createIssueReport(String location, String issueType, String description, String stsWardNumber, boolean anonymous) {
        // Initialize the Retrofit service
        ApiService apiService = RetrofitClient.getClient();

        // Retrieve access token from SharedPreferences
        String authToken = sharedPreferences.getString("AuthToken", null);

        // Create a new IssueReportRequest object
        IssueReportRequest issueReportRequest = new IssueReportRequest(location, issueType, description, stsWardNumber, anonymous); // Pass stsWardNumber

        // Call API to create a new issue report
        Call<Void> createIssueReportCall = apiService.createIssueReport(issueReportRequest);
        createIssueReportCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // Issue report created successfully
                    Toast.makeText(getApplicationContext(), "Issue report submitted successfully", Toast.LENGTH_SHORT).show();
                    // You may update the UI or perform any other action here
                } else {
                    // Error handling
                    String errorMessage = response.message();
                    Log.e("API Error", "Failed to submit issue report: " + errorMessage);
                    Toast.makeText(getApplicationContext(), "Failed to submit issue report", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, Throwable t) {
                // Failure handling
                Log.e("API Error", "Failed to submit issue report", t);
                Toast.makeText(getApplicationContext(), "Failed to submit issue report", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
