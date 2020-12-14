package com.example.conference_infinity;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.google.firebase.firestore.SetOptions;

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
    HashMap<String, Boolean> preferCategory = null;
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
    String headPhotoURL = "N/A";
    //----------------------------------------------------------------------------------------------

    //Pending Conference----------------------------------------------------------------------------
    ArrayList<Model> models = new ArrayList<>();
    //----------------------------------------------------------------------------------------------

    //firebase--------------------------------------------------------------------------------------
    FirebaseDatabase database;
    FirebaseFirestore db;
    //----------------------------------------------------------------------------------------------


    void setRealtime() {
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

                myRefConference.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        };
        myRefConference.addValueEventListener(valueEventListener);


        DatabaseReference myRefCategory = database.getReference("Category");
        myRefCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                UpdateCategorys((HashMap)dataSnapshot.getValue());

                myRefCategory.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        DatabaseReference myRefCategorysPreview = database.getReference("CategorysPreview");
        myRefCategorysPreview.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String[] listData = {};
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    listData = Arrays.copyOf(listData, listData.length + 1);
                    listData[listData.length - 1] = data.getKey();
                }
                UpdateCategorysPreviw(listData);

                myRefCategorysPreview.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    void loadUser(String Email) {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }

        db.collection("User")
                .document(Email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> UserData = documentSnapshot.getData();
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
                        Map<String, Object> UserData = documentSnapshot.getData();

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


        Log.d("----Debug----", preferLangCode);
        //Load following

    }


    void clearUserData(){
        preferLangCode = "Traditional";
        preferThemeCode = "Light";
        if (preferCategory != null) {
            preferCategory.clear();
            preferCategory = null;
        }

        userName = "N/A";
        userPassword = "N/A";
        userEmail = "N/A";
        headPhoto = null;
        headPhotoURL = "N/A";
        following_categoryPreview = null;
    }

    void UpdateConferences(HashMap hashMap) {
        if (hashMap != null) {
            conferences = (HashMap<String, Object>) hashMap.clone();
        }
    }


    void UpdateCategorysPreviw(String[] strings) {
        if (strings != null) {
            categoryPreview = strings.clone();
        }
    }

    void UpdateCategorys(HashMap hashMap) {
        if (hashMap != null) {
            categorys = (HashMap<String, Object>) hashMap.clone();
        }
    }

    void UpdatePreferCategorys(List<String> list){
        if (list != null){
            preferCategory = new HashMap<String, Boolean>();
            for(String t : list){
                preferCategory.put(t, true);
            }
        }
    }

    //Updata & Upload
    boolean UpdatePreferCategorysValue(String title, Boolean bool){
        if (preferCategory != null){
            preferCategory.put(title, bool);
            Log.d("---TAG---", title+" : "+bool.toString());

            List<String>data = new ArrayList<>();
            for (String key : preferCategory.keySet().toArray(new String[0])){
                if (preferCategory.get(key) == true){
                    data.add(key);
                }
            }

            Map<String, Object> user = new HashMap<>();
            user.put("Category", data);

            db.collection("Preference")
                    .document(userEmail)
                    .update("Category", data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "Len: "+String.valueOf(data.size()));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);
                        }
                    });
            return true;
        }
        return false;
    }

    void UpdataUser(String name, String password, String email, String imgUrl) {
        userName = name;
        userPassword = password;
        userEmail = email;
        if (imgUrl != null && !imgUrl.equals("N/A") && !imgUrl.isEmpty()) {
            new GetBitmap().execute(imgUrl);
        }
    }

    void UpdataFollowCategory() {
        if (preferCategory == null) {
            return;
        }

        following_categoryPreview = new String[categoryPreview.length];


        int cnt = 0;
      
      
        for (int i = 0; i < categoryPreview.length; i++){
            if (preferCategory.get(categoryPreview[i]) != null && preferCategory.get(categoryPreview[i]) == true){
                following_categoryPreview[cnt] = categoryPreview[i];
                cnt++;
            }
        }


        for (int i = 0; i < categoryPreview.length; i++){
            if (preferCategory.get(categoryPreview[i]) == null || preferCategory.get(categoryPreview[i]) == false){

                following_categoryPreview[cnt] = categoryPreview[i];
                cnt++;
            }
        }

    }

    private class GetBitmap extends AsyncTask<String, Integer, Bitmap> {
        String urlStr;

        @Override
        protected void onPreExecute() {
            //執行前
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //執行中

            urlStr = params[0];
            try {
                URL url = new URL(urlStr);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
                headPhotoURL = "N/A";
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //執行進度
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //執行後
            super.onPostExecute(bitmap);
            headPhoto = bitmap;
            headPhotoURL = urlStr;
        }
    }

    // TODO: 取得 firebase 的 pending conference
    public ArrayList<Model> getPendingConference()
    {
        if(models.isEmpty())
        {
            ArrayList<String> prepare = new ArrayList<>();
            prepare.add("test1");
            prepare.add("Test2");

            Model model = new Model();
            model.setConference_name("New Conference Name");
            model.setConference_location("Floor");
            model.setConference_time("Today");
            //model.setPrepareThings(prepare);
            model.addPrepareThing("3333");
            models.add(model);

            model = new Model();
            model.setConference_name("Second Conference Name");
            model.setConference_location("Second Floor");
            model.setConference_time("Tomorrow");
            model.addPrepareThing("123121231231231233");
            models.add(model);
        }

        return models;
    }
}
