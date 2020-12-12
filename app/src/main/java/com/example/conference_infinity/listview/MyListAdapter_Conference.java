package com.example.conference_infinity.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.conference_item_title.setText(datas.get(position).get("Topic"));
        holder.conference_item_deadline.setText(datas.get(position).get("Deadline"));
        holder.comment_number.setText(String.valueOf(position));
        return convertView;
    }
}
