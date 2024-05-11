package com.example.apiauth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Locale;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    TextView textViewUsers;
    Button changeLanguageButton;
    SharedPreferences sharedPreferences;
    private CardView reportCardView, publicNotificationCardView, mapCardView, communityEngagementCardView, socialSharingCardView, volunteerCardView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_main_2);



        //textViewUsers = findViewById(R.id.textViewUsers);
        changeLanguageButton = findViewById(R.id.changeLanguageButtonId);
        reportCardView = findViewById(R.id.reportCardViewId);
        publicNotificationCardView = findViewById(R.id.publicNotificationCardViewId);
        mapCardView = findViewById(R.id.mapCardViewId);
        communityEngagementCardView = findViewById(R.id.communityEngagementCardViewId);
        socialSharingCardView = findViewById(R.id.socialSharingCardViewId);
        volunteerCardView = findViewById(R.id.volunteerCardViewId);


        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        reportCardView.setOnClickListener(this);
        publicNotificationCardView.setOnClickListener(this);
        mapCardView.setOnClickListener(this);
        communityEngagementCardView.setOnClickListener(this);
        socialSharingCardView.setOnClickListener(this);
        volunteerCardView.setOnClickListener(this);

        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageDialog();
            }
        });


    }

    private void showLanguageDialog() {

        final  String[] languageList = {"English", "বাংলা", "日本"};

        SharedPreferences sharedPreferences = getSharedPreferences("LANGUAGE_SETTINGS", MODE_PRIVATE);
        int item = sharedPreferences.getInt("item", 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
        builder.setTitle(getString(R.string.change_lang));
        builder.setSingleChoiceItems(languageList, item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if(position == 0)
                {
                    setLanguage("en", 0);
                    recreate();
                }
                else if(position == 1)
                {
                    setLanguage("bn", 1);
                    recreate();
                }
                else if(position == 2)
                {
                    setLanguage("ja", 2);
                    recreate();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setLanguage(String language, int item) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());


        //save language
        SharedPreferences.Editor editor = getSharedPreferences("LANGUAGE_SETTINGS", MODE_PRIVATE).edit();
        editor.putString("language", language);
        editor.putInt("item", item);
        editor.apply();

    }

    private void loadLanguage()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("LANGUAGE_SETTINGS", MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "en");
        int item = sharedPreferences.getInt("item", 0);
        setLanguage(language, item);
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.reportCardViewId)
        {
            startActivity(new Intent(MainActivity2.this, ReportActivity.class));
            Toast.makeText(getApplicationContext(),"ReportActivity",Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == R.id.publicNotificationCardViewId)
        {
            startActivity(new Intent(MainActivity2.this, PublicNotificationActivity.class));
            Toast.makeText(getApplicationContext(),"PublicNotificationAcitvity",Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == R.id.communityEngagementCardViewId)
        {
            startActivity(new Intent(MainActivity2.this, CommunityEngagementActivity.class));
            Toast.makeText(getApplicationContext(),"CommunityEngagementActivity",Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == R.id.socialSharingCardViewId)
        {
            startActivity(new Intent(MainActivity2.this, SocialSharingActivity.class));
            Toast.makeText(getApplicationContext(),"SocialSharingActivity",Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == R.id.mapCardViewId)
        {
            startActivity(new Intent(MainActivity2.this, MapActivity.class));
            Toast.makeText(getApplicationContext(),"MapActivity",Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == R.id.volunteerCardViewId)
        {
            startActivity(new Intent(MainActivity2.this, VolunteerActivity.class));
            Toast.makeText(getApplicationContext(),"VolunteerActivity",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

