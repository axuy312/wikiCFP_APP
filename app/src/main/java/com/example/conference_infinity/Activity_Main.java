package com.example.conference_infinity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Activity_Main extends AppCompatActivity {

    GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gv = (GlobalVariable)getApplicationContext();
        gv.setRealtime();
        //gv.loadUser("aa@af.aa", "aaaaaaaa");

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (loadData()){
                    Intent intent = new Intent(Activity_Main.this, Activity_Login.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    handler.postDelayed(this,1000);
                }
            }
        };

        handler.postDelayed(runnable,1000);

    }

    boolean loadData(){
        if (gv == null || gv.categorys == null || gv.conferences == null || gv.categoryPreview == null)
            return false;
        return true;
    }
}