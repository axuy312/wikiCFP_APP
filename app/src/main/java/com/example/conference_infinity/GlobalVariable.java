package com.example.conference_infinity;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;


public class GlobalVariable extends Application {
    HashMap<String, Object> conferences;
    HashMap<String, Object> categorys;
    int preferLangCode = -1;
    int preferThemeCode = -1;
    String userName = "N/A";
    String userEmail = "N/A";
    String[] preferCategory = null;
    String[] categoryPreview = null;
    String[] Language = {
            "Traditional",
            "English"
    };
    String[] Theme = {
            "Light",
            "Dark"
    };
    Bitmap headPhoto;
    ValueEventListener valueEventListener;
    DatabaseReference myRefConference;
    void remove(){
        myRefConference.removeEventListener(valueEventListener);
    }
    void setRealtime(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRefConference = database.getReference("Conference");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                UpdateConferences((HashMap)dataSnapshot.getValue());
                remove();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        };
        myRefConference.addValueEventListener(valueEventListener);


        DatabaseReference myRefCategory = database.getReference("Category");
        myRefCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                UpdateCategorys((HashMap)dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
        DatabaseReference myRefCategorysPreview = database.getReference("CategorysPreview");
        myRefCategorysPreview.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String[] listData = {};
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    listData = Arrays.copyOf(listData, listData.length + 1);
                    listData[listData.length - 1] = data.getKey();
                }
                UpdateCategorysPreviw(listData);
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    void UpdateConferences(HashMap hashMap){
        if (hashMap != null){
            conferences = (HashMap<String, Object>) hashMap.clone();
        }
    }


    void UpdateCategorysPreviw(String[] strings) {
        if (strings != null) {
            categoryPreview = strings.clone();
        }
    }

    void UpdateCategorys(HashMap hashMap){
        if (hashMap != null){
            categorys = (HashMap<String, Object>) hashMap.clone();
        }
    }
}
