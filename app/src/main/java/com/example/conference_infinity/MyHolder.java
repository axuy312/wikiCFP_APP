package com.example.conference_infinity;

import android.content.Context;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder {

    TextView mTitle;
    AppCompatButton mTime, mLocation;
    EditText mAddItem;
    LinearLayout prepareItem;

    MyHolder(@NonNull View itemView) {
        super(itemView);

        // 取得一個cardview內的elements
        this.mTitle = itemView.findViewById(R.id.pending_conference_name);
        this.mTime = itemView.findViewById(R.id.pending_conference_time);
        this.mLocation = itemView.findViewById(R.id.pending_conference_location);
        this.prepareItem = itemView.findViewById(R.id.prepare_things_layout);       // add prepare things checkbox and textView place
        this.mAddItem = itemView.findViewById(R.id.add_prepare_item);               // add things button

        this.mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "title " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });

        this.mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "time " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });

        this.mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "location " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });

        this.mAddItem.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //this.mAddItem.setOnEditorActionListener(new DoneOnEditorActionListener());
        this.mAddItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 讓keyboard縮回去
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    // 加一個item到prepareitem中
                    if (!mAddItem.getText().toString().isEmpty()) {
                        // Create Checkbox Dynamically
                        CheckBox checkBox = new CheckBox(prepareItem.getContext());
                        checkBox.setText(mAddItem.getText().toString());
                        float scale = prepareItem.getContext().getResources().getDisplayMetrics().density;
                        checkBox.setPadding((int) (10 * scale), (int) (10 * scale), 0, (int) (10 * scale));
                        //checkBox.setChecked(ItemStates.get(boolIndex));
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

                        prepareItem.addView(checkBox);
                        mAddItem.getText().clear();
                    }


                    return true;
                }
                return false;
            }
        });
    }
}
