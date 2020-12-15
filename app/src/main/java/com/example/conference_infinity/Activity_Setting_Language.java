package com.example.conference_infinity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import at.markushi.ui.CircleButton;

public class Activity_Setting_Language extends AppCompatActivity {
    private AppCompatButton Language_ZH, Language_EN;
    private CircleButton back_btn;
    GlobalVariable user;
    boolean change = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_language);

        Language_EN = findViewById(R.id.setting_language_en_us);
        Language_ZH = findViewById(R.id.setting_language_zh_tw);
        back_btn = findViewById(R.id.setting_language_back_btn);

        user = (GlobalVariable) getApplicationContext();

        setLanguage();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Language_EN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.preferLangCode = user.Language[1];
                setLanguage();
                change = true;
                //((RegisterActivity) getActivity()).setViewPager(new States().getFragmentNum(States.STATE_THEME));
            }
        });

        Language_ZH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.preferLangCode = user.Language[0];
                setLanguage();
                change = true;
                //((RegisterActivity) getActivity()).setViewPager(new States().getFragmentNum(States.STATE_THEME));
            }
        });

    }

    private void setLanguage()
    {
        if(user.preferLangCode.equals(user.Language[1]))
        {
            Language_EN.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_en_us), null, getResources().getDrawable(R.drawable.ic_done), null);
            Language_ZH.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_zh_tw), null, null, null);
        }
        else if(user.preferLangCode.equals(user.Language[0]))
        {
            Language_EN.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_en_us), null, null, null);
            Language_ZH.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_zh_tw), null, getResources().getDrawable(R.drawable.ic_done), null);
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
            UploadPrefer(false, true);
        }
    }
}