package com.example.conference_infinity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.Map;

public class Fragment_Register_Done extends Fragment {

    private static final String TAG = "Fragment All Done";

    GlobalVariable user;
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create xml Layout (Same as Activity "OnCreate()")
        return inflater.inflate(R.layout.fragment_register_done, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Connect the button in xml, must add "view.findView" in Fragment
        Button next_btn = view.findViewById(R.id.done_next_btn);

        Log.d(TAG, "onCreateView: Started.");

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserRegisterDone", Context.MODE_PRIVATE);
                Login(sharedPreferences.getString("Mail", ""), sharedPreferences.getString("Password", ""));
            }
        });
    }

    void Login(String email, String password) {
        user = (GlobalVariable) getActivity().getApplicationContext();
        db = FirebaseFirestore.getInstance();
        db.collection("User")
                .document(email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> UserData = documentSnapshot.getData();
                        if (UserData != null && UserData.get("Password") != null && UserData.get("Password").equals(password)) {
                            user.loadUser(email, getActivity());
                            setLocale();
                        }
                    }
                });
    }

    private void setLocale() {
        Locale locale = Locale.getDefault();

        if (!user.preferLangCode.equals("N/A")) {
            if (user.preferLangCode.equals(user.Language[0])) {
                locale = Locale.TRADITIONAL_CHINESE;
            } else if (user.preferLangCode.equals(user.Language[1])) {
                locale = Locale.US;
            }
        }
        Log.d("----locale-----", locale.toString());
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
