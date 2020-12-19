package com.example.conference_infinity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MyListAdapter_Conference extends BaseAdapter implements Filterable {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private HashMap<String, HashMap<String, String>> datas;
    private HashMap<String, HashMap<String, String>> datas_All;
    private List<String>titles;
    private List<String>titles_All;

    HashMap<String , Boolean>following;

    GlobalVariable user;

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<String> filter_title = new ArrayList<>();

            if (constraint.toString().isEmpty()){
                filter_title.addAll(titles_All);
            }
            else {
                for (String map : titles_All){
                    if (map.toLowerCase().contains(constraint.toString().toLowerCase())){
                        filter_title.add(map);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filter_title;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            titles.clear();
            titles.addAll((Collection<? extends String>) results.values);
            notifyDataSetChanged();
        }
    };



    public MyListAdapter_Conference(Context context, HashMap[] dictionaries){
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

        user = (GlobalVariable)mContext.getApplicationContext();

        refresh(dictionaries, true);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    static class ViewHolder{
        public TextView conference_item_title, conference_item_deadline, comment_number;
        public ImageView tag;
        public LinearLayout body;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.conference_list_item, null);
            holder = new ViewHolder();
            holder.conference_item_title = convertView.findViewById(R.id.conference_item_title);
            holder.conference_item_deadline = convertView.findViewById(R.id.conference_item_deadline);
            holder.comment_number = convertView.findViewById(R.id.comment_number);
            holder.body = convertView.findViewById(R.id.conference_item_body);
            holder.tag = convertView.findViewById(R.id.conference_bookmark);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }


        String abbr = titles.get(position);

        holder.conference_item_title.setText(datas.get(abbr).get("Topic"));

        if (datas.get(abbr).get("Submission Deadline") == null && user.conferences.get(abbr) != null){
            holder.conference_item_deadline.setText(((HashMap<String,String>)user.conferences.get(abbr)).get("Submission Deadline"));
        }
        else {
            holder.conference_item_deadline.setText(datas.get(abbr).get("Submission Deadline"));
        }

        Long discussCnt = null;
        if (user.discussCnt != null){
            discussCnt = user.discussCnt.get(abbr);
        }
        if (discussCnt != null){
            holder.comment_number.setText(discussCnt.toString());
        }
        else {
            holder.comment_number.setText("0");
        }


        holder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Activity_Article.class);
                intent.putExtra("Topic", datas.get(titles.get(position)).get("Topic"));
                intent.putExtra("Abbreviation", titles.get(position));
                mContext.startActivity(intent);
            }
        });

        holder.tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (following.get(titles.get(position)) == null || following.get(titles.get(position)) == false){
                    if (user.UpdateFollowingConferencesValue(titles.get(position), true)){
                        following.put(titles.get(position), true);
                        ((ImageView)v).setImageResource(R.drawable.ic_bookmark_on);
                    }
                }
                else {
                    if (user.UpdateFollowingConferencesValue(titles.get(position), false)) {
                        following.put(titles.get(position), false);
                        ((ImageView) v).setImageResource(R.drawable.ic_bookmark_off);
                    }
                }
            }
        });

        if (following.get(abbr) == null || following.get(abbr) == false){
            holder.tag.setImageResource(R.drawable.ic_bookmark_off);
        }
        else {
            holder.tag.setImageResource(R.drawable.ic_bookmark_on);
        }

        return convertView;
    }

    void refresh(HashMap[] dictionaries, boolean refreshAllData){
        if (refreshAllData){
            if (datas != null){
                datas.clear();
            }
            if (datas_All != null){
                datas_All.clear();
            }
            if (following != null){
                following.clear();
            }
            datas = new HashMap<>();
            datas_All = new HashMap<>();
            following = new HashMap<>();
        }
        else {
            if (datas == null) {
                datas = new HashMap<>();
            }
            if (datas_All == null) {
                datas_All = new HashMap<>();
            }
            if (following == null) {
                following = new HashMap<>();
            }
        }
        if (titles != null) {
            titles.clear();
        }
        if (titles_All != null) {
            titles_All.clear();
        }

        titles = new ArrayList<>();
        titles_All = new ArrayList<>();

        if (user.followingConference != null){
            String[] keys = user.followingConference.keySet().toArray(new String[0]);
            if (keys != null){
                for (String key : keys){
                    if (user.followingConference.get(key) == true) {
                        following.put(key, user.followingConference.get(key));
                    }
                }
            }
        }


        if(dictionaries != null){
            for (HashMap<String, String>map : dictionaries){
                if (map != null && following.get(map.get("Abbreviation")) != null && following.get(map.get("Abbreviation")) == true){
                    titles.add(map.get("Abbreviation"));
                    titles_All.add(map.get("Abbreviation"));
                    if (refreshAllData){
                        datas.put(map.get("Abbreviation"), (HashMap<String, String>) map.clone());
                        datas_All.put(map.get("Abbreviation"), (HashMap<String, String>) map.clone());
                    }
                }
            }
            for (HashMap<String, String>map : dictionaries){
                if (map != null && (following.get(map.get("Abbreviation")) == null || following.get(map.get("Abbreviation")) == false)){
                    titles.add(map.get("Abbreviation"));
                    titles_All.add(map.get("Abbreviation"));
                    if (refreshAllData){
                        datas.put(map.get("Abbreviation"), (HashMap<String, String>) map.clone());
                        datas_All.put(map.get("Abbreviation"), (HashMap<String, String>) map.clone());
                    }
                }
            }
        }
    }
}
