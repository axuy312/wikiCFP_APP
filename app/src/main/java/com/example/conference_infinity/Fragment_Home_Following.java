package com.example.conference_infinity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Home_Following#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Home_Following extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyListAdapter_Conference Conference_List_Adapter;

    GlobalVariable db;
    HashMap<String, String>[] Conference_List_Data;

    ListView Conference_List;

    public Fragment_Home_Following() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Home_Following.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Home_Following newInstance(String param1, String param2) {
        Fragment_Home_Following fragment = new Fragment_Home_Following();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.d("---Resume---", "---");
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
        return inflater.inflate(R.layout.fragment__home__following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Log.d("onViewCreated-------","-----");
        db = (GlobalVariable) getActivity().getApplicationContext();

        //Create List
        Conference_List = view.findViewById(R.id.Home_Following_Conference_List);
        //Update List

        refreshData();
    }

    void ScrollTop() {
        if (Conference_List != null) {
            Conference_List.setSelection(0);
        }
    }

    void RefreshListView(String newText) {
        if (Conference_List_Adapter != null) {
            Conference_List_Adapter.getFilter().filter(newText);
        }
    }

    public void refreshData() {

        if (db != null && db.conferences != null && getActivity() != null) {

            List<HashMap<String, String>> tmpList = new ArrayList<>();

            if (db.followingConference != null) {
                for (String abbr : db.followingConference.keySet().toArray(new String[0])) {
                    if (db.followingConference.get(abbr)) {
                        tmpList.add((HashMap<String, String>) db.conferences.get(abbr));
                    }
                }
            }

            Conference_List_Data = tmpList.toArray(new HashMap[0]);


            if (Conference_List_Adapter == null) {
                Conference_List_Adapter = new MyListAdapter_Conference(getActivity(), Conference_List_Data);
                Conference_List.setAdapter(Conference_List_Adapter);
            } else {
                //Log.d("---newData----",String.valueOf(Conference_List_Data.length));
                Conference_List_Adapter.refresh(Conference_List_Data, true);
                Conference_List_Adapter.notifyDataSetChanged();
            }
        }
    }
}