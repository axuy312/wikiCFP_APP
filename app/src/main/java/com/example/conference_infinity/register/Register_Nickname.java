package com.example.conference_infinity.register;

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

import com.example.conference_infinity.R;
import com.example.conference_infinity.RegisterActivity;

import java.util.Objects;

import at.markushi.ui.CircleButton;

public class Register_Nickname extends Fragment {

    private static final String TAG = "Fragment Nickname";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create xml Layout (Same as Activity "OnCreate()")
        View view = inflater.inflate(R.layout.register_nickname_layout, container, false);

        Log.d(TAG, "onCreateView: Started.");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(view!= null)
        {
            // Connect the button in xml, must add "view.findView" in Fragment
            CircleButton back_btn = view.findViewById(R.id.nickname_back_btn);
            Button next_btn = getView().findViewById(R.id.nickname_next_btn);
            EditText nickname = getView().findViewById(R.id.input_nickname);

            back_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(getActivity(), "Going to nickname", Toast.LENGTH_SHORT).show();
                    // navigate to the other fragment method
                    ((RegisterActivity) getActivity()).setViewPager(new States().getFragmentNum(States.STATE_MAIL));
                }
            });


            next_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity(), "Going to password", Toast.LENGTH_SHORT).show();
                    // navigate to the other fragment method
                    ((RegisterActivity) getActivity()).setViewPager(new States().getFragmentNum(States.STATE_PASSWORD));
                }
            });
        }
    }

}
