package com.example.conference_infinity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.conference_infinity.register.Register_Density;
import com.example.conference_infinity.register.Register_Done;
import com.example.conference_infinity.register.Register_Language;
import com.example.conference_infinity.register.Register_Mail;
import com.example.conference_infinity.register.Register_Nickname;
import com.example.conference_infinity.register.Register_Password;
import com.example.conference_infinity.register.Register_Theme;
import com.example.conference_infinity.register.Register_Topics;
import com.example.conference_infinity.register.SectionsStatePagerAdapter;
import com.example.conference_infinity.register.States;

import java.util.Stack;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    // Use For fragment
    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private ViewPager mViewPager;
    private Stack<Integer> pageStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.d(TAG, "MainActivity onCreate");

        mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        // Setup the Pager
        setupViewPager(mViewPager);

        // Add first Page to Stack
        pageStack.push(0);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        // First Add First Show
        adapter.addFragment(new Register_Mail(), "Register_Mall");
        adapter.addFragment(new Register_Nickname(), "Register_Nickname");
        adapter.addFragment(new Register_Password(), "Register Password");
        adapter.addFragment(new Register_Language(), "Register Language");
        adapter.addFragment(new Register_Theme(), "Register Theme");
        adapter.addFragment(new Register_Density(), "Register Density");
        adapter.addFragment(new Register_Topics(), "Register Topics");
        adapter.addFragment(new Register_Done(), "Register Done");

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

        Log.d("pageStack", pageStack.toString());

        // Navigate to the assign Fragment
        mViewPager.setCurrentItem(fragmentNumber);
    }

    @Override
    public void onBackPressed() {

        if (pageStack.isEmpty()) {

            // Go to login Activity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else {

            // Go to last page
            setViewPager(pageStack.peek());
        }
    }
}