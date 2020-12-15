package com.example.conference_infinity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Article_Arrangement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Article_Arrangement extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String location = "";

    private GoogleMap mMap;

    public Fragment_Article_Arrangement(String where) {
        // Required empty public constructor
        location = where;
    }

    public Fragment_Article_Arrangement() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_article_arrangement.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Article_Arrangement newInstance(String param1, String param2) {
        Fragment_Article_Arrangement fragment = new Fragment_Article_Arrangement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //Log.d("TAG--------", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAG--------", "onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_arrangement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Log.d("TAG--------", "onViewCreate");
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("TAG--------", "onMapReady");
        mMap = googleMap;
        LatLng latLng = new LatLng(23.4696236,117.8357665);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


        Geocoder geocoder;
        List<Address> addressList = null;

        if (location != null && !location.equals("virtual")){
            geocoder = new Geocoder(getActivity());
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (addressList != null && !addressList.isEmpty()) {
            Address address = addressList.get(0);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            Log.d("TAG------", latLng.toString());
        }
        else {
            mMap.addMarker(new MarkerOptions().position(latLng).title("Here is Taiwan!"));
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7.0f));
    }
}