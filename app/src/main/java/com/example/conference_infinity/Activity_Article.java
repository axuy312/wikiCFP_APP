package com.example.conference_infinity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.baoyachi.stepview.VerticalStepView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Activity_Article extends AppCompatActivity {

    private at.markushi.ui.CircleButton back_btn;
    private TextView topicView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private EditText input_editText;
    private ImageButton send_img, attend_img;
    VerticalStepView verticalStepView;

    private Fragment_Article_Content fragment_article_content;
    private Fragment_Article_Discuss fragment_article_discuss;
    private Fragment_Article_Arrangement fragment_article_arrangement;

    public String url = "";
    private String topic, abbreviation, Abstract_Registration_Due, when, where, Submission_Deadline, Notification_Due, Final_Version_Due;
    private GlobalVariable db;
    private HashMap<String, String> conference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);


        db = (GlobalVariable) getApplicationContext();
        topicView = findViewById(R.id.Conference_topic);
        tabLayout = findViewById(R.id.article_tablayout);
        viewPager = findViewById(R.id.article_viewpager);


        topic = getIntent().getExtras().getString("Topic", "N/A");
        abbreviation = getIntent().getExtras().getString("Abbreviation", "N/A");

        Log.d("---abbreviation----", abbreviation);
        conference = (HashMap<String, String>) db.conferences.get(abbreviation);

        url = conference.get("Link");
        when = conference.get("When");
        where = conference.get("Where");
        Abstract_Registration_Due = conference.get("Abstract Registration Due");
        Submission_Deadline = conference.get("Submission Deadline");
        Notification_Due = conference.get("Notification Due");
        Final_Version_Due = conference.get("Final Version Due");


        if (topic != null) {
            topicView.setText(topic);
        }
        if (when != null) {

        }
        if (where != null) {

        }

        fragment_article_content = new Fragment_Article_Content(url);
        fragment_article_discuss = new Fragment_Article_Discuss(abbreviation);
        fragment_article_arrangement = new Fragment_Article_Arrangement(where);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragment_article_content, "Content");
        viewPagerAdapter.addFragment(fragment_article_discuss, "Discuss");
        viewPagerAdapter.addFragment(fragment_article_arrangement, "Arrangement");
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1)
                    ((LinearLayout) findViewById(R.id.discuss_input_layout)).setVisibility(View.VISIBLE);
                else
                    ((LinearLayout) findViewById(R.id.discuss_input_layout)).setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        back_btn = findViewById(R.id.article_back_btn);

        input_editText = findViewById(R.id.discuss_input_edittext);
        send_img = findViewById(R.id.discuss_send_img);
        attend_img = findViewById(R.id.attend_imgbtn);

        input_editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LinearLayout linearLayout = findViewById(R.id.discuss_input_layout);
                linearLayout.setGravity(Gravity.CENTER);
                return false;
            }
        });

        send_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input_editText.getText().toString();
                if (text != null && !text.isEmpty()) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Discuss/" + abbreviation);

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot == null) {
                                Log.d("----data---", "NULL");
                            } else {
                                long i = snapshot.getChildrenCount();
                                Log.d("----data---", "Count: " + String.valueOf(i) + " ---> " + snapshot.toString());
                                DatabaseReference myRefChild = database.getReference("Discuss/" + abbreviation + "/" + String.valueOf(i + 1));
                                HashMap<String, Object> data = new HashMap<>();
                                data.put("Email", db.userEmail);
                                data.put("Name", db.userName);
                                data.put("Content", text);
                                data.put("Time", ServerValue.TIMESTAMP);

                                myRefChild.setValue(data);
                                db.UpdateDiscussCnt(abbreviation, i + 1);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    input_editText.setText("");
                }
            }
        });

        attend_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.pendingConference.get(abbreviation) != null && ((HashMap) db.pendingConference.get(abbreviation)).get("Attend") != null && (Boolean) ((HashMap) db.pendingConference.get(abbreviation)).get("Attend")) {
                    attend_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_attended));
                    db.UpdateAttendConferencesValue(abbreviation, false, null, false);
                } else {
                    attend_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_unattended));
                    db.UpdateAttendConferencesValue(abbreviation, true, null, false);
                }
            }
        });

        if (db.pendingConference.get(abbreviation) != null && ((HashMap) db.pendingConference.get(abbreviation)).get("Attend") != null && (Boolean) ((HashMap) db.pendingConference.get(abbreviation)).get("Attend")) {
            attend_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_unattended));
        } else {
            attend_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_attended));
        }

        verticalStepView = findViewById(R.id.test);


        Long current = (new Date()).getTime();
        boolean gettime = false;
        int cnttime = 0;

        SimpleDateFormat f = new SimpleDateFormat("MMM d yyyy", Locale.ENGLISH);

        List<String> list = new ArrayList<>();

        if (Abstract_Registration_Due != null) {
            list.add(Abstract_Registration_Due + " - Abstract Registration Due");
            if (!gettime) {
                try {
                    Date d = f.parse(Abstract_Registration_Due.replaceAll("[^0-9a-zA-Z ]", ""));
                    long milliseconds = d.getTime();
                    if (milliseconds > current) {
                        gettime = true;
                    } else {
                        cnttime += 1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (Submission_Deadline != null) {
            list.add(Submission_Deadline + " - Submission Deadline");
            if (!gettime) {
                try {
                    Date d = f.parse(Submission_Deadline.replaceAll("[^0-9a-zA-Z ]", ""));
                    long milliseconds = d.getTime();
                    if (milliseconds > current) {
                        gettime = true;
                    } else {
                        cnttime += 1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (Notification_Due != null) {
            list.add(Notification_Due + " - Notification Due");
            if (!gettime) {
                try {
                    Date d = f.parse(Notification_Due.replaceAll("[^0-9a-zA-Z ]", ""));
                    long milliseconds = d.getTime();
                    if (milliseconds > current) {
                        gettime = true;
                    } else {
                        cnttime += 1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (Final_Version_Due != null) {
            list.add(Final_Version_Due + " - Final Version Due");
            if (!gettime) {
                try {
                    Date d = f.parse(Final_Version_Due.replaceAll("[^0-9a-zA-Z ]", ""));
                    long milliseconds = d.getTime();
                    if (milliseconds > current) {
                        gettime = true;
                    } else {
                        cnttime += 1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("time----", Submission_Deadline);
        Log.d("time----", Submission_Deadline.replaceAll("[^0-9a-zA-Z ]", ""));
        Log.d("time----", String.valueOf(cnttime) + " / " + String.valueOf(list.size()));


        verticalStepView.setStepsViewIndicatorComplectingPosition(cnttime)
                .reverseDraw(false)
                .setTextSize(15)
                .setStepViewTexts(list)
                .setLinePaddingProportion(0.2f)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#000000"))
                .setStepViewComplectedTextColor(Color.parseColor("#000000"))
                .setStepViewUnComplectedTextColor(Color.parseColor("#000000"))
                .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#000000"))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.ic_timeline_on))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.ic_timeline_off))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.ic_timeline_off));

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);

        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

}