package com.example.conference_infinity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import at.markushi.ui.CircleButton;

public class Fragment_Register_Topics extends Fragment {

    private static final String TAG = "Fragment Topics";
    private ChipGroup chipGroup;
    private final String CheckId = "";
    private List<String> topics;
    private String email, nickname, password, headPhotoUrl;
    private int language, theme;

    GlobalVariable user;
    private StorageReference storageReference;
    private StorageTask uploadTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create xml Layout (Same as Activity "OnCreate()")
        return inflater.inflate(R.layout.fragment_register_topic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Connect the button in xml, must add "view.findView" in Fragment
        CircleButton back_btn = view.findViewById(R.id.topics_back_btn);
        Button next_btn = view.findViewById(R.id.topics_next_btn);
        chipGroup = view.findViewById(R.id.chip_group);
        topics = new ArrayList<>();


        Log.d(TAG, "onCreateView: Started.");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getActivity(), "Going to density", Toast.LENGTH_SHORT).show();
                // navigate to the other fragment method
                ((Activity_Register_Register) getActivity()).setViewPager(new Root_Register_States().getFragmentNum(Root_Register_States.STATE_THEME));
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Going to all done", Toast.LENGTH_SHORT).show();

                // get tag
                for (int i = 0; i < chipGroup.getChildCount(); ++i) {
                    Chip chip2 = (Chip) chipGroup.getChildAt(i);

                    if (chip2.isChecked()) {
                        topics.add(chip2.getText().toString());
                    }
                }

                Log.d("Topics: ", topics.toString());

                getData();
                sendData();

                // navigate to the other fragment method
                ((Activity_Register_Register) getActivity()).setViewPager(new Root_Register_States().getFragmentNum(Root_Register_States.STATE_DONE));

                setLocale();
            }
        });

        //ChipGroup chipGroup = new ChipGroup(parentView.getContext());
        user = (GlobalVariable) getActivity().getApplicationContext();
        String[] genres = user.categoryPreview.clone();
        LayoutInflater inflater1 = LayoutInflater.from(getContext());
        for (String genre : genres) {
            Chip chip = (Chip) inflater1.inflate(R.layout.root_register_chip_item, null, false);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity(), "Chip clicked", Toast.LENGTH_SHORT).show();
                }
            });
            chip.setText(genre);
            chipGroup.addView(chip);
        }

        //Log.d("user: ", Category_List_Data.toString());
    }

    void register(String name, String email, String password, String imgURL) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> data = new HashMap<>();
        data.put("Name", name);
        data.put("Email", email);
        data.put("Password", password);
        data.put("HeadPhoto", imgURL);

        db.collection("User")
                .document(email)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserRegisterDone", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        // get data from device
                        editor.putString("Mail", email);
                        editor.putString("Password", password);
                        editor.apply();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    // TODO: 上傳使用者照片到FireStorage
    private void uploadImage() {
        storageReference = FirebaseStorage.getInstance().getReference("Uploads/Profile Picture/" + email);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if (user.headPhoto != null) {
            final StorageReference fileReference = storageReference.child(nickname + "_HeadPhoto.png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            user.headPhoto = Bitmap.createScaledBitmap( user.headPhoto
                    , 500
                    , user.headPhoto.getHeight() * 500 / user.headPhoto.getWidth()
                    , true
            );
            user.headPhoto.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            uploadTask = fileReference.putBytes(data);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    Log.d("---Debug---", "then");
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Log.d("---Debug---", "success");
                        Uri downloadUri = task.getResult();
                        headPhotoUrl = downloadUri.toString();

                        register(nickname, email, password, headPhotoUrl);
                        prefer(email, user.Language[language], user.Theme[theme], topics);

                        progressDialog.dismiss();
                    } else {
                        Log.d("---Debug---", "fail");
                        Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d("---Debug---", "onfail");
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            register(nickname, email, password, "N/A");
            prefer(email, user.Language[language], user.Theme[theme], topics);
            progressDialog.dismiss();
        }
    }

    void prefer(String email, String lang, String theme, List<String> categorys) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("Language", lang);
        user.put("Theme", theme);
        user.put("Category", categorys);

        db.collection("Preference")
                .document(email)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    // TODO: 取得前面設定資訊
    private void getData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserRegister", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // get data from device
        email = sharedPreferences.getString("Mail", null);
        nickname = sharedPreferences.getString("NickName", null);
        password = sharedPreferences.getString("Password", null);
        language = sharedPreferences.getInt("Language", 0);
        theme = sharedPreferences.getInt("Theme", 0);

        // delete part of data
        editor.remove("NickName");
        editor.remove("Language");
        editor.remove("Theme");
        editor.apply();
    }

    private void sendData() {
        uploadImage();
    }

    private void setLocale() {
        Locale locale = Locale.getDefault();
        //Log.d("----locale-----", locale.toString());
        if (!user.preferLangCode.equals("N/A")) {
            if (user.preferLangCode.equals(user.Language[1])) {
                locale = Locale.TRADITIONAL_CHINESE;
            } else if (user.preferLangCode.equals(user.Language[0])) {
                locale = Locale.US;
            }
        }
        Locale.setDefault(locale);
        Configuration config = getActivity().getBaseContext().getResources().getConfiguration();
        overwriteConfigurationLocale(config, locale);
    }

    private void overwriteConfigurationLocale(Configuration config, Locale locale) {
        config.locale = locale;
        getActivity().getBaseContext().getResources()
                .updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }

}
