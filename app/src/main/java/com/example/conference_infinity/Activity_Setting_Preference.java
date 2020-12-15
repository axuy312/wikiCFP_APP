package com.example.conference_infinity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import at.markushi.ui.CircleButton;

public class Activity_Setting_Preference extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_preference);

        CircleButton backBtn = findViewById(R.id.preference_back_btn);
        AppCompatButton themeBtn = findViewById(R.id.preference_theme_btn);
        AppCompatButton preferLanguageBtn = findViewById(R.id.prefernce_language_btn);
        AppCompatButton densityBtn = findViewById(R.id.preference_density_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        themeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Setting_Preference.this, Activity_Setting_Theme.class);
                startActivity(intent);
            }
        });

        preferLanguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}