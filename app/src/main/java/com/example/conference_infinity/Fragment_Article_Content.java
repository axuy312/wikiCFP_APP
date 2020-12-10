package com.example.conference_infinity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Article_Content#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Article_Content extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String url = "";

    private WebView webView;
    private LinearLayout loadWeb;

    public Fragment_Article_Content(String url) {
        // Required empty public constructor
        this.url = url;
    }

    public Fragment_Article_Content() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_article_content.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Article_Content newInstance(String param1, String param2) {
        Fragment_Article_Content fragment = new Fragment_Article_Content();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = getActivity().findViewById(R.id.webView);
        loadWeb = getActivity().findViewById(R.id.load_web);
        loadWebView();
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
                //view.loadUrl("javascript:showDetail();");
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