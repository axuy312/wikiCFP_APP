package com.example.conference_infinity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import at.markushi.ui.CircleButton;

public class Activity_Setting_Theme extends AppCompatActivity {
    AppCompatRadioButton light_btn, dark_btn;
    CircleButton back_btn;
    GlobalVariable user;
    boolean change = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_theme);

        light_btn = findViewById(R.id.setting_theme_light_btn);
        dark_btn = findViewById(R.id.setting_theme_dark_btn);
        back_btn = findViewById(R.id.setting_theme_back_btn);

        user = (GlobalVariable) getApplicationContext();

        // setting button
        if (user.preferThemeCode.equals(user.Language[0])) {
            // light theme
            light_btn.setChecked(true);
            dark_btn.setChecked(false);
        } else if (user.preferThemeCode.equals(user.Language[1])) {
            // dark theme
            light_btn.setChecked(false);
            dark_btn.setChecked(true);
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onRadioButtonClick(View view) {
        boolean isSelected = ((AppCompatRadioButton) view).isChecked();
        change = true;
        switch (view.getId()) {
            case R.id.setting_theme_light_btn:
                if (isSelected) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    user.preferThemeCode = user.Theme[0];
//                    light_btn.setTextColor(getColor(R.color.white));
//                    dark_btn.setTextColor(getColor(R.color.dark_gray));
                }
                break;
            case R.id.setting_theme_dark_btn:
                if (isSelected) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    user.preferThemeCode = user.Theme[1];
//                    light_btn.setTextColor(getColor(R.color.dark_gray));
//                    dark_btn.setTextColor(getColor(R.color.white));
                }
                break;
        }
    }

    void UploadPrefer(boolean theme, boolean lang) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        if (theme) {
            db.collection("Preference")
                    .document(user.userEmail)
                    .update("Theme", user.preferThemeCode)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);
                        }
                    });
        }
        if (lang) {
            db.collection("Preference")
                    .document(user.userEmail)
                    .update("Language", user.preferThemeCode)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);
                        }
                    });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // if theme has change
        if (change) {
            UploadPrefer(true, false);
        }
    }
}