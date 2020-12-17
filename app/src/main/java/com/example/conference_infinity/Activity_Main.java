package com.example.conference_infinity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Activity_Main extends AppCompatActivity {

    GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gv = (GlobalVariable) getApplicationContext();
        gv.initBitmapFromSharedPreferences();
        gv.setRealtime();

        setLocale();
        //gv.loadUser("aa@af.aa", "aaaaaaaa");

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (loadData()) {
                    Intent intent = new Intent(Activity_Main.this, Activity_Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.postDelayed(runnable, 1000);

    }

    boolean loadData() {
        if (gv == null || gv.categorys == null || gv.conferences == null || gv.categoryPreview == null)
            return false;
        return true;
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