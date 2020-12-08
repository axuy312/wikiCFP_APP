package com.example.conference_infinity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_Register_Done extends Fragment {

    private static final String TAG = "Fragment All Done";

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
                Intent intent = new Intent(getActivity(), Activity_Home_Home.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
