package com.example.conference_infinity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity_Phone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__phone);

        Button login_btn = findViewById(R.id.login_btn);
        Button login_mail_btn = findViewById(R.id.login_mail_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity_Phone.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login_mail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity_Phone.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}