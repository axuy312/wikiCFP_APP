package com.example.conference_infinity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Locale;
import java.util.Stack;

public class Activity_Home_Home extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    Deque<Integer> integerDeque = new ArrayDeque<>(4);
    boolean doubleBackToExitPressedOnce = false;
    GlobalVariable gv;
    Fragment home_fragment;
    Fragment category_fragment;
    Fragment pending_fragment;
    Fragment account_fragment;
    Fragment current_fragment;
    Stack<Integer> fragment_stack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_home);

        // determine user theme
//        if (!gv.preferThemeCode.equals("N/A") && gv.preferThemeCode != null) {
//            if (gv.preferThemeCode.equals(gv.Theme[0])) {
//                // light theme
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            } else {
//                // dark theme
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            }
//        }

        // Connect to xml
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);

        // Add home Fragment in Deque
        integerDeque.push(R.id.home_nav);

        gv = (GlobalVariable) getApplicationContext();

        setLocale();

        // new Fragment
        home_fragment = new Fragment_Home_Home();
        category_fragment = new Fragment_Home_Category();
        pending_fragment = new Fragment_Home_Pending();
        account_fragment = new Fragment_Home_Account();

        // Set home as default fragment
        bottomNavigationView.setSelectedItemId(R.id.home_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // Initial Home Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, home_fragment).commit();
            fragment_stack.push(R.id.home_nav);
            current_fragment = home_fragment;
        }
    }

    // bottom app bar onClickListener
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    // switch item
                    switch (item.getItemId()) {
                        case R.id.home_nav:
                            //Set Checked Home fragment
                            bottomNavigationView.getMenu().getItem(0).setChecked(true);
                            selectedFragment = home_fragment;
                            break;
                        case R.id.category_nav:
                            //Set Checked Home fragment
                            bottomNavigationView.getMenu().getItem(1).setChecked(true);

                            selectedFragment = category_fragment;
                            break;
                        case R.id.pending_nav:
                            //Set Checked Home fragment
                            bottomNavigationView.getMenu().getItem(2).setChecked(true);
                            if (gv != null && gv.pendingChange){
                                ((Fragment_Home_Pending)pending_fragment).refresh();
                                gv.pendingChange = false;
                            }
                            selectedFragment = pending_fragment;
                            break;
                        case R.id.account_nav:
                            //Set Checked Home fragment
                            bottomNavigationView.getMenu().getItem(3).setChecked(true);

                            selectedFragment = account_fragment;
                            break;
                    }

                    // get the top stack
                    if (fragment_stack != null && fragment_stack.size() > 1) {
                        fragment_stack.pop();
                    }
                    fragment_stack.push(item.getItemId());

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    if (!selectedFragment.isAdded()) {
                        Log.d("fragment transaction", "add");
                        fragmentTransaction.hide(current_fragment);
                        fragmentTransaction.add(R.id.fragment, selectedFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                        Log.d("fragment transaction", "show");
                        if (selectedFragment == home_fragment && selectedFragment == current_fragment) {
                            ((Fragment_Home_Home) home_fragment).ScrollTop();
                        }
                        fragmentTransaction.hide(current_fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.show(selectedFragment);
                        fragmentTransaction.commit();
                    }

                    current_fragment = selectedFragment;

                    return true;
                }
            };

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

    private void setLocale() {
        Locale locale = Locale.getDefault();

        if (gv.preferThemeCode != null) {
            if (!gv.preferLangCode.equals("N/A")) {
                if (gv.preferLangCode.equals(gv.Language[0])) {
                    locale = Locale.TRADITIONAL_CHINESE;
                } else if (gv.preferLangCode.equals(gv.Language[1])) {
                    locale = Locale.US;
                }
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