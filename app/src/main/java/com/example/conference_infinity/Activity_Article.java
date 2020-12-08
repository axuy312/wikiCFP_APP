package com.example.conference_infinity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
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

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class Activity_Article extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private BottomAppBar bottomAppBar;
    private at.markushi.ui.CircleButton back_btn;
    private WebView webView;
    private LinearLayout loadWeb;
    private TextView topicView, deadlineView, whenView, whereView;

    private String url = "";
    private String topic, deadline, link, when, where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        topicView = findViewById(R.id.Conference_topic);
        deadlineView = findViewById(R.id.Conference_deadline);
        whenView = findViewById(R.id.Conference_when);
        whereView = findViewById(R.id.Conference_where);

        topic = deadline = link = when = where = "N/A";

        topic = getIntent().getExtras().getString("Topic");
        deadline = getIntent().getExtras().getString("Deadline");
        link = getIntent().getExtras().getString("Link");
        when = getIntent().getExtras().getString("When");
        where = getIntent().getExtras().getString("Where");
        if (topic != null){
            topicView.setText(topic);
        }
        if (deadline != null){
            deadlineView.setText(deadline);
        }
        if (link != null){
            url = link;
        }
        if (when != null){
            whenView.setText(when);
        }
        if (where != null){
            whereView.setText(where);
        }

        floatingActionButton = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        back_btn = findViewById(R.id.article_back_btn);
        webView = findViewById(R.id.webView);
        loadWeb = findViewById(R.id.load_web);

        /* main line for getting menu in bottom app bar */
        setSupportActionBar(bottomAppBar);
        loadWebView();
        floatingActionButton.bringToFront();//移到最上層

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View viewPos = findViewById(R.id.coordinator_layout);
                Snackbar snackbar = Snackbar.make(
                        viewPos,
                        "FAB Clicked",
                        Snackbar.LENGTH_LONG
                ).setAction("UNDO", null);
                snackbar.setAnchorView(floatingActionButton);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                snackbar.show();

            }
        });

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);

                BottomSheetDialogFragment bottomSheetDialogFragment = new BottomDialogFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getClass().getSimpleName());
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.article_bottom_app_bar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_bar_translate:
                Toast.makeText(this, "Translate", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bottom_bar_chat:
                Toast.makeText(this, "Comments", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bottom_bar_time:
                Toast.makeText(this, "Time", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bottom_bar_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
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
}