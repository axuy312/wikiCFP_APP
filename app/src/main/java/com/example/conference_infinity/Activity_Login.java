package com.example.conference_infinity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.Map;

public class Activity_Login extends AppCompatActivity {
    Button login_btn, phone_login_btn, register_btn;
    GlobalVariable user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (GlobalVariable)getApplicationContext();
        db = FirebaseFirestore.getInstance();

        login_btn = findViewById(R.id.login_btn);
        phone_login_btn = findViewById(R.id.phone_login_btn);
        register_btn = findViewById(R.id.register_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login("aa@af.a", "aaaaaaaa");
            }
        });

        phone_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, Activity_Login_Phone.class);
                startActivity(intent);
                finish();
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
    }

    void Login(String email, String password){
        db.collection("User")
                .document(email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> UserData = documentSnapshot.getData();
                        if (UserData != null && UserData.get("Password") != null && UserData.get("Password").equals(password)){
                            user.loadUser(email, Activity_Login.this);
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


    void nextPage(){
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