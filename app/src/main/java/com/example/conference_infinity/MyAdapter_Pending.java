package com.example.conference_infinity;

import android.content.Context;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter_Pending extends RecyclerView.Adapter<MyHolder_Pending> {

    Context context;
    ArrayList<Model_Pending> modelPendings;    // this array list create a list of array which parameters define in our model class
    GlobalVariable user;

    public MyAdapter_Pending(Context context, ArrayList<Model_Pending> modelPendings) {
        this.context = context;
        this.modelPendings = modelPendings;
        //Log.d("---Size---", String.valueOf(models.size()) + " - " + String.valueOf(this.models.size()));
        user = (GlobalVariable) context.getApplicationContext();
    }

    @NonNull
    @Override
    public MyHolder_Pending onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create row layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.root_pending_row, parent, false);

        // this will return our view to holder class
        return new MyHolder_Pending(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder_Pending holder, int position) {
        // 設定要顯示的東西
        holder.mTitle.setText(modelPendings.get(position).getConference_name());
        holder.mLocation.setText(modelPendings.get(position).getConference_location());
        holder.mTime.setText(context.getText(R.string.pending_submit_deadline).toString() + modelPendings.get(position).getConference_time());
        holder.mAddItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 讓keyboard縮回去
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    // 加一個item到prepareitem中
                    if (!holder.mAddItem.getText().toString().isEmpty()) {
                        // Create Checkbox Dynamically
                        CheckBox checkBox = new CheckBox(context);
                        checkBox.setText(holder.mAddItem.getText().toString());
                        float scale = context.getResources().getDisplayMetrics().density;
                        checkBox.setPadding((int) (10 * scale), (int) (10 * scale), 0, (int) (10 * scale));
                        checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        checkBox.setGravity(Gravity.CENTER);
                        checkBox.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
                        checkBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!checkBox.isChecked()) {
                                    // 清除 checkbox 刪除線
                                    checkBox.setPaintFlags(checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                                } else {
                                    // 增加checkbox刪除線
                                    checkBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                }
                            }
                        });

                        holder.prepareItem.addView(checkBox);
                        holder.prepareItem.setVisibility(View.VISIBLE);
                        holder.mAddItem.getText().clear();

                        // add item to global variable
                        // how to do?
                        modelPendings.get(position).setPrepareThingState(checkBox.getText().toString(), false);
                        user.UpdateAttendConferencesValue(modelPendings.get(position).getAbbr(), modelPendings.get(position).getAttend(), checkBox.getText().toString(), false);
                    }

                    return true;
                }
                return false;
            }
        });

        int boolIndex;

        if (modelPendings.get(position).getPrepareThingCount() > 0) {
            boolIndex = 0;
            for (String item : modelPendings.get(position).getPrepareThing()) {
                // Create Checkbox Dynamically
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(item);
                float scale = context.getResources().getDisplayMetrics().density;
                checkBox.setPadding((int) (10 * scale), (int) (10 * scale), 0, (int) (10 * scale));
                checkBox.setChecked(modelPendings.get(position).getPrepareThingState(item));
                if (checkBox.isChecked()) {
                    checkBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    checkBox.setTextColor(context.getColor(R.color.light_gray));
                }
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

                        modelPendings.get(position).setPrepareThingState(checkBox.getText().toString(), checkBox.isChecked());
                        user.UpdateAttendConferencesValue(modelPendings.get(position).getAbbr(), modelPendings.get(position).getAttend(), checkBox.getText().toString(), checkBox.isChecked());
                    }
                });

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String msg = "You have " + (isChecked ? "checked" : "unchecked") + " this Check it Checkbox.";
                        //Log.d("checkbox debug", msg);
                    }
                });

                holder.prepareItem.addView(checkBox);
                holder.prepareItem.setVisibility(View.VISIBLE);
                boolIndex++;
            }

        }
    }

    @Override
    public int getItemCount() {
        return modelPendings.size();
    }

}
