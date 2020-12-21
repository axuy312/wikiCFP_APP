package com.example.conference_infinity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Home_Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Home_Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //Fragment
    private Fragment_Home_Latest fragment_home_latest;
    private Fragment_Home_Following fragment_home_following;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SearchView Conference_search;
    GlobalVariable gv;
    String lang;
    TextView home_home_title;

    public Fragment_Home_Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Home_Home newInstance(String param1, String param2) {
        Fragment_Home_Home fragment = new Fragment_Home_Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        tabLayout = getActivity().findViewById(R.id.hom_tablayout);
        viewPager = getActivity().findViewById(R.id.home_viewpager);
        Conference_search = getActivity().findViewById(R.id.home_searchview);
        gv = (GlobalVariable) getActivity().getApplicationContext();
        lang = gv.preferLangCode;
        home_home_title = getActivity().findViewById(R.id.home_home_title);
        //Add Fragment
        fragment_home_latest = new Fragment_Home_Latest();
        fragment_home_following = new Fragment_Home_Following();

        tabLayout.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fragment_home_latest.refreshData();
                    fragment_home_latest.RefreshListView(Conference_search.getQuery().toString());
                } else {
                    fragment_home_following.refreshData();
                    fragment_home_following.RefreshListView(Conference_search.getQuery().toString());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        Fragment_Home_Home.ViewPagerAdapter viewPagerAdapter = new Fragment_Home_Home.ViewPagerAdapter(getActivity().getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragment_home_latest, getText(R.string.trend).toString());
        viewPagerAdapter.addFragment(fragment_home_following, getText(R.string.following).toString());
        //viewPager.setAdapter(null);

        if (viewPager.getAdapter() != null) {
            viewPager.getAdapter().notifyDataSetChanged();
            //viewPager.invalidate();
        } else {
            viewPager.setAdapter(viewPagerAdapter);
        }

        Conference_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fragment_home_latest.ScrollTop();
                fragment_home_following.ScrollTop();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fragment_home_latest.RefreshListView(newText);
                fragment_home_following.RefreshListView(newText);
                return false;
            }
        });

        // save user preference
        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();

        editor1.putString("Account", gv.userEmail);
        editor1.putString("Password", gv.userPassword);
        editor1.apply();

    }

    public void ScrollTop() {
        fragment_home_latest.ScrollTop();
        fragment_home_following.ScrollTop();
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);

        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!lang.equals(gv.preferLangCode)) {
            home_home_title.setText(getText(R.string.Conference));
            lang = gv.preferLangCode;
        }

    }
}