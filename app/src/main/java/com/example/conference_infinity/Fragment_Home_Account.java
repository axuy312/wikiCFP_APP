package com.example.conference_infinity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Home_Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Home_Account extends Fragment {

    /* 取得 fragment 的物件*/
    private CircleImageView user_photo;
    private TextView user_nickname;
    private Button preference_btn, history_btn, account_management_btn, logout_btn, about_btn;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private static final int IMAGE_REQUEST = 1;
    private Uri imgUri;
    private LinearLayout loadingImg;
    private String url, code;
    GlobalVariable user;
    String lang;

    public Fragment_Home_Account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Home_Account newInstance(String param1, String param2) {
        Fragment_Home_Account fragment = new Fragment_Home_Account();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get id from fragment
        user_photo = view.findViewById(R.id.profile_image);
        user_nickname = view.findViewById(R.id.user_name);
        preference_btn = view.findViewById(R.id.preference_btn);
        history_btn = view.findViewById(R.id.history_conference_btn);
        account_management_btn = view.findViewById(R.id.account_management_btn);
        logout_btn = view.findViewById(R.id.logout_btn);
        about_btn = view.findViewById(R.id.about_btn);
        loadingImg = view.findViewById(R.id.load_Img);

        user = (GlobalVariable) getActivity().getApplicationContext();
        lang = user.preferLangCode;

        // Set user name
        if (!user.userName.equals("N/A")) {
            user_nickname.setText(user.userName);
        } else {
            user_nickname.setText(getResources().getText(R.string.user_name));
        }

        // Set user head photo
        if (user.headPhoto != null) {
            user_photo.setImageBitmap(user.headPhoto);
        } else {
            user_photo.setImageDrawable(getResources().getDrawable(R.drawable.ic_default_profile));
        }

        //FireStore init
        storageReference = FirebaseStorage.getInstance().getReference("Uploads/Profile Picture/" + user.userEmail);

        //onclick listener
        preference_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Setting_Preference.class);
                startActivity(intent);
            }
        });
        history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        account_management_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear user data


                Intent intent = new Intent(getActivity(), Activity_Login.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Setting_About.class);
                startActivity(intent);
            }
        });

        user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
    }

    // TODO: 開啟本機圖片 選擇照片
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // TODO: 上傳本機照片到 FireStorage
    private void uploadImage(Bitmap bitmap) {
        storageReference = FirebaseStorage.getInstance().getReference("Uploads/Profile Picture/" + user.userEmail);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if (bitmap != null) {
            user.headPhoto = bitmap;
            final StorageReference fileReference = storageReference.child(user_nickname + "_HeadPhoto.png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            uploadTask = fileReference.putBytes(data);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    Log.d("TH---------", "then");
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Log.d("TH---------", "success");
                        Uri downloadUri = task.getResult();
                        user.headPhotoURL = downloadUri.toString();

                        //user.loadUser(user.userEmail);
                        user_photo.setImageBitmap(user.headPhoto);

                    } else {
                        user.headPhoto = null;
                        user.headPhotoURL = "N/A";
                        Log.d("TH---------", "fail");
                        Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_LONG).show();

                    }
                    //upload firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    // Create a new user with a first and last name

                    db.collection("User")
                            .document(user.userEmail)
                            .update("HeadPhoto", user.headPhotoURL)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TH---------", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TH---------", "Error writing document", e);
                                }
                            });
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d("TH---------", "onfail");
                    user.headPhoto = null;
                    user.headPhotoURL = "N/A";
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        } else {
            user.headPhoto = null;
            user.headPhotoURL = "N/A";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imgUri = data.getData();
            Bitmap bitmapImage = null;
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getActivity(), "Upload in preogress", Toast.LENGTH_LONG).show();
            } else {

                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imgUri);

                    bitmapImage = BitmapFactory.decodeStream(inputStream);
                    user.headPhoto = bitmapImage;
                    user_photo.setImageBitmap(bitmapImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                uploadImage(bitmapImage);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!lang.equals(user.preferLangCode)) {
            preference_btn.setText(getText(R.string.preference));
            history_btn.setText(getText(R.string.history_conference));
            account_management_btn.setText(getText(R.string.account_management));
            logout_btn.setText(getText(R.string.logout));
            about_btn.setText(getText(R.string.about));
        }
    }
}