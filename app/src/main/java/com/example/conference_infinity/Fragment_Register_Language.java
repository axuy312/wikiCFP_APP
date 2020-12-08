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

public class Fragment_Register_Language extends Fragment {

    private static final String TAG = "Fragment Language";
    private Button Language_ZH, Language_EN;
    private int lang_index = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create xml Layout (Same as Activity "OnCreate()")
        View view = inflater.inflate(R.layout.fragment_register_language, container, false);

        // Connect the button in xml, must add "view.findView" in Fragment
        CircleButton back_btn = view.findViewById(R.id.language_back_btn);
        Button next_btn = view.findViewById(R.id.language_next_btn);
        Language_EN = view.findViewById(R.id.input_language_en_us);
        Language_ZH = view.findViewById(R.id.input_language_zh_tw);

        loadData();

        Log.d(TAG, "onCreateView: Started.");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getActivity(), "Going to password", Toast.LENGTH_SHORT).show();
                // navigate to the other fragment method
                ((Activity_Register_Register) getActivity()).setViewPager(new Root_Register_States().getFragmentNum(Root_Register_States.STATE_PASSWORD));
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Going to theme", Toast.LENGTH_SHORT).show();
                // Check if user has input
                if(lang_index!=-1)
                {
                    // navigate to the other fragment method
                    ((Activity_Register_Register) getActivity()).setViewPager(new Root_Register_States().getFragmentNum(Root_Register_States.STATE_THEME));
                }
                else
                {
                    Toast.makeText(getActivity(),"Select one to choose Language.",Toast.LENGTH_SHORT).show();
                }

            }
        });

        Language_EN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang_index = 1;
                saveData();
                setLanguage();
                //((RegisterActivity) getActivity()).setViewPager(new States().getFragmentNum(States.STATE_THEME));
            }
        });

        Language_ZH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang_index = 0;
                saveData();
                setLanguage();
                //((RegisterActivity) getActivity()).setViewPager(new States().getFragmentNum(States.STATE_THEME));
            }
        });

        return view;
    }

    // add view

    private void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserRegister", Context.MODE_PRIVATE);

        lang_index = sharedPreferences.getInt("Language", -1);

        if(lang_index!=-1)
        {
            setLanguage();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserRegister", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Language", lang_index);
        editor.apply();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setLanguage()
    {
        if(lang_index == 1)
        {
            Language_EN.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_en_us), null, getResources().getDrawable(R.drawable.ic_done), null);
            Language_ZH.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_zh_tw), null, null, null);
        }
        else if(lang_index == 0)
        {
            Language_EN.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_en_us), null, null, null);
            Language_ZH.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_zh_tw), null, getResources().getDrawable(R.drawable.ic_done), null);
        }
    }
}
