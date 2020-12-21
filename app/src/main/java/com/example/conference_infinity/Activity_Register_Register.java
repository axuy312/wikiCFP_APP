package com.example.conference_infinity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.Stack;

public class Activity_Register_Register extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    // Use For fragment
    private Root_Register_SectionsStatePagerAdapter mFragmentRegisterSectionsStatePagerAdapter;
    private ViewPager mViewPager;
    private final Stack<Integer> pageStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_register);

        //Log.d(TAG, "MainActivity onCreate");

        mFragmentRegisterSectionsStatePagerAdapter = new Root_Register_SectionsStatePagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        // Setup the Pager
        setupViewPager(mViewPager);

        // Add first Page to Stack
        pageStack.push(0);

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        Root_Register_SectionsStatePagerAdapter adapter = new Root_Register_SectionsStatePagerAdapter(getSupportFragmentManager());

        // First Add First Show
        adapter.addFragment(new Fragment_Register_Mail(), "Register_Mall");
        adapter.addFragment(new Fragment_Register_Nickname(), "Register_Nickname");
        adapter.addFragment(new Fragment_Register_Password(), "Register Password");
        adapter.addFragment(new Fragment_Register_UploadIMG(), "Register UploadIMG");
        adapter.addFragment(new Fragment_Register_Language(), "Register Language");
        adapter.addFragment(new Fragment_Register_Theme(), "Register Theme");
        adapter.addFragment(new Fragment_Register_Topics(), "Register Topics");
        adapter.addFragment(new Fragment_Register_Done(), "Register Done");

        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {

        // Determine whether exist
        if (pageStack.contains(fragmentNumber)) {
            pageStack.pop();
        } else {
            // Put in Stack to record Last Page
            pageStack.push(fragmentNumber);
        }

        //Log.d("pageStack", pageStack.toString());

        // Navigate to the assign Fragment
        mViewPager.setCurrentItem(fragmentNumber);
    }

    @Override
    public void onBackPressed() {

        //Log.d("activity backpress", pageStack.toString());

        if (pageStack.isEmpty()) {

            // Go to login Activity
            Intent intent = new Intent(Activity_Register_Register.this, Activity_Login.class);
            startActivity(intent);
            finish();

        } else {

            if (pageStack.peek() < 0) {
                // Go to login Activity
                Intent intent = new Intent(Activity_Register_Register.this, Activity_Login.class);
                startActivity(intent);
                finish();
            }
            // Go to last page
            setViewPager(pageStack.peek() - 1);
        }
    }
}