package com.example.conference_infinity.register;

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

import com.example.conference_infinity.R;
import com.example.conference_infinity.RegisterActivity;

import at.markushi.ui.CircleButton;

public class Register_Theme extends Fragment {

    private static final String TAG = "Fragment Theme";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create xml Layout (Same as Activity "OnCreate()")
        View view = inflater.inflate(R.layout.register_theme_layout, container, false);

        // Connect the button in xml, must add "view.findView" in Fragment
        CircleButton back_btn = view.findViewById(R.id.theme_back_btn);
        Button next_btn = view.findViewById(R.id.theme_next_btn);

        Log.d(TAG, "onCreateView: Started.");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getActivity(), "Going to language", Toast.LENGTH_SHORT).show();
                // navigate to the other fragment method
                ((RegisterActivity) getActivity()).setViewPager(new States().getFragmentNum(States.STATE_LANGUAGE));
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Going to density", Toast.LENGTH_SHORT).show();
                // navigate to the other fragment method
                ((RegisterActivity) getActivity()).setViewPager(new States().getFragmentNum(States.STATE_DENSITY));
            }
        });

        return view;
    }
}
