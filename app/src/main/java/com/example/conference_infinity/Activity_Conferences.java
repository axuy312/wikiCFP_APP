package com.example.conference_infinity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashMap;

public class Activity_Conferences extends AppCompatActivity {

    //View
    private TextView title_view;
    private ListView Conference_List;
    SearchView Conference_search;
    MyListAdapter_Conference Conference_List_Adapter;
    //end View

    //
    private String title;
    private HashMap<String, String>[] Conference_List_Data;
    private GlobalVariable db;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferences);

        db = (GlobalVariable) getApplicationContext();
        //db.refreshDiscussCnt();

        title_view = findViewById(R.id.Category_Title);
        title = getIntent().getExtras().getString("title");
        if (title != null) {
            title_view.setText(title);
        }

        Conference_search = findViewById(R.id.Search_Conference);

        //Create List
        Conference_List = findViewById(R.id.Conference_List);


        //Update List
        HashMap<String, Object> conferences = (HashMap<String, Object>) db.categorys.get(title);
        HashMap<String, Object>[] listData = new HashMap[0];
        Object[] objectKeys = conferences.keySet().toArray();
        String[] keys = Arrays.copyOf(objectKeys, objectKeys.length, String[].class);
        for (int i = 0; i < keys.length; i++) {
            listData = Arrays.copyOf(listData, listData.length + 1);
            listData[listData.length - 1] = (HashMap<String, Object>) (((HashMap<String, Object>) conferences.get(keys[i])).clone());
        }
        UpdateConferenceData(listData);
        Conference_List_Adapter = new MyListAdapter_Conference(Activity_Conferences.this, Conference_List_Data);
        Conference_List.setAdapter(Conference_List_Adapter);

        Conference_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Conference_List_Adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Conference_List_Adapter != null) {
            Conference_List_Adapter.notifyDataSetChanged();
        }
    }

    public void UpdateConferenceData(HashMap[] dictionaries) {
        if (dictionaries != null) {
            Conference_List_Data = dictionaries.clone();
        } else {
            Conference_List_Data = null;
        }
    }
}