package com.example.conference_infinity.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.conference_infinity.LoginActivity;
import com.example.conference_infinity.R;
import com.example.conference_infinity.RegisterActivity;

import at.markushi.ui.CircleButton;

public class Register_Mail extends Fragment {

    private static final String TAG = "Fragment Mail";
    private EditText mail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create xml Layout (Same as Activity "OnCreate()")
        View view = inflater.inflate(R.layout.register_mail_layout, container, false);

        // Connect the button in xml, must add "view.findView" in Fragment
        CircleButton back_btn = view.findViewById(R.id.mail_back_btn);
        Button next_btn = view.findViewById(R.id.mail_next_btn);
        mail = view.findViewById(R.id.input_mail);

        Log.d(TAG, "onCreateView: Started.");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Go to login Activity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mail.getText().toString().isEmpty()) {
                    // Check Email invalid
                    if (Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches()) {
                        // Save input data
                        saveData();
                        //Toast.makeText(getActivity(), "Going to nickname", Toast.LENGTH_SHORT).show();
                        // navigate to the other fragment method
                        ((RegisterActivity) getActivity()).setViewPager(1);
                    } else {
                        Toast.makeText(getActivity(), "Please input current Email", Toast.LENGTH_SHORT).show();
                        mail.setError("Email Pattern Invalidate");
                    }

                } else {
                    Toast.makeText(getActivity(), "Please input current Email", Toast.LENGTH_SHORT).show();
                    mail.setError("Email Invalidate");
                }
            }
        });

        return view;
    }

    private void saveData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserRegister", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Mail", mail.getText().toString());
        editor.apply();
    }
}
