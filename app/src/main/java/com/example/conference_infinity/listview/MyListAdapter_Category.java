package com.example.conference_infinity.listview;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.conference_infinity.R;

public class MyListAdapter_Category extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private String[] titles;
    private int data_len = 0;

    public MyListAdapter_Category(Context context, String[] strings){
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        if(strings != null){
            titles = strings.clone();
            data_len = titles.length;
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
        holder.title.setText(titles[position]);
        holder.icon.setImageResource(R.drawable.img);
        holder.tag.setImageResource(R.drawable.ic_bookmark_off);
        return convertView;
    }
}
