package com.example.conference_infinity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import at.markushi.ui.CircleButton;

import static android.app.Activity.RESULT_OK;

public class Fragment_Register_UploadIMG extends Fragment {

    private static final String TAG = "Upload Img Fragment";
    private ImageView circleButton;
    private GlobalVariable gb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create xml Layout (Same as Activity "OnCreate()")
        View view = inflater.inflate(R.layout.fragment_register_uploadimg, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Connect the button in xml, must add "view.findView" in Fragment
        CircleButton back_btn = view.findViewById(R.id.density_back_btn);
        Button next_btn = view.findViewById(R.id.density_next_btn);
        Button add_img_btn = view.findViewById(R.id.add_img_btn);
        circleButton = view.findViewById(R.id.register_head_img);
        gb = (GlobalVariable) getActivity().getApplicationContext();

        Log.d(TAG, "onCreateView: Started.");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Going to theme", Toast.LENGTH_SHORT).show();
                // navigate to the other fragment method
                ((Activity_Register_Register) getActivity()).setViewPager(new Root_Register_States().getFragmentNum(Root_Register_States.STATE_PASSWORD));
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Going to topics", Toast.LENGTH_SHORT).show();
                // navigate to the other fragment method
                ((Activity_Register_Register) getActivity()).setViewPager(new Root_Register_States().getFragmentNum(Root_Register_States.STATE_LANGUAGE));
            }
        });

        add_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//          // 測試版本
//                if(getActivity().checkSelfPermission(Build.VERSION.SDK_INT)>= Build.VERSION_CODES.M)
//                {
//                    if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
//                    {
//                        // permission not granted, request again
//                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
//                        // show popup for runtime permission
//                        requestPermissions(permissions,PERMISSION_CODE);
//                    }
//                }
                // 確定使用者有打開權限
                if (ActivityCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                } else {
                    startGallery();
                }
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                getActivity().startActivityForResult(Intent.createChooser(intent,"Pick a Image"),1);
            }
        });

        circleButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_headphoto_default));
    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        startActivityForResult(cameraIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmapImage = null;
        if (resultCode == RESULT_OK) {
            Uri returnUri = null;
            if (data != null) {
                returnUri = data.getData();
            }
//            circleButton.setImageURI(null);
//            circleButton.setImageURI(returnUri);

//            Bitmap bitmap = null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(returnUri);

                bitmapImage = BitmapFactory.decodeStream(inputStream);

                circleButton.setImageBitmap(bitmapImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //gb.headPhoto = Bitmap.createBitmap(bitmap);
            gb.headPhoto = bitmapImage;
            //circleButton.setImageBitmap(bitmap);
        }
    }
}
