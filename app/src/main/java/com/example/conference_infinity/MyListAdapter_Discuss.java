package com.example.conference_infinity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyListAdapter_Discuss extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;


    ViewHolder holder = null;

    GlobalVariable user;

    List<HashMap<String, String>> discuss;

    int loadImgFinish = 0;

    FirebaseFirestore db;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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

        Log.d("---debug---", String.valueOf(position));

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
                storeBitmap(mail);
            }
        }
    }

    void storeBitmap(String email) {
        if (email != null && (user.EmailToBitmap == null || user.EmailToBitmap.get(email) == null)) {
            if (db == null) {
                db = FirebaseFirestore.getInstance();
            }

            db.collection("User")
                    .document(email)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Map<String, Object> UserData = documentSnapshot.getData();
                            String url = UserData.get("HeadPhoto").toString();
                            if (url != null && !url.equals("N/A") && user.EmailToBitmap.get(email) == null) {
                                //Log.d("----TAG----", "Exist : " + url);
                                loadImgFinish += 1;
                                new GetBitmap().execute(url, email);
                            }
                            //Log.d("----TAG----", "here : " + url);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.d("----TAG----", "Error getting documents : " + e.getMessage());
                        }
                    });

        }
        else {
            notifyDataSetChanged();
        }
    }

    private class GetBitmap extends AsyncTask<String, Integer, Bitmap> {
        String urlStr, mail;

        @Override
        protected void onPreExecute() {
            //執行前
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //執行中

            urlStr = params[0];
            mail = params[1];
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

            //Log.d("----TAG2----", "Fun2");
            if (user.EmailToBitmap == null) {
                user.EmailToBitmap = new HashMap<>();
            }
            if (bitmap != null) {
                user.EmailToBitmap.put(mail, bitmap);

                sharedPreferences = mContext.getSharedPreferences("BitmapLoad", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();

                String data = sharedPreferences.getString("Email", "");
                //Log.d("----TAG2----", "Data add" + mail);
                if (data == null || data.isEmpty()) {
                    data = mail;
                } else {
                    data = data + "," + mail;
                }
                editor.putString("Email", data);
                editor.apply();
                loadImgFinish -= 1;
                //Log.d("----TAG2----", "Result: " + sharedPreferences.getString("Email", ""));
            } else {
                user.EmailToBitmap.put(mail, null);
                loadImgFinish -= 1;
            }
            if (loadImgFinish < 0){
                loadImgFinish = 0;
            }
            if (loadImgFinish == 0){
                Log.d("---debug---", "change");
                notifyDataSetChanged();
            }
        }
    }
}
