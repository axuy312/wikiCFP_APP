package com.example.conference_infinity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import at.markushi.ui.CircleButton;

public class Fragment_Register_Nickname extends Fragment {

    private static final String TAG = "Fragment Nickname";
    private EditText nickname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create xml Layout (Same as Activity "OnCreate()")
        View view = inflater.inflate(R.layout.fragment_register_nickname, container, false);

        Log.d(TAG, "onCreateView: Started.");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (view != null) {
            // Connect the button in xml, must add "view.findView" in Fragment
            CircleButton back_btn = view.findViewById(R.id.nickname_back_btn);
            Button next_btn = getView().findViewById(R.id.nickname_next_btn);
            nickname = getView().findViewById(R.id.input_nickname);

            back_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(getActivity(), "Going to nickname", Toast.LENGTH_SHORT).show();
                    // navigate to the other fragment method
                    ((Activity_Register_Register) getActivity()).setViewPager(new Root_Register_States().getFragmentNum(Root_Register_States.STATE_MAIL));
                }
            });


            next_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity(), "Going to password", Toast.LENGTH_SHORT).show();
                    if(!nickname.getText().toString().isEmpty())
                    {
                        saveData();

                        // navigate to the other fragment method
                        ((Activity_Register_Register) getActivity()).setViewPager(new Root_Register_States().getFragmentNum(Root_Register_States.STATE_PASSWORD));
                    }
                    else
                    {
                        nickname.setError("Input a name");
                    }
                }
            });
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserRegister", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NickName", nickname.getText().toString());
        editor.apply();
    }
}
