package com.example.conference_infinity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import at.markushi.ui.CircleButton;

public class Fragment_Register_Password extends Fragment {

    private static final String TAG = "Fragment Password";
    private EditText password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create xml Layout (Same as Activity "OnCreate()")
        View view = inflater.inflate(R.layout.fragment_register_password, container, false);

        // Connect the button in xml, must add "view.findView" in Fragment
        CircleButton back_btn = view.findViewById(R.id.password_back_btn);
        Button next_btn = view.findViewById(R.id.password_next_btn);
        password = view.findViewById(R.id.input_password);

        //Log.d(TAG, "onCreateView: Started.");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getActivity(), "Going to nickname", Toast.LENGTH_SHORT).show();
                // navigate to the other fragment method
                ((Activity_Register_Register) getActivity()).setViewPager(new Root_Register_States().getFragmentNum(Root_Register_States.STATE_NICKNAME));
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Going to language", Toast.LENGTH_SHORT).show();
                if (!password.getText().toString().isEmpty()) {

                    if (password.getText().length() > 5 && password.getText().length() < 25) {
                        saveData();
                        // navigate to the other fragment method
                        ((Activity_Register_Register) getActivity()).setViewPager(new Root_Register_States().getFragmentNum(Root_Register_States.STATE_UPLOADIMG));
                    } else {
                        password.setError("Password length no correct");
                    }

                } else {
                    password.setError("Password cannot be Empty!");
                }
            }
        });

        return view;
    }

    private void saveData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserRegister", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Password", password.getText().toString());
        editor.apply();
    }
}
