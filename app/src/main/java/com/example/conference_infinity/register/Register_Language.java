package com.example.conference_infinity.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.conference_infinity.HomeActivity;
import com.example.conference_infinity.R;
import com.example.conference_infinity.RegisterActivity;

import at.markushi.ui.CircleButton;

public class Register_Language extends Fragment {

    private static final String TAG = "Fragment Language";
    private Button Language_ZH, Language_EN;
    private int lang_index = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create xml Layout (Same as Activity "OnCreate()")
        View view = inflater.inflate(R.layout.register_language_layout, container, false);

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
                ((RegisterActivity) getActivity()).setViewPager(new States().getFragmentNum(States.STATE_PASSWORD));
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Going to theme", Toast.LENGTH_SHORT).show();
                // navigate to the other fragment method
                ((RegisterActivity) getActivity()).setViewPager(new States().getFragmentNum(States.STATE_THEME));
            }
        });

        Language_EN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang_index = 0;
                saveData();
                setLanguage();
                //((RegisterActivity) getActivity()).setViewPager(new States().getFragmentNum(States.STATE_THEME));
            }
        });

        Language_ZH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang_index = 1;
                saveData();
                setLanguage();
                //((RegisterActivity) getActivity()).setViewPager(new States().getFragmentNum(States.STATE_THEME));
            }
        });

        return view;
    }

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

    private void setLanguage()
    {
        if(lang_index == 0)
        {
            Language_EN.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_en_us), null, getResources().getDrawable(R.drawable.ic_done), null);
            Language_ZH.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_zh_tw), null, null, null);
        }
        else if(lang_index == 1)
        {
            Language_EN.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_en_us), null, null, null);
            Language_ZH.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_zh_tw), null, getResources().getDrawable(R.drawable.ic_done), null);
        }
    }
}
