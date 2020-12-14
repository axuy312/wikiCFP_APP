package com.example.conference_infinity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyachi.stepview.VerticalStepView;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Activity_Article extends AppCompatActivity {

    private at.markushi.ui.CircleButton back_btn;
    private TextView topicView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    VerticalStepView verticalStepView;

    private Fragment_Article_Content fragment_article_content;
    private Fragment_Article_Discuss fragment_article_discuss;
    private Fragment_Article_Arrangement fragment_article_arrangement;

    public String url = "";
    private String topic, abbreviation, Abstract_Registration_Due, when, where, Submission_Deadline, Notification_Due, Final_Version_Due;
    private GlobalVariable db;
    private HashMap<String, String>conference;


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

        if (topic != null){
            topicView.setText(topic);
        }
        if (when != null){

        }
        if (where != null){

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

        back_btn = findViewById(R.id.article_back_btn);


        verticalStepView = findViewById(R.id.test);

        List<String> list = new ArrayList<>();

        if (Abstract_Registration_Due != null){
            list.add(Abstract_Registration_Due + " - Abstract Registration Due");
        }
        if (Submission_Deadline != null){
            list.add(Submission_Deadline + " - Submission Deadline");
        }
        if (Notification_Due != null){
            list.add(Notification_Due + " - Notification Due");
        }
        if (Final_Version_Due != null){
            list.add(Final_Version_Due + " - Final Version Due");
        }

        verticalStepView.setStepsViewIndicatorComplectingPosition(1)
                .reverseDraw(false)
                .setTextSize(17)
                .setStepViewTexts(list)
                .setLinePaddingProportion(0.3f)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#000000"))
                .setStepViewComplectedTextColor(Color.parseColor("#000000"))
                .setStepViewUnComplectedTextColor(Color.parseColor("#000000"))
                .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#0adff0"))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.ic_timeline_on))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.ic_timeline_on))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.ic_timeline_off));

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment>fragments = new ArrayList<>();
        private List<String>fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);

        }

        public void addFragment(Fragment fragment, String title){
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