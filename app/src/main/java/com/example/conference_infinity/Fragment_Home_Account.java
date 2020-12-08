package com.example.conference_infinity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Home_Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Home_Account extends Fragment {

    private CircleImageView user_photo;
    private TextView user_nickname;
    private Button preference_btn, history_btn, account_management_btn, logout_btn, about_btn;

    public Fragment_Home_Account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Home_Account newInstance(String param1, String param2) {
        Fragment_Home_Account fragment = new Fragment_Home_Account();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get id from fragment
        user_photo = view.findViewById(R.id.profile_image);
        user_nickname = view.findViewById(R.id.user_name);
        preference_btn = view.findViewById(R.id.preference_btn);
        history_btn = view.findViewById(R.id.history_conference_btn);
        account_management_btn = view.findViewById(R.id.account_management_btn);
        logout_btn = view.findViewById(R.id.logout_btn);
        about_btn = view.findViewById(R.id.about_btn);

        //onclick listener
        preference_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Setting_Preference.class);
                startActivity(intent);
            }
        });
        history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        account_management_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Login.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Setting_About.class);
                startActivity(intent);
            }
        });
    }
}