package com.example.conference_infinity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Home_Latest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Home_Latest extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyListAdapter_Conference Conference_List_Adapter;

    GlobalVariable db;
    HashMap<String,String>[] Conference_List_Data;

    ListView Conference_List;

    public Fragment_Home_Latest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Home_Latest.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Home_Latest newInstance(String param1, String param2) {
        Fragment_Home_Latest fragment = new Fragment_Home_Latest();
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
        return inflater.inflate(R.layout.fragment__home__latest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = (GlobalVariable)getActivity().getApplicationContext();

        //Create List
        Conference_List = view.findViewById(R.id.Home_Latest_Conference_List);
        //Update List

        refreshData();
    }

    void RefreshListView(String newText){
        if (Conference_List_Adapter != null){
            Conference_List_Adapter.getFilter().filter(newText);
        }
    }

    public void refreshData(){
        if (db.conferences != null && getActivity() != null){
            HashMap[] tmpHashmaps = db.conferences.values().toArray(new HashMap[0]);

            List<HashMap<String, String>>tmp = new ArrayList<>();
            for (HashMap hashMap : tmpHashmaps)
            {
                tmp.add(hashMap);
            }
            Collections.sort(tmp, new sortByDiscussCnt());


            Conference_List_Data = tmp.toArray(new HashMap[0]);

            if (Conference_List_Adapter == null){
                Conference_List_Adapter = new MyListAdapter_Conference(getActivity(), Conference_List_Data);
                Conference_List.setAdapter(Conference_List_Adapter);
            }
            else {
                Conference_List_Adapter.refresh(Conference_List_Data, false);
                Conference_List_Adapter.notifyDataSetChanged();
            }
        }
    }

    class sortByDiscussCnt implements Comparator<HashMap<String, String>>
    {
        //以book的ID升序排列
        public int compare(HashMap<String, String> a, HashMap<String, String> b)
        {
            if (db == null) {
                db = (GlobalVariable) getActivity().getApplicationContext();
            }

            String abbrA, abbrB;
            abbrA = a.get("Abbreviation");
            abbrB = b.get("Abbreviation");
            Long aLong, bLong;
            aLong = db.discussCnt.get(abbrA);
            bLong = db.discussCnt.get(abbrB);

            if (aLong == null){
                aLong = Long.valueOf(0);
            }
            if (bLong == null){
                bLong = Long.valueOf(0);
            }

            return (int) (bLong - aLong);
        }
    }

}