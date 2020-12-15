package com.example.conference_infinity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.conference_infinity.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MyListAdapter_Conference extends BaseAdapter implements Filterable {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private List<HashMap<String, String>> datas;
    private List<HashMap<String, String>> datas_All;

    HashMap<String , Boolean>following;

    GlobalVariable user;

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<HashMap<String, String>> filter_title = new ArrayList<>();

            if (constraint.toString().isEmpty()){
                filter_title.addAll(datas_All);
            }
            else {
                for (HashMap<String, String> map : datas_All){
                    if (map.get("Topic").toLowerCase().contains(constraint.toString().toLowerCase())){
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
            datas.clear();
            datas.addAll((Collection<? extends HashMap<String, String>>) results.values);
            notifyDataSetChanged();
        }
    };


    public MyListAdapter_Conference(Context context, HashMap[] dictionaries){
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        datas = new ArrayList<>();
        datas_All = new ArrayList<>();
        user = (GlobalVariable)mContext.getApplicationContext();
        following = new HashMap<>();

        if (user.followingConference != null){
            String[] keys = user.followingConference.keySet().toArray(new String[0]);
            if (keys != null){
                for (String key : keys){
                    if (user.followingConference.get(key) == true){
                        following.put(key, true);
                    }
                }
            }
        }


        if(dictionaries != null){
            for (HashMap<String, String>map : dictionaries){
                datas.add((HashMap<String, String>) map.clone());
                datas_All.add((HashMap<String, String>) map.clone());
            }
        }
    }

    @Override
    public int getCount() {
        return datas.size();
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
        holder.conference_item_title.setText(datas.get(position).get("Topic"));
        holder.conference_item_deadline.setText(datas.get(position).get("Deadline"));
        holder.comment_number.setText(String.valueOf(position));

        holder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Activity_Article.class);
                intent.putExtra("Topic", datas.get(position).get("Topic"));
                intent.putExtra("Abbreviation", datas.get(position).get("Abbreviation"));
                mContext.startActivity(intent);
            }
        });

        holder.tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (following.get(datas.get(position).get("Abbreviation")) == null || following.get(datas.get(position).get("Abbreviation")) == false){
                    if (user.UpdateFollowingConferencesValue(datas.get(position).get("Abbreviation"), true)){
                        following.put(datas.get(position).get("Abbreviation"), true);
                        ((ImageView)v).setImageResource(R.drawable.ic_bookmark_on);
                    }
                }
                else {
                    if (user.UpdateFollowingConferencesValue(datas.get(position).get("Abbreviation"), false)) {
                        following.put(datas.get(position).get("Abbreviation"), false);
                        ((ImageView) v).setImageResource(R.drawable.ic_bookmark_off);
                    }
                }
            }
        });

        if (following.get(datas.get(position).get("Abbreviation")) == null || following.get(datas.get(position).get("Abbreviation")) == false){
            holder.tag.setImageResource(R.drawable.ic_bookmark_off);
        }
        else {
            holder.tag.setImageResource(R.drawable.ic_bookmark_on);
        }

        return convertView;
    }
}
