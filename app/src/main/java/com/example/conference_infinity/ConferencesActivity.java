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

import com.example.conference_infinity.listview.MyListAdapter_Category;
import com.example.conference_infinity.listview.MyListAdapter_Conference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class ConferencesActivity extends AppCompatActivity {

    //View
    private TextView title_view;
    private ListView Conference_List;
    //end View

    //
    private String title;
    private HashMap<String, String>[] Conference_List_Data;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferences);
        title_view = findViewById(R.id.Category_Title);
        title = getIntent().getExtras().getString("title");
        if (title != null){
            title_view.setText(title);
        }


        //Create List
        Conference_List = findViewById(R.id.Conference_List);
        Conference_List.setAdapter(new MyListAdapter_Conference(ConferencesActivity.this, null, title));
        Conference_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ConferencesActivity.this, ArticleActivity.class);
                //intent.putExtra("title", Category_List_Data[position]);
                startActivity(intent);
                //getActivity().finish();
            }
        });

        //Update List
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Category/"+title);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String keys = dataSnapshot.getKey();
                if (dataSnapshot == null)
                    Toast.makeText(ConferencesActivity.this, "Realtime database error: No data", Toast.LENGTH_LONG).show();
                else {
                    HashMap[] listData = {};
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        Map<String, Object> values = (Map<String, Object>)data.getValue();
                        listData = Arrays.copyOf(listData, listData.length + 1);
                        listData[listData.length - 1] = new HashMap<String,String>();
                        listData[listData.length - 1].put("Topic",values.get("Topic"));
                        listData[listData.length - 1].put("Deadline",values.get("Deadline"));
                    }
                    UpdateConferenceData(listData);
                    Conference_List.setAdapter(new MyListAdapter_Conference(ConferencesActivity.this, Conference_List_Data, title));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(ConferencesActivity.this, "Realtime database error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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