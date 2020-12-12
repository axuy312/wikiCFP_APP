package com.example.conference_infinity;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GlobalVariable extends Application {

    //Database--------------------------------------------------------------------------------------
    String[] categoryPreview = null;
    HashMap<String, Object> conferences;
    HashMap<String, Object> categorys;
    //----------------------------------------------------------------------------------------------

    //User preference-------------------------------------------------------------------------------
    String preferLangCode = "Traditional";
    String preferThemeCode = "Light";
    List<String> preferCategory = null;
    String[] Language = {
            "Traditional",
            "English"
    };
    String[] Theme = {
            "Light",
            "Dark"
    };
    //----------------------------------------------------------------------------------------------

    //User data-------------------------------------------------------------------------------------
    String userName = "N/A";
    String userPassword = "N/A";
    String userEmail = "N/A";
    Bitmap headPhoto;
    //----------------------------------------------------------------------------------------------

    //firebase--------------------------------------------------------------------------------------
    FirebaseDatabase database;
    FirebaseFirestore db;
    //----------------------------------------------------------------------------------------------

    boolean sucess = false;

    void setRealtime(){
        ValueEventListener valueEventListener;
        DatabaseReference myRefConference;

        database = FirebaseDatabase.getInstance();
        myRefConference = database.getReference("Conference");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                UpdateConferences((HashMap)dataSnapshot.getValue());
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

    boolean loadUser(String Email, String Password){
        if (db ==  null){
            db = FirebaseFirestore.getInstance();
        }


        sucess = true;
        db.collection("User")
                .document(Email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Map<String, Object>UserData = document.getData();

                            userName = UserData.get("Name").toString();
                            userPassword = UserData.get("Password").toString();;
                            userEmail = UserData.get("Email").toString();
                            String tmpUrl = UserData.get("HeadPhoto").toString();
                            if (tmpUrl != null && !tmpUrl.equals("N/A") && !tmpUrl.isEmpty())
                            {
                                try {
                                    URL url = new URL(tmpUrl);
                                    headPhoto = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                } catch (Exception e) {
                                    headPhoto = null;
                                    e.printStackTrace();
                                }
                            }
                        }
                        else {
                            sucess = false;
                            Log.w("----TAG----", "Error getting documents.", task.getException());
                        }
                    }
                });

        db.collection("Preference")
                .document(Email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Map<String, Object>UserData = document.getData();

                            preferLangCode = UserData.get("Language").toString();
                            preferThemeCode = UserData.get("Theme").toString();;
                            preferCategory = (List<String>) UserData.get("Category");
                        }
                        else {
                            sucess = false;
                            Log.w("----TAG----", "Error getting documents.", task.getException());
                        }
                    }
                });


        if (!sucess){
            clearUserData();
        }

        return true;
    }

    void clearUserData(){
        preferLangCode = "Traditional";
        preferThemeCode = "Light";
        if (preferCategory != null){
            preferCategory.clear();
            preferCategory = null;
        }

        userName = "N/A";
        userPassword = "N/A";
        userEmail = "N/A";
        headPhoto = null;
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
