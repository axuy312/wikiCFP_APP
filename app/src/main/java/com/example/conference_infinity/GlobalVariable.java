package com.example.conference_infinity;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.ArrayList;
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
    String[] following_categoryPreview;
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

    void loadUser(String Email){
        if (db ==  null){
            db = FirebaseFirestore.getInstance();
        }

        db.collection("User")
                .document(Email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object>UserData = documentSnapshot.getData();
                        UpdataUser(UserData.get("Name").toString(), UserData.get("Password").toString(), UserData.get("Email").toString(), UserData.get("HeadPhoto").toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("----TAG----", "Error getting documents : " + e.getMessage());
                    }
                });

        db.collection("Preference")
                .document(Email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object>UserData = documentSnapshot.getData();

                        preferLangCode = UserData.get("Language").toString();
                        preferThemeCode = UserData.get("Theme").toString();
                        UpdatePreferCategorys((List<String>) UserData.get("Category"));
                        UpdataFollowCategory();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("----TAG----", "Error getting documents : " + e.getMessage());
                    }
                });



        Log.d("----Debug----",preferLangCode);
        //Load following

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
        following_categoryPreview = null;
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

    void UpdatePreferCategorys(List<String> list){
        if (list != null){
            preferCategory = new ArrayList<>();
            for(String t : list){
                preferCategory.add(t);
            }
        }
    }

    void UpdataUser(String name, String password, String email, String imgUrl){
        userName = name;
        userPassword = password;
        userEmail = email;
        if (imgUrl != null && !imgUrl.equals("N/A") && !imgUrl.isEmpty())
        {
            try {
                URL url = new URL(imgUrl);
                headPhoto = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                headPhoto = null;
                e.printStackTrace();
            }
        }
    }

    void UpdataFollowCategory(){
        if (preferCategory == null){
            return;
        }
        HashMap<String, Boolean>followingCategory = new HashMap<String, Boolean>();

        following_categoryPreview = new String[categoryPreview.length];

        int cnt = 0;
        for (int i = 0; i < preferCategory.size(); i++){
            followingCategory.put(preferCategory.get(i), true);
            following_categoryPreview[cnt] = preferCategory.get(i);
            cnt++;
        }


        for (int i = 0; i < categoryPreview.length; i++){
            if (followingCategory.get(categoryPreview[i]) == null){
                following_categoryPreview[cnt] = categoryPreview[i];
                cnt++;
            }
        }

    }
}
