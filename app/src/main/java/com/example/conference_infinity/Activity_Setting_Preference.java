package com.example.conference_infinity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import at.markushi.ui.CircleButton;

public class Activity_Setting_Preference extends AppCompatActivity {
    String lang;
    GlobalVariable gv;
    AppCompatButton themeBtn, preferLanguageBtn, densityBtn;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_preference);

        CircleButton backBtn = findViewById(R.id.preference_back_btn);
        themeBtn = findViewById(R.id.preference_theme_btn);
        preferLanguageBtn = findViewById(R.id.prefernce_language_btn);
        densityBtn = findViewById(R.id.preference_density_btn);
        title = findViewById(R.id.preference_title);
        gv = (GlobalVariable) getApplicationContext();
        lang = gv.preferLangCode;

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
                Intent intent = new Intent(Activity_Setting_Preference.this, Activity_Setting_Language.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!lang.equals(gv.preferLangCode)) {
            //setContentView(R.layout.activity_setting_preference);
            themeBtn.setText(getText(R.string.theme));
            preferLanguageBtn.setText(getText(R.string.prefer_language));
            title.setText(getText(R.string.preference));
        }
    }
}