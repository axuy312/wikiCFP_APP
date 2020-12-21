package com.example.conference_infinity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyListAdapter_Discuss extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    ViewHolder holder = null;

    GlobalVariable user;

    List<HashMap<String, String>> discuss;

    public MyListAdapter_Discuss(Context context, List<HashMap<String, String>> data) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

        user = (GlobalVariable) mContext.getApplicationContext();

        freshDiscuss(data);
    }

    @Override
    public int getCount() {
        if (discuss == null)
            return 0;
        return discuss.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public ImageView headphoto;
        public TextView time, content, name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.discuss_list_item, null);
            holder = new ViewHolder();
            holder.content = convertView.findViewById(R.id.discuss_content);
            holder.time = convertView.findViewById(R.id.discuss_time);
            holder.name = convertView.findViewById(R.id.discuss_name);
            holder.headphoto = convertView.findViewById(R.id.headphoto_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (user.EmailToBitmap != null && user.EmailToBitmap.get(discuss.get(position).get("Email")) != null) {
            holder.headphoto.setImageBitmap(user.EmailToBitmap.get(discuss.get(position).get("Email")));
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        holder.time.setText(simpleDateFormat.format(new Date(Long.valueOf(discuss.get(position).get("Time")))));
        holder.content.setText(discuss.get(position).get("Content"));
        holder.name.setText(discuss.get(position).get("Name"));


        return convertView;
    }

    void freshDiscuss(List<HashMap<String, String>> data) {
        if (discuss != null) {
            discuss.clear();
        }


        if (data != null) {
            discuss = new ArrayList<>();
            HashMap<String, Boolean> tmpMail = new HashMap<>();
            for (HashMap<String, String> d : data) {
                HashMap<String, String> tmp = new HashMap<String, String>();
                tmp.put("Content", String.valueOf(d.get("Content")));
                tmp.put("Name", String.valueOf(d.get("Name")));
                tmp.put("Time", String.valueOf(d.get("Time")));
                tmp.put("Email", String.valueOf(d.get("Email")));

                tmpMail.put(d.get("Email"), true);

                discuss.add(tmp);
            }

            for (String mail : tmpMail.keySet().toArray(new String[0])) {
                user.storeBitmap(mail);
            }
            notifyDataSetChanged();
        }
    }
}
