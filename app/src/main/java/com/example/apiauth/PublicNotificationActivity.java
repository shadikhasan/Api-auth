package com.example.apiauth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicNotificationActivity extends AppCompatActivity {

    private TextView textView;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_notification_acitvity);

        textView = findViewById(R.id.notificationTvId);

        ApiService apiService = RetrofitClient.getClient();

        // Make the API call
        Call<List<PublicNotification>> call = apiService.getPublicNotifications();
        call.enqueue(new Callback<List<PublicNotification>>() {
            @Override
            public void onResponse(Call<List<PublicNotification>> call, Response<List<PublicNotification>> response) {
                if (response.isSuccessful()) {
                    List<PublicNotification> notifications = response.body();

                    // Sort notifications by created_at in decreasing order
                    Collections.sort(notifications, new Comparator<PublicNotification>() {
                        @Override
                        public int compare(PublicNotification notification1, PublicNotification notification2) {
                            return notification2.getCreatedAt().compareTo(notification1.getCreatedAt());
                        }
                    });

                    StringBuilder notificationsString = new StringBuilder();
                    for (PublicNotification notification : notifications) {
                        notificationsString.append(notification.getMessage()).append(" ").append(notification.getCreatedAt()).append("\n\n");// Append each notification with a newline
                    }
                    textView.setText(notificationsString.toString());
                } else {
                    // Handle error response
                    textView.setText("Failed to retrieve notifications");
                }
            }

            @Override
            public void onFailure(Call<List<PublicNotification>> call, Throwable throwable) {

            }
        });
    }

}