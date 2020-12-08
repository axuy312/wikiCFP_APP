package com.example.conference_infinity;

import android.annotation.SuppressLint;
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

import at.markushi.ui.CircleButton;

public class Fragment_Register_Theme extends Fragment {

    private static final String TAG = "Fragment Theme";
    private Button Theme_light, Theme_dark;
    private int theme = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create xml Layout (Same as Activity "OnCreate()")
        View view = inflater.inflate(R.layout.fragment_register_theme, container, false);

        // Connect the button in xml, must add "view.findView" in Fragment
        CircleButton back_btn = view.findViewById(R.id.theme_back_btn);
        Button next_btn = view.findViewById(R.id.theme_next_btn);
        Theme_light = view.findViewById(R.id.input_theme_light);
        Theme_dark = view.findViewById(R.id.input_theme_dark);

        loadData();

        Log.d(TAG, "onCreateView: Started.");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getActivity(), "Going to language", Toast.LENGTH_SHORT).show();
                // navigate to the other fragment method
                ((Activity_Register_Register) getActivity()).setViewPager(new Root_Register_States().getFragmentNum(Root_Register_States.STATE_LANGUAGE));
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Going to density", Toast.LENGTH_SHORT).show();

                // check if user has choose theme
                if(theme!=-1)
                {
                    // navigate to the other fragment method
                    ((Activity_Register_Register) getActivity()).setViewPager(new Root_Register_States().getFragmentNum(Root_Register_States.STATE_TOPIC));
                }
                else
                {
                    Toast.makeText(getActivity(),"Select one to choose theme.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Theme_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme = 0;
                saveData();
                setTheme();
            }
        });

        Theme_dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme = 1;
                saveData();
                setTheme();
            }
        });
        return view;
    }

    private void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserRegister", Context.MODE_PRIVATE);

        theme = sharedPreferences.getInt("Language", -1);

        if (theme != -1) {
            setTheme();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserRegister", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Theme", theme);
        editor.apply();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setTheme() {
        if (theme == 0) {
            Theme_light.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_light), null, getResources().getDrawable(R.drawable.ic_done), null);
            Theme_dark.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_dark), null, null, null);
        } else if (theme == 1) {
            Theme_light.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_light), null, null, null);
            Theme_dark.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_dark), null, getResources().getDrawable(R.drawable.ic_done), null);
        }
    }
}
