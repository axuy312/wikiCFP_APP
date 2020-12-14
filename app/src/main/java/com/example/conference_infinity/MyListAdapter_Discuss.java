package com.example.conference_infinity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MyListAdapter_Discuss extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    ViewHolder holder = null;

    GlobalVariable user;

    List<HashMap<String, String>>discuss;
    HashMap<String, Bitmap>URLtoBitmap;

    public MyListAdapter_Discuss(Context context, List<HashMap<String, String>>data){
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

        user = (GlobalVariable)mContext.getApplicationContext();

        URLtoBitmap = new HashMap<String, Bitmap>();

        freshDiscuss(data);
    }

    @Override
    public int getCount() {
        return 2;
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
        public ImageView headphoto;
        public TextView time, content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.discuss_list_item, null);
            holder = new ViewHolder();
            holder.content = convertView.findViewById(R.id.discuss_content);
            holder.time = convertView.findViewById(R.id.discuss_time);
            holder.headphoto = convertView.findViewById(R.id.headphoto_item);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (URLtoBitmap.get(discuss.get(position).get("HeadPhoto")) != null) {
            Log.d("---URL---", "exist");
            holder.headphoto.setImageBitmap(URLtoBitmap.get(discuss.get(position).get("HeadPhoto")));
        }
        else {
            Log.d("---RUL---", "null");
        }

        holder.time.setText(discuss.get(position).get("Time"));
        holder.content.setText(discuss.get(position).get("Content"));

        return convertView;
    }

    void freshDiscuss(List<HashMap<String, String>>data){
        if (discuss != null){
            discuss.clear();
        }


        if (data != null){
            discuss = new ArrayList<>();
            for (HashMap<String, String> d : data){
                HashMap<String, String>tmp = new HashMap<String, String>();
                tmp.put("HeadPhoto", d.get("HeadPhoto"));
                tmp.put("Content", d.get("Content"));
                tmp.put("Time", d.get("Time"));

                Log.d("---url---",d.get("HeadPhoto"));

                if (URLtoBitmap.get((d.get("HeadPhoto"))) == null){
                    Log.d("---url---","add");
                    if (d.get("HeadPhoto").equals("N/A") != true){
                        try {
                            new GetBitmap().execute(d.get("HeadPhoto"));
                        } catch (Exception e) {
                            Log.d("---url---","catch");
                            Log.d("-----Discuss-----", d.get("HeadPhoto")+"----"+e.toString());
                            e.printStackTrace();
                        }
                    }
                }

                discuss.add(tmp);
            }
        }
    }

    private class GetBitmap extends AsyncTask<String, Integer, Bitmap> {

        String urlStr;

        @Override
        protected void onPreExecute() {
            //執行前
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //執行中

            urlStr = params[0];
            try {
                URL url = new URL(urlStr);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //執行進度
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //執行後
            super.onPostExecute(bitmap);
            URLtoBitmap.put(urlStr, bitmap);
            notifyDataSetChanged();
        }
    }
}
