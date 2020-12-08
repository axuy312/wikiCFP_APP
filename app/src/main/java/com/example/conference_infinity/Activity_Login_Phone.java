package com.example.conference_infinity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity_Login_Phone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);

        Button login_btn = findViewById(R.id.login_btn);
        Button login_mail_btn = findViewById(R.id.login_mail_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login_Phone.this, Activity_Home_Home.class);
                startActivity(intent);
                finish();
            }
        });

        login_mail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login_Phone.this, Activity_Article.class);
                startActivity(intent);
                finish();
            }
        });
    }
}