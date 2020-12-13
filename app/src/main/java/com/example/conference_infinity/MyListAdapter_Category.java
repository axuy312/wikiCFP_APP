package com.example.conference_infinity;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
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

import com.example.conference_infinity.Activity_Article;
import com.example.conference_infinity.Activity_Conferences;
import com.example.conference_infinity.GlobalVariable;
import com.example.conference_infinity.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

public class MyListAdapter_Category extends BaseAdapter implements Filterable {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private List<String> titles;
    private List<String> titles_All;
    HashMap<String , Boolean>following;
    ViewHolder holder = null;


    GlobalVariable user;


    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<String> filter_title = new ArrayList<>();

            if (constraint.toString().isEmpty()){
                filter_title.addAll(titles_All);
            }
            else {
                for (String str : titles_All){
                    if (str.toLowerCase().contains(constraint.toString().toLowerCase())){
                        filter_title.add(str);
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



    public MyListAdapter_Category(Context context, String[] strings, int follow){
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

        user = (GlobalVariable)mContext.getApplicationContext();

        titles = new ArrayList<>();
        titles_All = new ArrayList<>();
        for (String s : strings){
            titles.add(s);
            titles_All.add(s);
        }
        following = new HashMap<>();
        for (int i = 0; i < follow; i++){
            following.put(titles_All.get(i), true);
        }
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
        public ImageView icon, tag;
        public TextView title;
        public LinearLayout layout_title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.category_list_item, null);
            holder = new ViewHolder();
            holder.icon = convertView.findViewById(R.id.category_item_icon);
            holder.title = convertView.findViewById(R.id.category_item_title);
            holder.tag = convertView.findViewById(R.id.category_item_tag);
            holder.layout_title = convertView.findViewById(R.id.category_item_title_layout);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tag.setTag(String.valueOf(position));
        holder.title.setText(titles.get(position));
        holder.icon.setImageResource(R.drawable.img);
        holder.layout_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Activity_Conferences.class);
                intent.putExtra("title", titles.get(position));
                mContext.startActivity(intent);
            }
        });
        holder.tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (following.get(titles.get(position)) == null || following.get(titles.get(position)) == false){
                    if (user.UpdatePreferCategorysValue(titles.get(position), true)){
                        following.put(titles.get(position), true);
                        ((ImageView)v).setImageResource(R.drawable.ic_bookmark_on);
                    }
                }
                else {
                    if (user.UpdatePreferCategorysValue(titles.get(position), false)) {
                        following.put(titles.get(position), false);
                        ((ImageView) v).setImageResource(R.drawable.ic_bookmark_off);
                    }
                }
            }
        });

        if (following.get(titles.get(position)) == null || following.get(titles.get(position)) == false){
            holder.tag.setImageResource(R.drawable.ic_bookmark_off);
        }
        else {
            holder.tag.setImageResource(R.drawable.ic_bookmark_on);
        }
        return convertView;
    }
}
