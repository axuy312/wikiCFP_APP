package com.example.conference_infinity;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    Context context;
    ArrayList<Model> models;    // this array list create a list of array which parameters define in our model class

    public MyAdapter(Context context, ArrayList<Model> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create row layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.root_pending_row, null);

        // this will return our view to holder class
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        // 設定要顯示的東西
        holder.mTitle.setText(models.get(position).getConference_name());
        holder.mLocation.setText(models.get(position).getConference_location());
        holder.mTime.setText(models.get(position).getConference_time());

        ArrayList<String> setItem = new ArrayList<>();
        setItem = models.get(position).getPrepareThings();
        ArrayList<Boolean> ItemStates = new ArrayList<>();
        ItemStates = models.get(position).getPrepareThingsStatus();
        int boolIndex;

        if (setItem.size() > 0) {
            boolIndex = 0;
            for (String item : setItem) {
                // Create Checkbox Dynamically
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(item);
                float scale = context.getResources().getDisplayMetrics().density;
                checkBox.setPadding((int) (10 * scale), (int) (10 * scale), 0, (int) (10 * scale));
                //checkBox.setChecked(ItemStates.get(boolIndex));
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                checkBox.setGravity(Gravity.CENTER);
                checkBox.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkBox.isChecked()) {
                            // 設定checkbox狀態
                            //checkBox.setChecked(!checkBox.isChecked());
                            // 清除 checkbox 刪除線
                            checkBox.setPaintFlags(checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            checkBox.setTextColor(v.getResources().getColor(R.color.dark_gray));
                        } else {
                            // 設定checkbox狀態
                            //checkBox.setChecked(!checkBox.isChecked());
                            // 增加checkbox刪除線
                            checkBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            checkBox.setTextColor(v.getResources().getColor(R.color.light_gray));
                        }
                    }
                });

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String msg = "You have " + (isChecked ? "checked" : "unchecked") + " this Check it Checkbox.";
                        Log.d("checkbox debug", msg);
                    }
                });

                holder.prepareItem.addView(checkBox);
                boolIndex++;
            }

        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
