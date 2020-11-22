package com.example.conference_infinity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.conference_infinity.listview.MyListAdapter_Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    //View
     ListView Category_List;
     ListAdapter Category_List_Adapter;
    //end View

    //
    String[] Category_List_Data;
    //

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



        //Create List
        Category_List = view.findViewById(R.id.Category_List);
        Category_List.setAdapter(new MyListAdapter_Category(getActivity(), null));
        Category_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ConferencesActivity.class);
                intent.putExtra("title", Category_List_Data[position]);
                startActivity(intent);
                //getActivity().finish();
            }
        });

        //Update List
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Category");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String keys = dataSnapshot.getKey();
                if (dataSnapshot == null)
                    Toast.makeText(getActivity(), "Realtime database error: No data", Toast.LENGTH_LONG).show();
                else {
                    String[] listData = {};
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        listData = Arrays.copyOf(listData, listData.length + 1);
                        listData[listData.length - 1] = data.getKey();
                    }
                    UpdateCategoryData(listData);
                    Category_List.setAdapter(new MyListAdapter_Category(getActivity(), Category_List_Data));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getActivity(), "Realtime database error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void UpdateCategoryData(String[] strings){
        if (strings != null){
            Category_List_Data = strings.clone();
        }
        else {
            Category_List_Data = null;
        }
    }
}