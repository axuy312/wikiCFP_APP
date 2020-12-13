package com.example.conference_infinity.listview;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.conference_infinity.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyListAdapter_Category extends BaseAdapter implements Filterable {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private List<String> titles;
    private List<String> titles_All;
    private int followNum = 0;

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
        followNum = follow;
        if(strings != null){
            titles = new ArrayList<>();
            titles_All = new ArrayList<>();
            for (String s : strings){
                titles.add(s);
                titles_All.add(s);
            }
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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.category_list_item, null);
            holder = new ViewHolder();
            holder.icon = convertView.findViewById(R.id.category_item_icon);
            holder.title = convertView.findViewById(R.id.category_item_title);
            holder.tag = convertView.findViewById(R.id.category_item_tag);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.title.setText(titles.get(position));
        holder.icon.setImageResource(R.drawable.img);
        if (position < followNum){
            holder.tag.setImageResource(R.drawable.ic_bookmark_on);
        }
        else {
            holder.tag.setImageResource(R.drawable.ic_bookmark_off);
        }
        return convertView;
    }
}
