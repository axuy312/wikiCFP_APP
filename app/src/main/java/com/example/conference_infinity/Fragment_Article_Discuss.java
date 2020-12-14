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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Article_Discuss#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Article_Discuss extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String abbreviation;

    ListView discussListView;
    MyListAdapter_Discuss myListAdapter_discuss;

    FirebaseDatabase database;
    DatabaseReference myRef;

    public Fragment_Article_Discuss(String string) {
        // Required empty public constructor
        abbreviation = string;
    }

    public Fragment_Article_Discuss() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_article_discuss.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Article_Discuss newInstance(String param1, String param2) {
        Fragment_Article_Discuss fragment = new Fragment_Article_Discuss();
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
        return inflater.inflate(R.layout.fragment_article_discuss, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        discussListView = view.findViewById(R.id.dicussListView);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Discuss/"+abbreviation);

        myListAdapter_discuss = new MyListAdapter_Discuss(getActivity(), (List)(new ArrayList<HashMap>()));
        discussListView.setAdapter(myListAdapter_discuss);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot != null) {
                    List<HashMap<String,String>>discuss = new ArrayList<>();
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        HashMap<String,String>tmp = (HashMap<String, String>) childSnapshot.getValue();
                        discuss.add(tmp);
                    }

                    //myListAdapter_discuss.freshDiscuss(discuss);
                    myListAdapter_discuss.freshDiscuss(discuss);
                    myListAdapter_discuss.notifyDataSetChanged();
                    discussListView.setSelection(myListAdapter_discuss.getCount());
                }
                else {
                    Log.d("---Discuss---", "NULL dataSnapshot");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

}