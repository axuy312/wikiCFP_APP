package com.example.conference_infinity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.ArrayDeque;
import java.util.Deque;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    Deque<Integer> integerDeque = new ArrayDeque<>(4);
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Connect to xml
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);



        // Add home Fragment in Deque
        integerDeque.push(R.id.home_nav);

        //Load Home Fragment
        loadFragment(new HomeFragment());

        // Set home as default fragment
        bottomNavigationView.setSelectedItemId(R.id.home_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get Selected item id
                int id = item.getItemId();

                if (integerDeque.contains(id))
                {
                    //When deque list contains selected id

                    if (id == R.id.home_nav)
                    {
                        //When selected id is equal to home fragment

                        if (integerDeque.size() != 1)
                        {
                            //When deque list size is not equal to 1

                            if (flag)
                            {
                                // When flag value is true
                                // Add home fragment in deque list
                                integerDeque.addFirst(R.id.home_nav);
                                //Set flag to false
                                flag = false;
                            }
                        }

                    }

                    //Remove Selected id from deque list
                    integerDeque.remove(id);
                }
                // Push selected id in deque list
                integerDeque.push(id);

                //Load Fragment
                loadFragment(getFragment(item.getItemId()));
                return true;
            }
        });
    }

    private Fragment getFragment(int itemId) {
        switch(itemId)
        {
            case R.id.home_nav:
                //Set Checked Home fragment
                bottomNavigationView.getMenu().getItem(0).setChecked(true);

                //Return home fragment
                return new HomeFragment();

                case R.id.category_nav:
                //Set Checked Home fragment
                bottomNavigationView.getMenu().getItem(1).setChecked(true);

                //Return home fragment
                return new CategoryFragment();

                case R.id.pending_nav:
                //Set Checked Home fragment
                bottomNavigationView.getMenu().getItem(2).setChecked(true);

                //Return home fragment
                return new PendingFragment();
            case R.id.account_nav:
                //Set Checked Home fragment
                bottomNavigationView.getMenu().getItem(3).setChecked(true);

                //Return home fragment
                return new AccountFragment();
        }

        // Set checked default home fragment
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        //Return home fragment
        return new HomeFragment();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment, fragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void onBackPressed() {
        // Pop to previous fragment
        integerDeque.pop();

        if(!integerDeque.isEmpty())
        {
            // When deque list is not empty
            //Load fragment
            loadFragment(getFragment(integerDeque.peek()));
        }
        else
        {
            // When deque list is empty
            finish();
        }
    }
}