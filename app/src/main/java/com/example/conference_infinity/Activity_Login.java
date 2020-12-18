package com.example.conference_infinity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.Map;

public class Activity_Login extends AppCompatActivity {
    Button login_btn, guest_login_btn, register_btn;
    GlobalVariable user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (GlobalVariable) getApplicationContext();
        db = FirebaseFirestore.getInstance();

        login_btn = findViewById(R.id.login_btn);
        guest_login_btn = findViewById(R.id.guest_login_btn);
        register_btn = findViewById(R.id.register_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login("1073338@mail.com", "asdfghjjl");
            }
        });

        guest_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Activity_Login.this, Activity_Login_Phone.class);
//                startActivity(intent);
//                finish();
                Login("guest", "guest");
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, Activity_Register_Register.class);
                startActivity(intent);
                finish();
            }
        });

        // determine user theme
//        if (!user.preferThemeCode.equals("N/A") && user.preferThemeCode != null) {
//            if (user.preferThemeCode.equals(user.Theme[0])) {
//                // light theme
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            } else {
//                // dark theme
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            }
//        }
    }

    void Login(String email, String password) {
        db.collection("User")
                .document(email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> UserData = documentSnapshot.getData();
                        if (UserData != null && UserData.get("Password") != null && UserData.get("Password").equals(password)) {
                            user.loadUser(email, Activity_Login.this);
                            setLocale();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("----TAG----", "Error getting documents : " + e.getMessage());
                    }
                });
    }


    void nextPage() {
        Intent intent = new Intent(Activity_Login.this, Activity_Home_Home.class);
        startActivity(intent);
        finish();
    }

    private void setLocale() {
        Locale locale = Locale.getDefault();

        if (!user.preferLangCode.equals("N/A")) {
            if (user.preferLangCode.equals(user.Language[0])) {
                locale = Locale.TRADITIONAL_CHINESE;
            } else if (user.preferLangCode.equals(user.Language[1])) {
                locale = Locale.US;
            }
        }
        Log.d("----locale-----", locale.toString());
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        overwriteConfigurationLocale(config, locale);
    }

    private void overwriteConfigurationLocale(Configuration config, Locale locale) {
        config.locale = locale;
        getBaseContext().getResources()
                .updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}