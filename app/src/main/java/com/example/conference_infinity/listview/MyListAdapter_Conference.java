package com.example.conference_infinity.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.conference_infinity.R;

import java.util.Dictionary;
import java.util.HashMap;

public class MyListAdapter_Conference extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private HashMap<String, String>[] datas;
    private final String title;
    private int data_len = 0;

    public MyListAdapter_Conference(Context context, HashMap[] dictionaries, String t){
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        title = t;
        if(dictionaries != null){
            datas = dictionaries.clone();
            data_len = datas.length;
        }
    }

    @Override
    public int getCount() {
        return data_len;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        public ImageView category_item_icon;
        public TextView category_item_title, conference_item_title, conference_item_deadline, comment_number;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.conference_list_item, null);
            holder = new ViewHolder();
            holder.category_item_icon = convertView.findViewById(R.id.category_item_icon);
            holder.category_item_title = convertView.findViewById(R.id.category_item_title);
            holder.conference_item_title = convertView.findViewById(R.id.conference_item_title);
            holder.conference_item_deadline = convertView.findViewById(R.id.conference_item_deadline);
            holder.comment_number = convertView.findViewById(R.id.comment_number);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.category_item_icon.setImageResource(R.drawable.img);
        holder.category_item_title.setText(title);
        holder.conference_item_title.setText(datas[position].get("Topic"));
        holder.conference_item_deadline.setText(datas[position].get("Deadline"));
        holder.comment_number.setText(String.valueOf(position));
        return convertView;
    }
}
