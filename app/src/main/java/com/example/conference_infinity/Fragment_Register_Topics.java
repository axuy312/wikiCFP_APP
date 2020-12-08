package com.example.conference_infinity;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.markushi.ui.CircleButton;

public class Fragment_Register_Topics extends Fragment {

    private static final String TAG = "Fragment Topics";
    private ChipGroup chipGroup;
    private String CheckId = "";
    private List<String> topics;
    private String email, nickname, password;
    private String[] topic;
    private int language, theme, density;
    GlobalVariable user;

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
                ((Activity_Register_Register) getActivity()).setViewPager(new Root_Register_States().getFragmentNum(Root_Register_States.STATE_DENSITY));
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
                    Toast.makeText(getActivity(), "Chip clicked", Toast.LENGTH_SHORT).show();
                }
            });
            chip.setText(genre);
            chipGroup.addView(chip);
        }

        //Log.d("user: ", Category_List_Data.toString());
    }

    void register(String name, String email, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("Name", name);
        user.put("Email", email);
        user.put("Password", password);

        db.collection("User")
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

    private void getData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserRegister", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // get data from device
        email = sharedPreferences.getString("Mail", null);
        nickname = sharedPreferences.getString("NickName", null);
        password = sharedPreferences.getString("Password", null);
        language = sharedPreferences.getInt("Language", -1);
        theme = sharedPreferences.getInt("Theme", -1);
        density = sharedPreferences.getInt("Density", -1);

        // delete part of data
        editor.remove("Password");
        editor.remove("Language");
        editor.remove("Theme");
        editor.remove("Density");
        editor.apply();
    }

    private void sendData() {
        register(nickname, email, password);
        prefer(email, user.Language[language], user.Theme[theme], topics);
    }
}
