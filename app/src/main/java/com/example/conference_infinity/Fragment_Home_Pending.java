package com.example.conference_infinity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Home_Pending#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Home_Pending extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    GlobalVariable user;
    String lang;
    TextView home_pending_title;

    public Fragment_Home_Pending() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PendingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Home_Pending newInstance(String param1, String param2) {
        Fragment_Home_Pending fragment = new Fragment_Home_Pending();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_pending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        user = (GlobalVariable) getActivity().getApplicationContext();
        home_pending_title = view.findViewById(R.id.home_pending_title);
        lang = user.preferLangCode;
        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!lang.equals(user.preferLangCode)) {
            home_pending_title.setText(getText(R.string.pending_conference));
            lang = user.preferLangCode;
        }
    }

    public void refresh() {
        if (user != null && recyclerView != null) {
            // 資料由getMyList產生，再由adapter產生
            myAdapter = new MyAdapter(getActivity(), (ArrayList<Model>) user.getPendingConference().clone());
            // 設定recycleView的adapter
            recyclerView.setAdapter(myAdapter);

            // 判斷是否為空的 提示文字
            if (user.getPendingConference().size() == 0) {
                recyclerView.setVisibility(View.GONE);
                TextView empty_conference_tag = getActivity().findViewById(R.id.empty_conference_tag);
                empty_conference_tag.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
}