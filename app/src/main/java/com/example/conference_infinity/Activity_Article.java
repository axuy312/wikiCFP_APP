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
    private WebView webView;
    private LinearLayout loadWeb;
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
        fragment_article_discuss = new Fragment_Article_Discuss();
        fragment_article_arrangement = new Fragment_Article_Arrangement();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragment_article_content, "Content");
        viewPagerAdapter.addFragment(fragment_article_discuss, "Discuss");
        viewPagerAdapter.addFragment(fragment_article_arrangement, "Arrangement");
        viewPager.setAdapter(viewPagerAdapter);

        //back_btn = findViewById(R.id.article_back_btn);
        //webView = findViewById(R.id.webView);
        //loadWeb = findViewById(R.id.load_web);


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

        //loadWebView();

        /*back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }


    //Class code------------------------------------------------------------------------------------
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

    //Function--------------------------------------------------------------------------------------
    void loadWebView(){
        //webView.loadDataWithBaseURL(null, testStr, "text/html", "utf-8", null);

        if (url == null || url.isEmpty() || url.equals("N/A")){
            return;
        }

        webView.setInitialScale(400);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        //webSettings.setUseWideViewPort(true);
        //webSettings.setBuiltInZoomControls(true);
        //webSettings.setDisplayZoomControls(true);
        //webSettings.setSupportZoom(true);
        //webSettings.setDefaultTextEncodingName("utf-8");

        webView.setWebViewClient(new HelloWebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                loadWeb.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                view.setVisibility(View.GONE);
                loadWeb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String fun="javascript:function getClass(parent,sClass) { var aEle=parent.getElementsByTagName('div'); var aResult=[]; var i=0; for(i<0;i<aEle.length;i++) { if(aEle[i].className==sClass) { aResult.push(aEle[i]); } }; return aResult; } ";

                //view.loadUrl(fun);
                String javascript="javascript:function showDetail()\n" +
                        "{\n" +
                        "\tvar table = document.getElementsByClassName(\"contsec\");\n" +
                        "\tif(table.length > 0){\n" +
                        "\t\tdocument.body.innerHTML = table[0].innerHTML;\n" +
                        "\t}\n" +
                        "\tvar form = document.getElementsByTagName(\"form\");\n" +
                        "\tif(form.length > 0){\n" +
                        "\t\twhile(form.length > 0){\n" +
                        "\t\t\tform[0].parentNode.removeChild(form[0])\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "\t\n" +
                        "}\n" +
                        "showDetail()";
                view.loadUrl(javascript);
                view.loadUrl("javascript:showDetail();");
                loadWeb.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
                Log.d("RUN-----------------", "url=" + url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {

                } else {
                    Log.e("TAG", "url=" + url);
                }
                return true;
            }
        });

        webView.loadUrl(url);
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