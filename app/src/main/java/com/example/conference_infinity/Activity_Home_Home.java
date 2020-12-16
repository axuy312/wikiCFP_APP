package com.example.conference_infinity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.ArrayDeque;
import java.util.Deque;

public class Activity_Home_Home extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    Deque<Integer> integerDeque = new ArrayDeque<>(4);
    boolean flag = true;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_home);

        // Connect to xml
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);

        // Add home Fragment in Deque
        integerDeque.push(R.id.home_nav);

        //Load Home Fragment
        loadFragment(new Fragment_Home_Home());

        // Set home as default fragment
        bottomNavigationView.setSelectedItemId(R.id.home_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get Selected item id
                int id = item.getItemId();

                // only save home fragment in the first
                if (integerDeque.size() > 1) {
                    integerDeque.pop();
                }
                integerDeque.push(id);

                //Log.d("integerDeque", integerDeque.toString());

                //Load Fragment
                loadFragment(getFragment(item.getItemId()));
                return true;
            }
        });
    }

    private Fragment getFragment(int itemId) {
        switch (itemId) {
            case R.id.home_nav:
                //Set Checked Home fragment
                bottomNavigationView.getMenu().getItem(0).setChecked(true);

                //Return home fragment
                return new Fragment_Home_Home();

            case R.id.category_nav:
                //Set Checked Home fragment
                bottomNavigationView.getMenu().getItem(1).setChecked(true);

                //Return home fragment
                return new Fragment_Home_Category();

            case R.id.pending_nav:
                //Set Checked Home fragment
                bottomNavigationView.getMenu().getItem(2).setChecked(true);

                //Return home fragment
                return new Fragment_Home_Pending();
            case R.id.account_nav:
                //Set Checked Home fragment
                bottomNavigationView.getMenu().getItem(3).setChecked(true);

                //Return home fragment
                return new Fragment_Home_Account();
        }

        // Set checked default home fragment
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        //Return home fragment
        return new Fragment_Home_Home();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment, fragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void onBackPressed() {
        // When deque list is not empty
        if (!integerDeque.isEmpty()) {
            integerDeque.pop();
        }

        // Pop to previous fragment
        if (!integerDeque.isEmpty()) {
            //Load fragment
            loadFragment(getFragment(integerDeque.peek()));
        } else {
            // When deque list is empty
            // check double click
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
            }

            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Double BackPressed to exit", Toast.LENGTH_SHORT).show();

            // delay to get the second back press
            final Runnable runnable = new Runnable() {
                public void run() {
                    doubleBackToExitPressedOnce = false;
                    // Add home Fragment in Deque
                    integerDeque.addFirst(R.id.home_nav);
                }
            };

            Handler handler = new Handler();
            handler.postDelayed(runnable, 800);
        }

        Log.d("integerDeque", integerDeque.toString());
    }
}