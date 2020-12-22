package com.example.conference_infinity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    HashMap<String, Long> discussCnt;
    //----------------------------------------------------------------------------------------------

    //User preference-------------------------------------------------------------------------------
    String preferLangCode = "Traditional";
    String preferThemeCode = "Light";
    HashMap<String, Boolean> preferCategory = null;
    HashMap<String, Boolean> followingConference = null;
    HashMap<String, Object> pendingConference = null;
    boolean pendingChange = false;
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
    HashMap<String, Bitmap> EmailToBitmap;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //----------------------------------------------------------------------------------------------

    //Pending Conference----------------------------------------------------------------------------
    ArrayList<Model_Pending> modelPendings = new ArrayList<>();
    //----------------------------------------------------------------------------------------------

    //firebase--------------------------------------------------------------------------------------
    FirebaseDatabase database;
    FirebaseFirestore db;
    //----------------------------------------------------------------------------------------------

    void initBitmapFromSharedPreferences() {
        if (EmailToBitmap == null) {
            EmailToBitmap = new HashMap<>();
        }
        sharedPreferences = getSharedPreferences("BitmapLoad", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String dataStr = sharedPreferences.getString("Email", "");
        //Log.d("----TAG----", "Share : " + dataStr);
        editor.remove("Email");
        editor.apply();
        HashMap<String, Boolean> tmp = new HashMap<>();
        for (String mail : dataStr.split(",")) {
            if (mail != null && !mail.isEmpty()) {
                tmp.put(mail, true);
            }
        }
        if (!tmp.isEmpty()) {
            String[] tmparr = tmp.keySet().toArray(new String[0]);
            for (String mail : tmparr) {
                if (mail != null && !mail.isEmpty()) {
                    //Log.d("----TAG----", "Share : " + dataStr);
                    storeBitmap(mail);
                }
            }
        }
    }

    void setRealtime() {
        ValueEventListener valueEventListener;
        database = FirebaseDatabase.getInstance();
        refreshDiscussCnt();

        DatabaseReference myRefConference = database.getReference("Conference");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                UpdateConferences((HashMap) dataSnapshot.getValue());

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

                UpdateCategorys((HashMap) dataSnapshot.getValue());

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

    void loadUser(String Email, Activity activity) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserRegisterDone", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            // delete part of data
            editor.remove("Password");
            editor.remove("Mail");
            editor.apply();
        }

        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }

        if (pendingConference == null) {
            pendingConference = new HashMap<>();
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

                        // set theme
                        if (!preferThemeCode.equals("N/A")) {
                            if (preferThemeCode.equals(Theme[0])) {
                                // light theme
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            } else if (preferThemeCode.equals(Theme[1])) {
                                // dark theme
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            }
                        }

                        UpdatePreferCategorys((List<String>) UserData.get("Category"), (List<String>) UserData.get("Following Conference"), (HashMap<String, Object>) UserData.get("Pending Conference"));
                        UpdataFollowCategory();

                        Intent intent = new Intent(activity, Activity_Home_Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        activity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("----TAG----", "Error getting documents : " + e.getMessage());
                    }
                });


        //Load following
    }


    void clearUserData() {
        preferLangCode = "Traditional";
        preferThemeCode = "Light";
        if (preferCategory != null) {
            preferCategory.clear();
            preferCategory = null;
        }
        if (followingConference != null) {
            followingConference.clear();
            followingConference = null;
        }
        if (pendingConference != null) {
            pendingConference.clear();
            pendingConference = null;
        }
        pendingChange = false;
        userName = "N/A";
        userPassword = "N/A";
        userEmail = "N/A";
        headPhoto = null;
        headPhotoURL = "N/A";
        following_categoryPreview = null;

        // clear user sharedPreference
        SharedPreferences sharedPreferences1 = getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();

        editor1.remove("Account");
        editor1.remove("Password");
        editor1.apply();
    }


    void UpdateDiscussCnt(String abbr, long cnt) {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
        }
        DatabaseReference myRef = database.getReference("DiscussCnt/" + abbr + "/");
        myRef.setValue(cnt);
    }

    void refreshDiscussCnt() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
        }
        DatabaseReference myRefDiscussCnt = database.getReference("DiscussCnt");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (discussCnt == null) {
                    discussCnt = new HashMap<>();
                }
                if (dataSnapshot != null && dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        discussCnt.put(data.getKey(), (Long) data.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        };
        myRefDiscussCnt.addValueEventListener(valueEventListener);
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

    void UpdatePreferCategorys(List<String> categ, List<String> conf, HashMap<String, Object> pend) {
        if (categ != null) {
            preferCategory = new HashMap<String, Boolean>();
            for (String t : categ) {
                preferCategory.put(t, true);
            }
        }
        if (conf != null) {
            followingConference = new HashMap<String, Boolean>();
            for (String t : conf) {
                followingConference.put(t, true);
            }
        }
        if (pend != null) {
            pendingConference = new HashMap<String, Object>();
            for (String abbr : pend.keySet().toArray(new String[0])) {
                pendingConference.put(abbr, new HashMap());
                ((HashMap<String, Object>) pendingConference.get(abbr)).put("Attend", ((HashMap<String, Object>) pend.get(abbr)).get("Attend"));
                HashMap<String, Boolean> tmpPrepare = new HashMap<>();
                HashMap<String, Boolean> prepare = ((HashMap) ((HashMap<String, Object>) pend.get(abbr)).get("Prepare"));
                for (String prepareKey : prepare.keySet().toArray(new String[0])) {
                    tmpPrepare.put(prepareKey, prepare.get(prepareKey));
                }
                ((HashMap<String, Object>) pendingConference.get(abbr)).put("Prepare", tmpPrepare);
            }
            //Log.d("---Pend---", pendingConference.toString());
        }
    }

    //Updata & Upload
    boolean UpdatePreferCategorysValue(String title, Boolean bool) {
        if (preferCategory == null) {
            preferCategory = new HashMap<>();
        }
        preferCategory.put(title, bool);
        //Log.d("---TAG---", title + " : " + bool.toString());

        List<String> data = new ArrayList<>();
        for (String key : preferCategory.keySet().toArray(new String[0])) {
            if (preferCategory.get(key) == true) {
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
                        //Log.d("TAG", "Len: " + String.valueOf(data.size()));
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

    //Updata & Upload Conference follow
    boolean UpdateFollowingConferencesValue(String title, Boolean bool) {
        if (followingConference == null) {
            followingConference = new HashMap<>();
        }


        followingConference.put(title, bool);
        //Log.d("---TAG---", title + " : " + bool.toString());

        List<String> data = new ArrayList<>();
        for (String key : followingConference.keySet().toArray(new String[0])) {
            if (followingConference.get(key) == true) {
                data.add(key);
            }
        }


        db.collection("Preference")
                .document(userEmail)
                .update("Following Conference", data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d("TAG", "Len: " + String.valueOf(data.size()));
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

    //Updata & Upload Attend Conference
    boolean UpdateAttendConferencesValue(String title, Boolean attend, String prepare, Boolean ready) {
        if (pendingConference == null) {
            pendingConference = new HashMap<>();

        }


        if (pendingConference.get(title) == null) {
            pendingConference.put(title, new HashMap<String, Object>());
        }

        ((HashMap) pendingConference.get(title)).put("Attend", attend);

        if (((HashMap) pendingConference.get(title)).get("Prepare") == null) {
            ((HashMap) pendingConference.get(title)).put("Prepare", new HashMap<String, Boolean>());
        }

        if (prepare != null && !prepare.isEmpty() && !prepare.equals("N/A")) {
            ((HashMap) ((HashMap) pendingConference.get(title)).get("Prepare")).put(prepare, ready);
        }


        db.collection("Preference")
                .document(userEmail)
                .update("Pending Conference", pendingConference)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d("TAG", "Len: " + String.valueOf(pendingConference.size()));
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

    void storeBitmap(String email) {
        if (email != null && (EmailToBitmap == null || EmailToBitmap.get(email) == null)) {
            if (db == null) {
                db = FirebaseFirestore.getInstance();
            }

            db.collection("User")
                    .document(email)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Map<String, Object> UserData = documentSnapshot.getData();
                            String url = UserData.get("HeadPhoto").toString();
                            if (url != null && !url.equals("N/A") && EmailToBitmap.get(email) == null) {
                                //Log.d("----TAG----", "Exist : " + url);
                                new GetBitmap().execute(url, email);
                            }
                            //Log.d("----TAG----", "here : " + url);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.d("----TAG----", "Error getting documents : " + e.getMessage());
                        }
                    });

        }
    }

    void UpdataUser(String name, String password, String email, String imgUrl) {
        userName = name;
        userPassword = password;
        userEmail = email;
        if (imgUrl != null && !imgUrl.equals("N/A") && !imgUrl.isEmpty()) {
            new GetBitmap().execute(imgUrl, null);
        }
    }

    void UpdataFollowCategory() {
        if (preferCategory == null) {
            return;
        }

        following_categoryPreview = new String[categoryPreview.length];


        int cnt = 0;


        for (int i = 0; i < categoryPreview.length; i++) {
            if (preferCategory.get(categoryPreview[i]) != null && preferCategory.get(categoryPreview[i]) == true) {
                following_categoryPreview[cnt] = categoryPreview[i];
                cnt++;
            }
        }


        for (int i = 0; i < categoryPreview.length; i++) {
            if (preferCategory.get(categoryPreview[i]) == null || preferCategory.get(categoryPreview[i]) == false) {

                following_categoryPreview[cnt] = categoryPreview[i];
                cnt++;
            }
        }

    }

    private class GetBitmap extends AsyncTask<String, Integer, Bitmap> {
        String urlStr, mail;

        @Override
        protected void onPreExecute() {
            //執行前
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //執行中

            urlStr = params[0];
            if (params.length == 1 || params[1] == null || params[1].equals("")) {
                mail = null;
            } else {
                mail = params[1];
            }
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
            if (mail == null || mail.isEmpty()) {
                //Log.d("----TAG----", "Fun1");
                headPhoto = bitmap;
                headPhotoURL = urlStr;
                if (EmailToBitmap == null) {
                    EmailToBitmap = new HashMap<>();
                }
                //Log.d("----TAG----", "new img");
                //Log.d("----TAG----", "failed img");
                EmailToBitmap.put(userEmail, bitmap);
            } else {
                //Log.d("----TAG2----", "Fun2");
                if (EmailToBitmap == null) {
                    EmailToBitmap = new HashMap<>();
                }
                if (bitmap != null) {
                    EmailToBitmap.put(mail, bitmap);

                    sharedPreferences = getSharedPreferences("BitmapLoad", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();

                    String data = sharedPreferences.getString("Email", "");
                    //Log.d("----TAG2----", "Data add" + mail);
                    if (data == null || data.isEmpty()) {
                        data = mail;
                    } else {
                        data = data + "," + mail;
                    }
                    editor.putString("Email", data);
                    editor.apply();
                    //Log.d("----TAG2----", "Result: " + sharedPreferences.getString("Email", ""));
                } else {
                    EmailToBitmap.put(mail, null);
                }
            }
        }
    }

    // TODO: 取得 firebase 的 pending conference :Attend == false -> no display
    public ArrayList<Model_Pending> getPendingConference() {
        if (pendingConference == null) {
            pendingConference = new HashMap<>();
        }
        String[] abbrconf = pendingConference.keySet().toArray(new String[0]);
        if (modelPendings == null) {
            modelPendings = new ArrayList<>();
        } else {
            modelPendings.clear();
        }
        Model_Pending modelPending;

        for (String abbr : abbrconf) {
            if (!(Boolean) ((HashMap<String, Object>) pendingConference.get(abbr)).get("Attend")) {
                continue;
            }
            HashMap<String, String> conference = (HashMap<String, String>) conferences.get(abbr);
            modelPending = new Model_Pending();
            modelPending.setConference_name(conference.get("Topic"));
            modelPending.setAbbr(abbr);
            modelPending.setConference_location(conference.get("Where"));
            modelPending.setConference_time(conference.get("Submission Deadline"));

            HashMap<String, Object> tmpConf = (HashMap<String, Object>) pendingConference.get(abbr);
            HashMap<String, Boolean> tmpPrepare = (HashMap<String, Boolean>) tmpConf.get("Prepare");
            modelPending.setAttend((Boolean) tmpConf.get("Attend"));
            modelPending.setPrepareThings(tmpPrepare);
            modelPendings.add(modelPending);

        }

        return modelPendings;
    }


}
