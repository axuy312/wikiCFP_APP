package com.example.conference_infinity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Home_Category#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Home_Category extends Fragment {

    //View
     ListView Category_List;
     SearchView Category_search;
     MyListAdapter_Category Category_List_Adapter;
    //end View

    //
    String[] Category_List_Data;
    GlobalVariable user;
    //

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Home_Category() {
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
    public static Fragment_Home_Category newInstance(String param1, String param2) {
        Fragment_Home_Category fragment = new Fragment_Home_Category();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        user = (GlobalVariable)getActivity().getApplicationContext();

        Category_search = view.findViewById(R.id.Search_Category);

        //Create List
        Category_List = view.findViewById(R.id.Category_List);
        /*Category_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Activity_Conferences.class);
                intent.putExtra("title", user.categoryPreview[position]);
                startActivity(intent);
                //getActivity().finish();
            }
        });*/

        if (user.categoryPreview != null && getActivity() != null){
            Category_List_Adapter = new MyListAdapter_Category(getActivity(), user.following_categoryPreview, user.preferCategory.size());
            Category_List.setAdapter(Category_List_Adapter);
        }

        Category_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (Category_List_Adapter != null)
                {
                    Category_List_Adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }
}