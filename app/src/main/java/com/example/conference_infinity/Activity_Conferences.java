package com.example.conference_infinity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.conference_infinity.listview.MyListAdapter_Conference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Activity_Conferences extends AppCompatActivity {

    //View
    private TextView title_view;
    private ListView Conference_List;
    //end View

    //
    private String title;
    private HashMap<String, String>[] Conference_List_Data;
    private  GlobalVariable db;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferences);

        db = (GlobalVariable)getApplicationContext();

        title_view = findViewById(R.id.Category_Title);
        title = getIntent().getExtras().getString("title");
        if (title != null){
            title_view.setText(title);
        }


        //Create List
        Conference_List = findViewById(R.id.Conference_List);
        Conference_List.setAdapter(new MyListAdapter_Conference(Activity_Conferences.this, null, title));
        Conference_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Activity_Conferences.this, Activity_Article.class);
                intent.putExtra("Topic", Conference_List_Data[position].get("Topic"));
                intent.putExtra("Abbreviation", Conference_List_Data[position].get("Abbreviation"));
                startActivity(intent);
                //getActivity().finish();
            }
        });

        Conference_List.setAdapter(new MyListAdapter_Conference(Activity_Conferences.this, Conference_List_Data, title));

        //Update List
        HashMap<String,Object>conferences = (HashMap<String, Object>) db.categorys.get(title);
        HashMap<String,Object>[] listData =  new HashMap[0];
        Object[] objectKeys = conferences.keySet().toArray();
        String[] keys = Arrays.copyOf(objectKeys, objectKeys.length, String[].class);
        for (int i = 0; i < keys.length; i++){
            listData = Arrays.copyOf(listData, listData.length + 1);
            listData[listData.length - 1] = (HashMap<String, Object>) (((HashMap<String,Object>) conferences.get(keys[i])).clone());
        }
        UpdateConferenceData(listData);
        Conference_List.setAdapter(new MyListAdapter_Conference(Activity_Conferences.this, Conference_List_Data, title));

    }

    public void UpdateConferenceData(HashMap[] dictionaries){
        if (dictionaries != null){
            Conference_List_Data = dictionaries.clone();
        }
        else {
            Conference_List_Data = null;
        }
    }
}