package com.example.conference_infinity;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
                //Toast.makeText(v.getContext(), "title " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });

        this.mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "time " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });

        this.mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "location " + getAdapterPosition(), Toast.LENGTH_SHORT).show();

//                String uri = "http://maps.google.com/maps?saddr=" + "9982878" + "," + "76285774" + "&daddr=" + "9992084" + "," + "76286455";
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
//                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                mLocation.getContext().startActivity(intent);
            }
        });

        this.mAddItem.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //this.mAddItem.setOnEditorActionListener(new DoneOnEditorActionListener());

    }
}
