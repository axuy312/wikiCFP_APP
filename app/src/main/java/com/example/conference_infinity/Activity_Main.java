package com.example.conference_infinity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.Map;

public class Activity_Main extends AppCompatActivity {

    GlobalVariable gv;
    ImageView imageView;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.app_logo_gif);
        gv = (GlobalVariable) getApplicationContext();
        gv.initBitmapFromSharedPreferences();
        gv.setRealtime();
        db = FirebaseFirestore.getInstance();

        //gv.loadUser("aa@af.aa", "aaaaaaaa");

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (loadData()) {
                    loadUser();
                } else {
                    Animatable animatable = (Animatable) imageView.getDrawable();
                    animatable.start();
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.postDelayed(runnable, 1000);

    }

    boolean loadData() {
        return gv != null && gv.categorys != null && gv.conferences != null && gv.categoryPreview != null;
    }

    void loadUser() {
        String ac, pw;
        // clear user sharedPreference
        SharedPreferences sharedPreferences1 = getSharedPreferences("Account", Context.MODE_PRIVATE);
        ac = sharedPreferences1.getString("Account", null);
        if (ac != null) {
            if (!ac.equals("guest@guest.com")) {
                pw = sharedPreferences1.getString("Password", null);
                if (pw != null) {
                    if (!ac.equals("N/A") && !pw.equals("N/A")) {
                        Login(ac, pw);
                    } else {
                        startLogin();
                    }
                } else {
                    startLogin();
                }
            } else {
                startLogin();
            }
        } else {
            startLogin();
        }
    }

    public void setLocale() {
        Locale locale = Locale.getDefault();

        if (gv.preferLangCode != null) {
            if (gv.preferLangCode.equals(gv.Language[0])) {
                locale = Locale.TRADITIONAL_CHINESE;
            } else if (gv.preferLangCode.equals(gv.Language[1])) {
                locale = Locale.US;
            }
        }
        //Log.d("----locale-----", locale.toString());
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        overwriteConfigurationLocale(config, locale);
    }

    private void overwriteConfigurationLocale(Configuration config, Locale locale) {
        config.locale = locale;
        getBaseContext().getResources()
                .updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    void Login(String email, String password) {
        //Log.d("---User--- ", email);
        //Log.d("---Password--- ", password);
        db.collection("User")
                .document(email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> UserData = documentSnapshot.getData();
                        if (UserData != null && UserData.get("Password") != null && UserData.get("Password").equals(password)) {

                            gv.loadUser(email, Activity_Main.this);
                            //setLocale();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("----TAG----", "Error getting documents : " + e.getMessage());
                        startLogin();
                        //Toast.makeText(Activity_Main.this, "Wrong Account or Password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void startLogin() {
        Intent intent = new Intent(Activity_Main.this, Activity_Login.class);
        startActivity(intent);
        finish();
    }
}